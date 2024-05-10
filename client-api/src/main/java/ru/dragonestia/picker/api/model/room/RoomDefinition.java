package ru.dragonestia.picker.api.model.room;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.dragonestia.picker.api.repository.type.NodeIdentifier;
import ru.dragonestia.picker.api.repository.type.RoomIdentifier;

import java.security.InvalidParameterException;

public class RoomDefinition implements IRoom {

    private final String id;
    private final String nodeId;
    private int slots = 5;
    private boolean locked = false;
    private boolean persist = false;
    private String payload = "";

    public RoomDefinition(@NotNull NodeIdentifier nodeIdentifier, @NotNull RoomIdentifier roomIdentifier) {
        nodeId = nodeIdentifier.getValue();
        id = roomIdentifier.getValue();
    }

    @Override
    public @NotNull String getIdentifier() {
        return id;
    }

    @Override
    public @NotNull String getInstanceIdentifier() {
        return nodeId;
    }

    @Override
    public int getMaxSlots() {
        return slots;
    }

    @Contract("_ -> this")
    public @NotNull RoomDefinition setMaxSlots(int value) {
        if (value == 0 || value < -1) {
            throw new InvalidParameterException("Slots cannot be negative");
        }

        slots = value;
        return this;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    @Contract("_ -> this")
    public @NotNull RoomDefinition setLocked(boolean value) {
        locked = value;
        return this;
    }

    @Override
    public @NotNull Boolean isPersist() {
        return persist;
    }

    @Contract("_ -> this")
    public @NotNull RoomDefinition setPersist(boolean value) {
        persist = value;
        return this;
    }

    @Override
    public @Nullable String getPayload() {
        return payload;
    }

    @Contract("_ -> this")
    public @NotNull RoomDefinition setPayload(@NotNull String value) {
        payload = value;
        return this;
    }

    @Override
    public @Nullable String getDetail(@NotNull RoomDetails detail) {
        throw new UnsupportedOperationException();
    }
}
