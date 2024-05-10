package ru.dragonestia.picker.model.room;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.dragonestia.picker.api.model.room.IRoom;
import ru.dragonestia.picker.api.model.room.ResponseRoom;
import ru.dragonestia.picker.api.model.room.RoomDetails;
import ru.dragonestia.picker.api.model.room.ShortResponseRoom;
import ru.dragonestia.picker.api.repository.type.RoomIdentifier;
import ru.dragonestia.picker.model.instance.Instance;

import java.util.Objects;

public class Room implements IRoom {

    private final String identifier;
    private final String instanceIdentifier;
    private final int slots;
    private final String payload;
    private final boolean persist;
    private boolean locked = false;

    public Room(@NotNull RoomIdentifier identifier, @NotNull Instance instance, int slots, @NotNull String payload, boolean persist) {
        this.identifier = identifier.getValue();
        this.instanceIdentifier = instance.getIdentifier();
        this.slots = slots;
        this.payload = payload;
        this.persist = persist;
    }

    @Override
    public @NotNull String getIdentifier() {
        return identifier;
    }

    @Override
    public @NotNull String getInstanceIdentifier() {
        return instanceIdentifier;
    }

    @Override
    public int getMaxSlots() {
        return slots;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean value) {
        locked = value;
    }

    @Override
    public @NotNull Boolean isPersist() {
        return persist;
    }

    @Override
    public @NotNull String getPayload() {
        return payload;
    }

    @Override
    public @Nullable String getDetail(@NotNull RoomDetails detail) {
        throw new UnsupportedOperationException();
    }

    public boolean isAvailable(int usedSlots, int requiredSlots) {
        if (locked) return false;
        if (hasUnlimitedSlots()) return true;
        return slots >= usedSlots + requiredSlots;
    }

    public @NotNull ResponseRoom toResponseObject() {
        return new ResponseRoom(identifier, instanceIdentifier, slots, locked, payload);
    }

    public @NotNull ShortResponseRoom toShortResponseObject() {
        return new ShortResponseRoom(identifier, instanceIdentifier, slots, locked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, instanceIdentifier);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof Room other) {
            return identifier.equals(other.identifier) && instanceIdentifier.equals(other.instanceIdentifier);
        }
        return false;
    }
}
