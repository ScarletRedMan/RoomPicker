package ru.dragonestia.picker.cp.model;

import lombok.NonNull;

public record User(@NonNull String identifier) {

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof User other) {
            return identifier.equals(other.identifier);
        }
        return false;
    }
}
