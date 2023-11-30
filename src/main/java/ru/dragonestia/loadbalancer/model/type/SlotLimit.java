package ru.dragonestia.loadbalancer.model.type;

import lombok.Getter;

import java.beans.Transient;

@Getter
public class SlotLimit {

    private final static int UNLIMITED_VALUE = -1;

    private final int slots;

    private SlotLimit(int slots) {
        this.slots = slots;
    }

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
