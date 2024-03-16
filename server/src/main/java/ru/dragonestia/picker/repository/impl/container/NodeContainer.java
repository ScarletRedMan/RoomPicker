package ru.dragonestia.picker.repository.impl.container;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.exception.RoomAlreadyExistException;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.impl.picker.LeastPickedPicker;
import ru.dragonestia.picker.repository.impl.picker.RoomPicker;
import ru.dragonestia.picker.repository.impl.picker.RoundRobinPicker;
import ru.dragonestia.picker.repository.impl.picker.SequentialFillingPicker;
import ru.dragonestia.picker.repository.impl.type.UserTransaction;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NodeContainer {

    @Getter
    private final Node node;
    private final UserTransaction.Listener transactionListener;
    private final RoomPicker picker;

    private final ReadWriteLock roomLock = new ReentrantReadWriteLock();
    private final Map<String, RoomContainer> rooms = new ConcurrentHashMap<>();

    public NodeContainer(@NotNull Node node, @NotNull UserTransaction.Listener transactionListener) {
        this.node = node;
        this.transactionListener = transactionListener;
        this.picker = initPicker();
    }

    private @NotNull RoomPicker initPicker() {
        return switch (node.getPickingMethod()) {
            case SEQUENTIAL_FILLING -> new SequentialFillingPicker();
            case ROUND_ROBIN -> new RoundRobinPicker();
            case LEAST_PICKED -> new LeastPickedPicker();
        };
    }

    public void addRoom(Room room) throws RoomAlreadyExistException {
        roomLock.writeLock().lock();
        try {
            if (rooms.containsKey(room.getIdentifier())) {
                throw new RoomAlreadyExistException(node.getIdentifier(), room.getIdentifier());
            }

            var container = new RoomContainer(room, this);
            rooms.put(room.getIdentifier(), container);
            picker.add(container);
        } finally {
            roomLock.writeLock().unlock();
        }
    }

    public void removeRoom(@NotNull Room room) {
        roomLock.writeLock().lock();
        try {
            picker.remove(rooms.remove(room.getIdentifier()));
        } finally {
            roomLock.writeLock().unlock();
        }
    }

    public void removeRoomsByIds(@NotNull Collection<String> roomIds) {
        roomLock.writeLock().lock();
        try {
            roomIds.forEach(roomId -> picker.remove(rooms.remove(roomId)));
        } finally {
            roomLock.writeLock().unlock();
        }
    }

    public @NotNull Optional<RoomContainer> findRoomById(@NotNull String roomId) {
        roomLock.readLock().lock();
        try {
            return Optional.ofNullable(rooms.get(roomId));
        } finally {
            roomLock.readLock().unlock();
        }
    }

    public @NotNull Collection<RoomContainer> allRooms() {
        roomLock.readLock().lock();
        try {
            return rooms.values();
        } finally {
            roomLock.readLock().unlock();
        }
    }

    public @NotNull Room pick(@NotNull Set<User> users) {
        var room = picker.pick(users);
        transactionListener.accept(new UserTransaction(room.getRoom(), users));
        return room.getRoom();
    }

    public @NotNull RoomPicker getPicker() {
        return picker;
    }
}
