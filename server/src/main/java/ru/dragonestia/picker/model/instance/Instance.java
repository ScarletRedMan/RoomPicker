package ru.dragonestia.picker.model.instance;

import lombok.Getter;
import ru.dragonestia.picker.model.instance.type.PickingMethod;

@Getter
public class Instance {

    private final InstanceId id;
    private final PickingMethod pickingMethod;
    private final boolean persist;

    public Instance(InstanceId id, PickingMethod pickingMethod, boolean persist) {
        this.id = id;
        this.pickingMethod = pickingMethod;
        this.persist = persist;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof Instance other) {
            return id.equals(other.id);
        }
        return false;
    }

    @Override
    public String toString() {
        return "{Instance id='%s'}".formatted(id);
    }
}
