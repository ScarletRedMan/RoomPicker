package ru.dragonestia.picker.cp.model;

import lombok.NonNull;
import ru.dragonestia.picker.cp.model.type.PickingMode;

import java.io.Serializable;

public record Node(@NonNull String id, @NonNull PickingMode mode) implements Serializable {

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
