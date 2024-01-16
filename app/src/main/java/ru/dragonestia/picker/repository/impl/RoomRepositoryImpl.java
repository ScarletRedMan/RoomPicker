package ru.dragonestia.picker.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.dragonestia.picker.api.exception.RoomAlreadyExistException;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepository {

    private final UserRepository userRepository;
    private final PickerRepository pickerRepository;
    private final Map<Node, Rooms> node2roomsMap = new ConcurrentHashMap<>();

    @Override
    public void create(Room room) throws RoomAlreadyExistException {
        var nodeId = room.getNodeId();

        synchronized (node2roomsMap) {
            var node = node2roomsMap.keySet().stream()
                    .filter(n -> room.getNodeId().equals(n.id()))
                    .findFirst();

            if (node.isEmpty()) {
                throw new IllegalArgumentException("Node '" + nodeId + "' does not exist");
            }

            var rooms = node2roomsMap.get(node.get());
            if (rooms.containsKey(room.getId())) {
                throw new RoomAlreadyExistException(room.getNodeId(), room.getId());
            }
            rooms.put(room.getId(), new RoomContainer(room, new AtomicInteger(0)));
            pickerRepository.find(room.getNodeId()).add(room);
        }
    }

    @Override
    public void remove(Room room) {
        var nodeId = room.getNodeId();
        var node = node2roomsMap.keySet().stream()
                .filter(n -> room.getNodeId().equals(n.id()))
                .findFirst();

        synchronized (node2roomsMap) {
            if (node.isEmpty()) {
                throw new IllegalArgumentException("Node '" + nodeId + "' does not exist");
            }

            node2roomsMap.get(node.get()).remove(room.getId());
            pickerRepository.find(room.getNodeId()).remove(room);
        }

        userRepository.onRemoveRoom(room);
    }

    @Override
    public Optional<Room> find(Node node, String identifier) {
        synchronized (node2roomsMap) {
            if (!node2roomsMap.containsKey(node)) {
                throw new IllegalArgumentException("Node '" + node.id() + "' does not exist");
            }

            var result = node2roomsMap.get(node).getOrDefault(identifier, null);
            return result == null? Optional.empty() : Optional.of(result.room());
        }
    }

    @Override
    public List<Room> all(Node node) {
        synchronized (node2roomsMap) {
            return node2roomsMap.get(node).values().stream().map(RoomContainer::room).toList();
        }
    }

    @Override
    public int countAvailable(Node node, int requiredSlots) {
        return (int) node2roomsMap.get(node).values().stream()
                .filter(bucket -> bucket.isAvailable(requiredSlots))
                .count();
    }

    @Override
    public Optional<Room> pickFree(Node node, Collection<User> users) {
        synchronized (node2roomsMap) {
            if (!node2roomsMap.containsKey(node)) {
                throw new IllegalArgumentException("Node '" + node.id() + "' does not exist");
            }

            Room room = null;
            try {
                room = pickerRepository.pick(node.id(), users);
            } catch (RuntimeException ignore) {} // TODO: may be problem. Check it later

            Optional<RoomContainer> container = room == null?
                    Optional.empty() :
                    Optional.of(node2roomsMap.get(node).get(room.getId()));

            if (container.isPresent()) {
                var cont = container.get();
                var addedUsers = userRepository.linkWithRoom(cont.room(), users, false);
                cont.used().getAndAdd((int) addedUsers.values().stream().filter(Boolean.TRUE::equals).count());
            }

            return container.map(RoomContainer::room);
        }
    }

    @Override
    public void onCreateNode(Node node) {
        synchronized (node2roomsMap) {
            node2roomsMap.put(node, new Rooms());
        }
    }

    @Override
    public void onRemoveNode(Node node) {
        synchronized (node2roomsMap) {
            node2roomsMap.remove(node);
        }
    }

    private record RoomContainer(Room room, AtomicInteger used) {

        public boolean isAvailable(int requiredSlots) {
            return room.isAvailable(used.get(), requiredSlots);
        }
    }

    private static class Rooms extends LinkedHashMap<String, RoomContainer> {}
}
