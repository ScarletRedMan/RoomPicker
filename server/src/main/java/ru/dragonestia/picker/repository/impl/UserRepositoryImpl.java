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

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final NodeId2PickerModeCache nodeId2PickerModeCache;
    private final Map<User, Set<Room>> usersMap = new ConcurrentHashMap<>();
    private final Map<NodeRoomPath, Set<User>> roomUsers = new ConcurrentHashMap<>();

    @Override
    public Map<User, Boolean> linkWithRoom(Room room, Collection<User> users, boolean force) throws RoomAreFullException {
        var result = new HashMap<User, Boolean>();

        synchronized (usersMap) {
            var path = new NodeRoomPath(room.getNodeIdentifier(), room.getIdentifier());
            var usersSet = roomUsers.getOrDefault(path, new HashSet<>());

            if (force || room.hasUnlimitedSlots()) {
                users.forEach(user -> result.put(user, true));
            } else {
                for (var user : users) {
                    var set = usersMap.getOrDefault(user, new HashSet<>());
                    result.put(user, !set.contains(room));
                }

                if (room.getMaxSlots() < usersSet.size() + users.size()) {
                    throw new RoomAreFullException(room.getNodeIdentifier(), room.getIdentifier());
                }
            }

            for (var user: users) {
                var set = usersMap.getOrDefault(user, new HashSet<>());
                set.add(room);
                usersMap.put(user, set);
            }

            usersSet.addAll(users);
            roomUsers.put(path, usersSet);

            var picker = nodeId2PickerModeCache.get(room.getNodeIdentifier());
            if (picker instanceof LeastPickedPicker leastPickedPicker) {
                leastPickedPicker.updateUsersAmount(room, roomUsers.get(path).size());
            }
        }

        return result;
    }

    @Override
    public int unlinkWithRoom(Room room, Collection<User> users) {
        var counter = new AtomicInteger();
        synchronized (usersMap) {
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
        }
        return counter.get();
    }

    @Override
    public List<Room> findAllLinkedUserRooms(User user) {
        synchronized (usersMap) {
            return usersMap.getOrDefault(user, new HashSet<>()).stream().toList();
        }
    }

    @Override
    public void onRemoveRoom(Room room) {
        synchronized (usersMap) {
            usersMap.forEach((user, set) -> {
                set.remove(room);
                if (set.isEmpty()) {
                    usersMap.remove(user);
                }
            });
        }
    }

    @Override
    public List<User> usersOf(Room room) {
        synchronized (usersMap) {
            return roomUsers.getOrDefault(new NodeRoomPath(room.getNodeIdentifier(), room.getIdentifier()), new HashSet<>())
                    .stream()
                    .toList();
        }
    }

    @Override
    public List<User> search(String input) {
        synchronized (usersMap) {
            return usersMap.keySet().stream()
                    .filter(user -> user.getIdentifier().startsWith(input))
                    .sorted(Comparator.comparing(User::getIdentifier))
                    .toList();
        }
    }

    private record NodeRoomPath(String node, String bucket) {

        @Override
        public int hashCode() {
            return Objects.hash(node, bucket);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            if (o == this) return true;
            if (o instanceof NodeRoomPath other) {
                return other.node().equals(node()) && other.bucket().equals(bucket());
            }
            return false;
        }
    }
}
