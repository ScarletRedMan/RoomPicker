package ru.dragonestia.picker.api.model.room;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IRoom {

    int UNLIMITED_SLOTS = -1;

    @NotNull String getIdentifier();

    @NotNull String getNodeIdentifier();

    int getMaxSlots();

    default boolean hasUnlimitedSlots() {
        return getMaxSlots() == UNLIMITED_SLOTS;
    }

    boolean isLocked();

    @Nullable Boolean isPersist();

    @Nullable String getPayload();

    @Nullable String getDetail(@NotNull RoomDetails detail);
}
