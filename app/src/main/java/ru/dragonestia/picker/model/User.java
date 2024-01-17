package ru.dragonestia.picker.model;

import lombok.NonNull;
import ru.dragonestia.picker.api.repository.response.type.RUser;

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

    public RUser toResponseObject() {
        return new RUser(id);
    }
}
