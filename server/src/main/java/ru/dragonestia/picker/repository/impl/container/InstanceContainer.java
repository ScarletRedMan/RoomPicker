package ru.dragonestia.picker.repository.impl.container;

import lombok.Getter;
import ru.dragonestia.picker.exception.AlreadyExistsException;
import ru.dragonestia.picker.model.entity.EntityId;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.model.entity.Entity;
import ru.dragonestia.picker.model.room.RoomId;
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

    @Getter private final Instance instance;
    private final EntityTransaction.Listener transactionListener;
    @Getter private final RoomPicker picker;

    private final ReadWriteLock roomLock = new ReentrantReadWriteLock();
    private final Map<RoomId, RoomContainer> rooms = new ConcurrentHashMap<>();

    public InstanceContainer(Instance instance, EntityTransaction.Listener transactionListener) {
        this.instance = instance;
        this.transactionListener = transactionListener;
        this.picker = initPicker();
    }

    private RoomPicker initPicker() {
        return switch (instance.getPickingMethod()) {
            case SEQUENTIAL_FILLING -> new SequentialFillingPicker(this);
            case ROUND_ROBIN -> new RoundRobinPicker(this);
            case LEAST_PICKED -> new LeastPickedPicker(this);
        };
    }

    public void addRoom(Room room) throws AlreadyExistsException {
        roomLock.writeLock().lock();
        try {
            if (rooms.containsKey(room.getId())) {
                throw AlreadyExistsException.forRoom(instance.getId(), room.getId());
            }

            var container = new RoomContainer(room, this);
            rooms.put(room.getId(), container);
            picker.add(container);
        } finally {
            roomLock.writeLock().unlock();
        }
    }

    public void removeRoom(RoomId roomId) {
        roomLock.writeLock().lock();
        try {
            picker.remove(rooms.remove(roomId));
        } finally {
            roomLock.writeLock().unlock();
        }
    }

    public void removeRoomsByIds(Collection<RoomId> roomIds) {
        roomLock.writeLock().lock();
        try {
            roomIds.forEach(roomId -> picker.remove(rooms.remove(roomId)));
        } finally {
            roomLock.writeLock().unlock();
        }
    }

    public Optional<RoomContainer> findRoomById(RoomId roomId) {
        roomLock.readLock().lock();
        try {
            return Optional.ofNullable(rooms.get(roomId));
        } finally {
            roomLock.readLock().unlock();
        }
    }

    public Collection<RoomContainer> allRooms() {
        roomLock.readLock().lock();
        try {
            return rooms.values();
        } finally {
            roomLock.readLock().unlock();
        }
    }

    public Room pick(Set<EntityId> entities) {
        var entitiesObj = entities.stream()
                .map(Entity::new)
                .toList(); // TODO: find entities

        synchronized (picker) {
            var room = picker.pick(entitiesObj);
            room.addEntities(entitiesObj, false);
            transactionListener.accept(new EntityTransaction(room.getRoom(), entitiesObj));
            return room.getRoom();
        }
    }
}
