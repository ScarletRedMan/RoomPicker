package ru.dragonestia.picker.api.repository.type;

import org.jetbrains.annotations.NotNull;

public record RoomPath(@NotNull NodeIdentifier nodeIdentifier, @NotNull RoomIdentifier roomIdentifier) {

    public @NotNull String getNodeId() {
        return nodeIdentifier.getValue();
    }

    public @NotNull String getRoomId() {
        return roomIdentifier.getValue();
    }

    @Override
    public String toString() {
        return nodeIdentifier.getValue() + "/" + roomIdentifier.getValue();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof RoomPath other) {
            return toString().equals(other.toString());
        }
        return false;
    }
}
