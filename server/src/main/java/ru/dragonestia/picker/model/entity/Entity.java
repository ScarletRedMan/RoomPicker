package ru.dragonestia.picker.model.entity;

import lombok.Getter;

@Getter
public class Entity {

    private final EntityId id;

    public Entity(EntityId id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id.getValue();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof Entity other) {
            return id.equals(other.id);
        }
        return false;
    }
}
