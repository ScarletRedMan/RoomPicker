package ru.dragonestia.picker.repository.impl.container;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.exception.RoomAreFullException;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.impl.picker.LeastPickedPicker;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RoomContainer {

    @Getter
    private final Room room;
    private final NodeContainer container;

    private final ReadWriteLock usersLock = new ReentrantReadWriteLock(true);
    private final Set<User> users = new HashSet<>();

    public RoomContainer(@NotNull Room room, @NotNull NodeContainer container) {
        this.room = room;
        this.container = container;
    }

    public void addUsers(@NotNull Collection<User> toAdd, boolean force) {
        usersLock.writeLock().lock();
        try {
            if (force || canAdd0(toAdd.size())) {
                users.addAll(toAdd);
                noticePickersAboutUserNumberUpdate();
            } else {
                throw new RoomAreFullException(room.getNodeIdentifier(), room.getIdentifier());
            }
        } finally {
            usersLock.writeLock().unlock();
        }
    }

    public void removeUsers(@NotNull Collection<User> toRemove) {
        usersLock.writeLock().lock();
        try {
            users.removeAll(toRemove);
            noticePickersAboutUserNumberUpdate();
        } finally {
            usersLock.writeLock().unlock();
        }
    }

    public @NotNull Collection<User> removeAllUsers() {
        usersLock.writeLock().lock();
        try {
            var set = new HashSet<>(users);
            users.clear();
            noticePickersAboutUserNumberUpdate();
            return set;
        } finally {
            usersLock.writeLock().unlock();
        }
    }

    public @NotNull Collection<User> allUsers() {
        usersLock.readLock().lock();
        try {
            return Collections.unmodifiableSet(users);
        } finally {
            usersLock.readLock().unlock();
        }
    }

    public int countUsers() {
        return users.size();
    }

    private boolean canAdd0(int users) {
        return room.hasUnlimitedSlots() || users + countUsers() <= room.getMaxSlots();
    }

    public boolean canAdd(int users) {
        try {
            return canAdd0(users);
        } finally {
            usersLock.readLock().unlock();
        }
    }

    public boolean canBePicked(int users) {
        usersLock.readLock().lock();
        try {
            return !room.isLocked() && canAdd0(users);
        } finally {
            usersLock.readLock().unlock();
        }
    }

    private void noticePickersAboutUserNumberUpdate() {
        if (container.getPicker() instanceof LeastPickedPicker picker) {
            picker.updateUsersAmount(room, countUsers());
        }
    }
}
