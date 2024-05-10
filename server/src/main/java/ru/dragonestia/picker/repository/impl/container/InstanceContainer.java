package ru.dragonestia.picker.repository.impl.container;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.exception.RoomAlreadyExistException;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.model.entity.Entity;
import ru.dragonestia.picker.repository.impl.picker.LeastPickedPicker;
import ru.dragonestia.picker.repository.impl.picker.RoomPicker;
import ru.dragonestia.picker.repository.impl.picker.RoundRobinPicker;
import ru.dragonestia.picker.repository.impl.picker.SequentialFillingPicker;
import ru.dragonestia.picker.repository.impl.type.EntityTransaction;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class InstanceContainer {

    @Getter
    private final Instance instance;
    private final EntityTransaction.Listener transactionListener;
    private final RoomPicker picker;

    private final ReadWriteLock roomLock = new ReentrantReadWriteLock();
    private final Map<String, RoomContainer> rooms = new ConcurrentHashMap<>();

    public InstanceContainer(@NotNull Instance instance, @NotNull EntityTransaction.Listener transactionListener) {
        this.instance = instance;
        this.transactionListener = transactionListener;
        this.picker = initPicker();
    }

    private @NotNull RoomPicker initPicker() {
        return switch (instance.getPickingMethod()) {
            case SEQUENTIAL_FILLING -> new SequentialFillingPicker(this);
            case ROUND_ROBIN -> new RoundRobinPicker(this);
            case LEAST_PICKED -> new LeastPickedPicker(this);
        };
    }

    public void addRoom(Room room) throws RoomAlreadyExistException {
        roomLock.writeLock().lock();
        try {
            if (rooms.containsKey(room.getIdentifier())) {
                throw new RoomAlreadyExistException(instance.getIdentifier(), room.getIdentifier());
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

    public @NotNull Room pick(@NotNull Set<Entity> entities) {
        synchronized (picker) {
            var room = picker.pick(entities);
            room.addEntities(entities, false);
            transactionListener.accept(new EntityTransaction(room.getRoom(), entities));
            return room.getRoom();
        }
    }

    public @NotNull RoomPicker getPicker() {
        return picker;
    }
}
