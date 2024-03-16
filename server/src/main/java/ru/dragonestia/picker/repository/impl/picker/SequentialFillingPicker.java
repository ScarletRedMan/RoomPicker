package ru.dragonestia.picker.repository.impl.picker;

import ru.dragonestia.picker.api.model.node.PickingMethod;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.impl.container.RoomContainer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class SequentialFillingPicker implements RoomPicker {

    private final Map<String, RoomWrapper> wrappers = new LinkedHashMap<>();

    @Override
    public void add(RoomContainer container) {
        synchronized (wrappers) {
            wrappers.put(container.getRoom().getIdentifier(), new RoomWrapper(container));
        }
    }

    @Override
    public void remove(RoomContainer container) {
        synchronized (wrappers) {
            wrappers.remove(container.getRoom().getIdentifier());
        }
    }

    @Override
    public RoomContainer pick(Collection<User> users) {
        int amount = users.size();

        synchronized (wrappers) {
            for (var wrapper: wrappers.values()) {
                if (!wrapper.canAddUnits(amount)) continue;

                return wrapper.getItem();
            }
        }

        throw new RuntimeException("There are no rooms available");
    }

    @Override
    public PickingMethod getPickingMode() {
        return PickingMethod.SEQUENTIAL_FILLING;
    }
}
