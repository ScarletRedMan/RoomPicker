package ru.dragonestia.picker.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.dragonestia.picker.api.repository.response.type.RRoom;
import ru.dragonestia.picker.model.type.SlotLimit;

import java.util.HashMap;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Room {

    private final String id;
    private final String nodeId;
    private final SlotLimit slots;
    private final String payload;
    private boolean locked = false;

    public static Room create(String roomId, Node node, SlotLimit limit, String payload) {
        return new Room(roomId, node.id(), limit, payload);
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
        return id.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof Room other) {
            return id.equals(other.id);
        }
        return false;
    }

    public RRoom toResponseObject() {
        var result = new RRoom(id, nodeId, slots.getSlots(), payload);
        result.setLocked(locked);
        return result;
    }

    public RRoom.Short toShortResponseObject() {
        return new RRoom.Short(id, nodeId, slots.getSlots(), locked, new HashMap<>());
    }
}
