package ru.dragonestia.picker.cp.model;

import lombok.NonNull;
import ru.dragonestia.picker.cp.model.type.LoadBalancingMethod;

import java.io.Serializable;

public record Node(@NonNull String identifier, @NonNull LoadBalancingMethod method) implements Serializable {

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
