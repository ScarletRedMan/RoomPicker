package ru.dragonestia.loadbalancer.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.dragonestia.loadbalancer.model.type.SlotLimit;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Bucket {

    private final String identifier;
    private final String nodeIdentifier;
    private final SlotLimit slots;
    private final String payload;
    private boolean locked = false;

    public static Bucket create(String identifier, Node node, SlotLimit limit, String payload) {
        return new Bucket(identifier, node.identifier(), limit, payload);
    }

    public void setLocked(boolean value) {
        locked = value;
    }

    public boolean isAvailable(int usedSlots, int requiredSlots) {
        if (locked) return false;
        if (slots.isUnlimited()) return true;
        return slots.getSlots() >= usedSlots + requiredSlots;
    }
}
