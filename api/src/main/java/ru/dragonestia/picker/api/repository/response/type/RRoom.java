package ru.dragonestia.picker.api.repository.response.type;

import ru.dragonestia.picker.api.repository.details.RoomDetails;

import java.beans.Transient;
import java.util.HashMap;
import java.util.Map;

public class RRoom {

    public final static int INFINITE_SLOTS = -1;

    private String id;
    private String nodeId;
    private int slots;
    private String payload;
    private boolean locked = false;
    private Map<RoomDetails, String> details;

    private RRoom() {}

    public RRoom(String id, String nodeId, int slots, String payload) {
        this.id = id;
        this.nodeId = nodeId;
        this.slots = slots;
        this.payload = payload;
        this.details = new HashMap<>();
    }

    public RRoom(String id, RNode node, int limit, String payload) {
        this(id, node.getId(), limit, payload);
    }

    public String getId() {
        return id;
    }

    public String getNodeId() {
        return nodeId;
    }

    public int getSlots() {
        return slots;
    }

    public String getPayload() {
        return payload;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean value) {
        locked = value;
    }

    @Transient
    public boolean isUnlimited() {
        return slots == INFINITE_SLOTS;
    }

    public void putDetail(RoomDetails detail, String value) {
        details.put(detail, value);
    }

    public String getDetail(RoomDetails detail) {
        return details.get(detail);
    }

    public Map<RoomDetails, String> getDetails() {
        return details;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof RRoom other) {
            return id.equals(other.id);
        }
        return false;
    }

    public record Short(String id, String nodeId, int slots, boolean locked, Map<RoomDetails, String> details) {}
}
