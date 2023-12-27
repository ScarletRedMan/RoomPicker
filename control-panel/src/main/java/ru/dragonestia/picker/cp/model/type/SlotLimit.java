package ru.dragonestia.picker.cp.model.type;

import java.beans.Transient;

public record SlotLimit(int slots) {

    private final static int UNLIMITED_VALUE = -1;

    public static SlotLimit unlimited() {
        return new SlotLimit(UNLIMITED_VALUE);
    }

    public static SlotLimit of(int slots) {
        return new SlotLimit(slots);
    }

    @Transient
    public boolean isUnlimited() {
        return slots == UNLIMITED_VALUE;
    }
}
