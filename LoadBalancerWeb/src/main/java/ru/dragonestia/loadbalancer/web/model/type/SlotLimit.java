package ru.dragonestia.loadbalancer.web.model.type;

import lombok.Getter;

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

    public boolean isUnlimited() {
        return slots == UNLIMITED_VALUE;
    }
}
