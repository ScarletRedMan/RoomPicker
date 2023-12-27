package ru.dragonestia.picker.model;

import lombok.NonNull;
import ru.dragonestia.picker.model.type.PickingMode;

public record Node(@NonNull String id, @NonNull PickingMode mode) {

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
