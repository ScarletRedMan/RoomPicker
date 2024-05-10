package ru.dragonestia.picker.model.entity;

import lombok.Getter;
import ru.dragonestia.picker.exception.InvalidIdentifierException;

import java.util.Objects;

@Getter
public final class EntityId {

    private final String value;

    private EntityId(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityId entityId = (EntityId) o;
        return Objects.equals(value, entityId.value);
    }

    public static EntityId of(String identifier) throws InvalidIdentifierException {
        if (identifier.matches("^[aA-zZ\\d-.\\s:@_;]{1,64}$")) {
            return new EntityId(identifier);
        }

        throw InvalidIdentifierException.taken(identifier);
    }
}
