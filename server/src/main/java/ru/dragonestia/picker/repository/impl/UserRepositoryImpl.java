package ru.dragonestia.picker.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.dragonestia.picker.api.exception.RoomAreFullException;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.UserRepository;
import ru.dragonestia.picker.repository.impl.cache.NodeId2PickerModeCache;
import ru.dragonestia.picker.repository.impl.picker.LeastPickedPicker;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final NodeId2PickerModeCache nodeId2PickerModeCache;
    private final Map<User, Set<Room>> usersMap = new ConcurrentHashMap<>();
    private final Map<NodeRoomPath, Set<User>> roomUsers = new ConcurrentHashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock(true);

    @Override
    public int linkWithRoom(Room room, Collection<User> users, boolean force) throws RoomAreFullException {
        var toAdd = new HashSet<User>();

        lock.writeLock().lock();
        try {
            var path = new NodeRoomPath(room.getNodeIdentifier(), room.getIdentifier());
            var usersSet = roomUsers.getOrDefault(path, new HashSet<>());

            if (!force && !room.hasUnlimitedSlots()) {
                if (room.getMaxSlots() < usersSet.size() + users.size()) {
                    throw new RoomAreFullException(room.getNodeIdentifier(), room.getIdentifier());
                }
            }

            users.forEach(user -> {
                var set = usersMap.getOrDefault(user, new HashSet<>());
                if (!set.contains(room)) {
                    toAdd.add(user);
                    set.add(room);
                }
                usersMap.put(user, set);
            });

            usersSet.addAll(toAdd);
            roomUsers.put(path, usersSet);

            var picker = nodeId2PickerModeCache.get(room.getNodeIdentifier());
            if (picker instanceof LeastPickedPicker leastPickedPicker) {
                leastPickedPicker.updateUsersAmount(room, roomUsers.get(path).size());
            }
        } finally {
            lock.writeLock().unlock();
        }
        return toAdd.size();
    }

    @Override
    public void unlinkWithRoom(Room room, Collection<User> users) {
        var counter = new AtomicInteger();

        lock.writeLock().lock();
        try {
            usersMap.forEach((user, set) -> {
                if (!set.contains(room)) return;

                set.remove(room);
                counter.incrementAndGet();

                if (set.isEmpty()) {
                    usersMap.remove(user);
                }
            });

            var path = new NodeRoomPath(room.getNodeIdentifier(), room.getIdentifier());
            var set = roomUsers.getOrDefault(path, new HashSet<>());
            set.removeAll(users);
            if (set.isEmpty()) {
                roomUsers.remove(path);
            } else {
                roomUsers.put(path, set);
            }

            var picker = nodeId2PickerModeCache.get(room.getNodeIdentifier());
            if (picker instanceof LeastPickedPicker leastPickedPicker) {
                leastPickedPicker.updateUsersAmount(room, set.size());
            }
        } finally {
            lock.writeLock().unlock();
        }
        counter.get();
    }

    @Override
    public List<Room> findAllLinkedUserRooms(User user) {
        lock.writeLock().lock();
        try {
            return usersMap.getOrDefault(user, new HashSet<>()).stream().toList();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void onRemoveRoom(Room room) {
        lock.writeLock().lock();
        try {
            var users = roomUsers.remove(new NodeRoomPath(room.getNodeIdentifier(), room.getIdentifier()));

            if (users == null) return;

            users.forEach(user -> {
                var set = usersMap.getOrDefault(user, new HashSet<>());
                set.remove(room);

                if (set.isEmpty()) {
                    usersMap.remove(user);
                }
            });
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public List<User> usersOf(Room room) {
        lock.readLock().lock();
        try {
            return roomUsers.getOrDefault(new NodeRoomPath(room.getNodeIdentifier(), room.getIdentifier()), new HashSet<>())
                    .stream()
                    .toList();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<User> search(String input) {
        lock.readLock().lock();
        try {
            return usersMap.keySet().stream()
                    .filter(user -> user.getIdentifier().startsWith(input))
                    .sorted(Comparator.comparing(User::getIdentifier))
                    .toList();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int countAllUsers() {
        lock.readLock().lock();
        try {
            return usersMap.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Map<String, Integer> countUsersForNodes() {
        var map = new HashMap<String, Set<User>>();

        lock.readLock().lock();
        try {
            roomUsers.forEach((path, users) -> {
                if (map.containsKey(path.node)) {
                    map.get(path.node).addAll(users);
                    return;
                }

                map.put(path.node, new HashSet<>(users));
            });
        } finally {
            lock.readLock().unlock();
        }

        var result = new HashMap<String, Integer>();
        map.forEach((node, users) -> result.put(node, users.size()));

        return result;
    }

    private record NodeRoomPath(String node, String room) {

        @Override
        public int hashCode() {
            return Objects.hash(node, room);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            if (o == this) return true;
            if (o instanceof NodeRoomPath other) {
                return other.node().equals(node()) && other.room().equals(room());
            }
            return false;
        }
    }
}
