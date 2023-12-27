package ru.dragonestia.picker.cp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import ru.dragonestia.picker.cp.model.type.SlotLimit;

import java.net.URI;

@Getter
public class Room {

    private final String id;
    private final String nodeId;
    private final SlotLimit slots;
    private final String payload;
    private boolean locked = false;

    @JsonCreator
    private Room(@JsonProperty("id") String id,
                 @JsonProperty("nodeIdentifier") String nodeId,
                 @JsonProperty("slots") SlotLimit slots,
                 @JsonProperty("payload") String payload,
                 @JsonProperty("locked") boolean locked) {

        this.id = id;
        this.nodeId = nodeId;
        this.slots = slots;
        this.payload = payload;
        this.locked = locked;
    }

    public static Room create(String roomId, Node node, SlotLimit limit, String payload) {
        return new Room(roomId, node.id(), limit, payload, false);
    }

    public void setLocked(boolean value) {
        locked = value;
    }

    public boolean isAvailable(int usedSlots, int requiredSlots) {
        if (locked) return false;
        if (slots.isUnlimited()) return true;
        return slots.slots() >= usedSlots + requiredSlots;
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

    public URI createApiURI() {
        return URI.create("/nodes/" + nodeId + "/rooms/" + id);
    }

    public String getUsingPercentage(int used) {
        if (getSlots().isUnlimited()) return "0%";
        double percent = used / (double) getSlots().slots() * 100;
        return ((int) percent) + "%";
    }
}
