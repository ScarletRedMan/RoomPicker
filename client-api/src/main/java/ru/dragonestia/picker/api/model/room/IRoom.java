package ru.dragonestia.picker.api.model.room;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.dragonestia.picker.api.repository.type.NodeIdentifier;
import ru.dragonestia.picker.api.repository.type.RoomIdentifier;
import ru.dragonestia.picker.api.repository.type.RoomPath;

import java.beans.Transient;

public interface IRoom {

    int UNLIMITED_SLOTS = -1;

    @NotNull String getIdentifier();

    @Transient
    default @NotNull RoomIdentifier getIdentifierObject() {
        return RoomIdentifier.of(getIdentifier());
    }

    @NotNull String getNodeIdentifier();

    @Transient
    default @NotNull NodeIdentifier getNodeIdentifierObject() {
        return NodeIdentifier.of(getNodeIdentifier());
    }

    default @NotNull RoomPath getPath() {
        return new RoomPath(NodeIdentifier.of(getNodeIdentifier()), RoomIdentifier.of(getIdentifier()));
    }

    int getMaxSlots();

    default boolean hasUnlimitedSlots() {
        return getMaxSlots() == UNLIMITED_SLOTS;
    }

    boolean isLocked();

    @Transient
    @Nullable Boolean isPersist();

    @Nullable String getPayload();

    @Nullable String getDetail(@NotNull RoomDetails detail);
}
