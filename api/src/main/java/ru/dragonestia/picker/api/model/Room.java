package ru.dragonestia.picker.api.model;

import java.beans.Transient;

public class Room {

    public final static int INFINITE_SLOTS = -1;

    private String id;
    private String nodeId;
    private int slots;
    private String payload;
    private boolean locked = false;

    private Room() {}

    public Room(String id, String nodeId, int slots, String payload) {
        this.id = id;
        this.nodeId = nodeId;
        this.slots = slots;
        this.payload = payload;
    }

    public Room(String id, Node node, int limit, String payload) {
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

    public record Short(String id, int slots, boolean locked) {}
}
