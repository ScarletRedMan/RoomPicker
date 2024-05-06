package ru.dragonestia.picker.repository.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.api.exception.NodeNotFoundException;
import ru.dragonestia.picker.api.exception.RoomAreFullException;
import ru.dragonestia.picker.api.exception.RoomNotFoundException;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.UserRepository;
import ru.dragonestia.picker.repository.impl.container.RoomContainer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final ContainerRepository containerRepository;

    private final Map<User, Set<Room>> userRooms = new ConcurrentHashMap<>();

    @PostConstruct
    void init() {
        containerRepository.setTransactionListener(transaction -> {
            synchronized (userRooms) {
                for (var user: transaction.target()) {
                    var set = userRooms.computeIfAbsent(user, k -> new HashSet<>());
                    set.add(transaction.room());
                }
            }
        });
    }

    @Override
    public void linkWithRoom(Room room, Collection<User> users, boolean force) throws RoomAreFullException {
        synchronized (userRooms) {
            getRoomContainer(room).addUsers(users, force);

            for (var user: users) {
                var set = userRooms.computeIfAbsent(user, k -> new HashSet<>());
                set.add(room);
            }
        }
    }

    @Override
    public void unlinkWithRoom(Room room, Collection<User> users) {
        synchronized (userRooms) {
            getRoomContainer(room).removeUsers(users);

            for (var user: users) {
                var set = userRooms.get(user);
                if (set == null) continue;
                set.remove(room);
                if (set.isEmpty()) userRooms.remove(user);
            }
        }
    }

    @Override
    public Collection<Room> findAllLinkedUserRooms(User user) {
        var result = userRooms.get(user);
        return Collections.unmodifiableSet(result == null? new HashSet<>() : result);
    }

    @Override
    public Collection<User> usersOf(Room room) {
        return getRoomContainer(room).allUsers();
    }

    @Override
    public Collection<User> search(String input) {
        return userRooms.keySet().stream().filter(user -> user.getIdentifier().startsWith(input)).toList();
    }

    @Override
    public int countAllUsers() {
        return userRooms.size();
    }

    @Override
    public Map<String, Integer> countUsersForNodes() {
        var result = new HashMap<String, Integer>();

        containerRepository.all().forEach(nodeContainer -> {
            var nodeId = nodeContainer.getNode().getIdentifier();

            nodeContainer.allRooms().forEach(roomContainer -> {
                result.put(nodeId, result.getOrDefault(nodeId, 0) + roomContainer.countUsers());
            });
        });

        return result;
    }

    private RoomContainer getRoomContainer(Room room) {
        return containerRepository.findById(room.getNodeIdentifier())
                .orElseThrow(() -> new NodeNotFoundException(room.getNodeIdentifier()))
                .findRoomById(room.getIdentifier())
                .orElseThrow(() -> new RoomNotFoundException(room.getNodeIdentifier(), room.getIdentifier()));
    }
}
