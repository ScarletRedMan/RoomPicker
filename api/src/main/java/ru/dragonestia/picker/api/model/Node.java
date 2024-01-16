package ru.dragonestia.picker.api.model;

import ru.dragonestia.picker.api.model.type.PickingMode;

public class Node {

    private String id;
    private PickingMode mode;

    private Node() {}

    public Node(String id, PickingMode mode) {
        this.id = id;
        this.mode = mode;
    }

    public String getId() {
        return id;
    }

    public PickingMode getMode() {
        return mode;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof Node other) {
            return id.equals(other.id);
        }
        return false;
    }
}
