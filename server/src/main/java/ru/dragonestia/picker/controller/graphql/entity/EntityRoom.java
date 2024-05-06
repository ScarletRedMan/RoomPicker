package ru.dragonestia.picker.controller.graphql.entity;

import jakarta.validation.constraints.NotNull;
import ru.dragonestia.picker.model.Room;

public class EntityRoom {

    private final Room room;

    public EntityRoom(@NotNull Room room) {
        this.room = room;
    }

    public @NotNull String getId() {
        return room.getIdentifier();
    }

    public @NotNull String getNodeId() {
        return room.getNodeIdentifier();
    }

    public int getSlots() {
        return room.getMaxSlots();
    }

    public @NotNull String getPayload() {
        return room.getPayload();
    }

    public boolean isLocked() {
        return room.isLocked();
    }
}
