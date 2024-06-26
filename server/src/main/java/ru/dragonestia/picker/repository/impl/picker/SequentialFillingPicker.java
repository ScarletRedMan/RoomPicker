package ru.dragonestia.picker.repository.impl.picker;

import lombok.RequiredArgsConstructor;
import ru.dragonestia.picker.exception.NoRoomsAvailableException;
import ru.dragonestia.picker.model.entity.Entity;
import ru.dragonestia.picker.model.instance.type.PickingMethod;
import ru.dragonestia.picker.repository.impl.container.InstanceContainer;
import ru.dragonestia.picker.repository.impl.container.RoomContainer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public class SequentialFillingPicker implements RoomPicker {

    private final InstanceContainer container;
    private final Map<String, RoomWrapper> wrappers = new LinkedHashMap<>();

    @Override
    public void add(RoomContainer container) {
        synchronized (wrappers) {
            wrappers.put(container.getRoom().getId().getValue(), new RoomWrapper(container));
        }
    }

    @Override
    public void remove(RoomContainer container) {
        synchronized (wrappers) {
            wrappers.remove(container.getRoom().getId());
        }
    }

    @Override
    public RoomContainer pick(Collection<Entity> entities) {
        int amount = entities.size();

        synchronized (wrappers) {
            for (var wrapper: wrappers.values()) {
                if (!wrapper.canAddUnits(amount)) continue;

                return wrapper.getItem();
            }
        }

        throw new NoRoomsAvailableException(container.getInstance().getId());
    }

    @Override
    public PickingMethod getPickingMode() {
        return PickingMethod.SEQUENTIAL_FILLING;
    }
}
