package ru.dragonestia.picker.cp.model;

import lombok.NonNull;

public record User(@NonNull String id) {

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof User other) {
            return id.equals(other.id);
        }
        return false;
    }
}
