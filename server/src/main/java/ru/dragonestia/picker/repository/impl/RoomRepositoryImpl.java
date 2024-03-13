package ru.dragonestia.picker.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.dragonestia.picker.api.exception.NodeNotFoundException;
import ru.dragonestia.picker.api.exception.RoomAlreadyExistException;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.repository.UserRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepository {

    private final UserRepository userRepository;
    private final PickerRepository pickerRepository;
    private final Map<Node, Rooms> node2roomsMap = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock(true);

    @Override
    public void create(Room room) throws RoomAlreadyExistException {
        var nodeId = room.getNodeIdentifier();

        lock.writeLock().lock();
        try {
            var node = node2roomsMap.keySet().stream()
                    .filter(n -> room.getNodeIdentifier().equals(n.getIdentifier()))
                    .findFirst();

            if (node.isEmpty()) {
                throw new IllegalArgumentException("Node '" + nodeId + "' does not exist");
            }

            var rooms = node2roomsMap.get(node.get());
            if (rooms.containsKey(room.getIdentifier())) {
                throw new RoomAlreadyExistException(room.getNodeIdentifier(), room.getIdentifier());
            }
            rooms.put(room.getIdentifier(), new RoomContainer(room, new AtomicInteger(0)));
            pickerRepository.find(room.getNodeIdentifier()).add(room);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void remove(Room room) {
        var nodeId = room.getNodeIdentifier();
        var node = node2roomsMap.keySet().stream()
                .filter(n -> room.getNodeIdentifier().equals(n.getIdentifier()))
                .findFirst();

        lock.writeLock().lock();
        try {
            if (node.isEmpty()) {
                throw new NodeNotFoundException("Node '" + nodeId + "' does not exist");
            }

            node2roomsMap.get(node.get()).remove(room.getIdentifier());
            pickerRepository.find(room.getNodeIdentifier()).remove(room);

            userRepository.onRemoveRoom(room);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Optional<Room> find(Node node, String identifier) {
        lock.readLock().lock();
        try {
            if (!node2roomsMap.containsKey(node)) {
                throw new NodeNotFoundException("Node '" + node.getIdentifier() + "' does not exist");
            }

            var result = node2roomsMap.get(node).getOrDefault(identifier, null);
            return result == null? Optional.empty() : Optional.of(result.room());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Room> all(Node node) {
        lock.readLock().lock();
        try {
            if (!node2roomsMap.containsKey(node)) {
                throw new NodeNotFoundException("Node '%s' does not exists".formatted(node.getIdentifier()));
            }

            return node2roomsMap.get(node).values().stream().map(RoomContainer::room).toList();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Optional<Room> pickFree(Node node, Collection<User> users) {
        lock.writeLock().lock();
        try {
            if (!node2roomsMap.containsKey(node)) {
                throw new NodeNotFoundException("Node '" + node.getIdentifier() + "' does not exist");
            }

            Room room = null;
            try {
                room = pickerRepository.pick(node.getIdentifier(), users);
            } catch (RuntimeException ignore) {} // TODO: may be problem. Check it later

            Optional<RoomContainer> container = room == null?
                    Optional.empty() :
                    Optional.of(node2roomsMap.get(node).get(room.getIdentifier()));

            if (container.isPresent()) {
                var cont = container.get();
                var addedUsers = userRepository.linkWithRoom(cont.room(), users, false);
                cont.used().getAndAdd((int) addedUsers.values().stream().filter(Boolean.TRUE::equals).count());
            }

            return container.map(RoomContainer::room);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void onCreateNode(Node node) {
        lock.writeLock().lock();
        try {
            node2roomsMap.put(node, new Rooms());
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public List<Room> onRemoveNode(Node node) {
        lock.writeLock().lock();
        try {
            var deleted = node2roomsMap.get(node).values().stream().map(container -> container.room).toList();
            node2roomsMap.remove(node);

            return deleted;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private record RoomContainer(Room room, AtomicInteger used) {

        public boolean isAvailable(int requiredSlots) {
            return room.isAvailable(used.get(), requiredSlots);
        }
    }

    private static class Rooms extends LinkedHashMap<String, RoomContainer> {}
}
