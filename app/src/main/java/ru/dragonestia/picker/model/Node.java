package ru.dragonestia.picker.model;

import lombok.NonNull;
import ru.dragonestia.picker.model.type.LoadBalancingMethod;

public record Node(@NonNull String identifier, @NonNull LoadBalancingMethod method) {

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof Node other) {
            return identifier.equals(other.identifier);
        }
        return false;
    }
}
