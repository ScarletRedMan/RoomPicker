package ru.dragonestia.picker.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.dragonestia.picker.api.model.room.ResponseRoom;
import ru.dragonestia.picker.api.model.room.ShortResponseRoom;
import ru.dragonestia.picker.model.type.SlotLimit;

import java.util.HashMap;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Room {

    private final String id;
    private final String nodeId;
    private final SlotLimit slots;
    private final String payload;
    private final boolean persist;
    private boolean locked = false;

    public static Room create(String roomId, Node node, SlotLimit limit, String payload, boolean persist) {
        return new Room(roomId, node.id(), limit, payload, persist);
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

    public ResponseRoom toResponseObject() {
        return new ResponseRoom(id, nodeId, slots.getSlots(), locked, payload);
    }

    public ShortResponseRoom toShortResponseObject() {
        return new ShortResponseRoom(id, nodeId, slots.getSlots(), locked);
    }
}
