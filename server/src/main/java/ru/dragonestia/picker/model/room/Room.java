package ru.dragonestia.picker.model.room;

import lombok.Getter;
import lombok.Setter;
import ru.dragonestia.picker.model.instance.Instance;

import java.util.Objects;

@Getter
public class Room {

    private final RoomId id;
    private final Instance instance;
    private final int slots;
    private final String payload;
    private final boolean persist;
    @Setter private boolean locked = false;

    public Room(RoomId id, Instance instance, int slots, String payload, boolean persist) {
        this.id = id;
        this.instance = instance;
        this.slots = slots;
        this.payload = payload;
        this.persist = persist;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, instance.getId());
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof Room other) {
            return id.equals(other.id) && instance.getId().equals(other.instance.getId());
        }
        return false;
    }
}
