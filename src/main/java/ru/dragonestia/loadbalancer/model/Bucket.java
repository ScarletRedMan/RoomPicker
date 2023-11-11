package ru.dragonestia.loadbalancer.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.dragonestia.loadbalancer.model.type.SlotLimit;

@Getter
@RequiredArgsConstructor
public class Bucket {

    private final String identifier;
    private final String nodeIdentifier;
    private final SlotLimit slots;
    private final String payload;
    private boolean locked = false;

    public void setLocked(boolean value) {
        locked = value;
    }

    public boolean isAvailable(int usedSlots, int requiredSlots) {
        if (locked) return false;
        if (slots.isUnlimited()) return true;
        return slots.getSlots() >= usedSlots + requiredSlots;
    }
}
