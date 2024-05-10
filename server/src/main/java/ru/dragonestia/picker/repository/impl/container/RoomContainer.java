package ru.dragonestia.picker.repository.impl.container;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.exception.RoomAreFullException;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.model.entity.Entity;
import ru.dragonestia.picker.repository.impl.picker.LeastPickedPicker;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RoomContainer {

    @Getter
    private final Room room;
    private final InstanceContainer container;

    private final ReadWriteLock entityLock = new ReentrantReadWriteLock(true);
    private final Set<Entity> entities = new HashSet<>();

    public RoomContainer(@NotNull Room room, @NotNull InstanceContainer container) {
        this.room = room;
        this.container = container;
    }

    public void addEntities(@NotNull Collection<Entity> toAdd, boolean force) {
        entityLock.writeLock().lock();
        try {
            if (force || canAdd0(toAdd.size())) {
                entities.addAll(toAdd);
                noticePickersAboutEntityNumberUpdate();
            } else {
                throw new RoomAreFullException(room.getInstance().getId(), room.getId());
            }
        } finally {
            entityLock.writeLock().unlock();
        }
    }

    public void removeEntities(@NotNull Collection<Entity> toRemove) {
        entityLock.writeLock().lock();
        try {
            entities.removeAll(toRemove);
            noticePickersAboutEntityNumberUpdate();
        } finally {
            entityLock.writeLock().unlock();
        }
    }

    public @NotNull Collection<Entity> removeAllEntities() {
        entityLock.writeLock().lock();
        try {
            var set = new HashSet<>(entities);
            entities.clear();
            noticePickersAboutEntityNumberUpdate();
            return set;
        } finally {
            entityLock.writeLock().unlock();
        }
    }

    public @NotNull Collection<Entity> allEntities() {
        entityLock.readLock().lock();
        try {
            return new ArrayList<>(entities);
        } finally {
            entityLock.readLock().unlock();
        }
    }

    public int countEntities() {
        return entities.size();
    }

    private boolean canAdd0(int entities) {
        return room.getSlots() == -1 || entities + countEntities() <= room.getSlots();
    }

    public boolean canAdd(int entities) {
        try {
            return canAdd0(entities);
        } finally {
            entityLock.readLock().unlock();
        }
    }

    public boolean canBePicked(int entities) {
        entityLock.readLock().lock();
        try {
            return !room.isLocked() && canAdd0(entities);
        } finally {
            entityLock.readLock().unlock();
        }
    }

    private void noticePickersAboutEntityNumberUpdate() {
        if (container.getPicker() instanceof LeastPickedPicker picker) {
            picker.updateEntitiesAmount(room, countEntities());
        }
    }
}
