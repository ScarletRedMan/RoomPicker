package ru.dragonestia.loadbalancer.web.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.dragonestia.loadbalancer.web.model.type.SlotLimit;

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

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof Bucket other) {
            return identifier.equals(other.identifier);
        }
        return false;
    }
}
