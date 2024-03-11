package ru.dragonestia.picker.model;

import lombok.NonNull;
import ru.dragonestia.picker.api.model.node.PickingMethod;
import ru.dragonestia.picker.api.model.node.ResponseNode;

public record Node(@NonNull String id, @NonNull PickingMethod method, boolean persist) {

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

    public ResponseNode toResponseObject() {
        return new ResponseNode(id, method);
    }
}
