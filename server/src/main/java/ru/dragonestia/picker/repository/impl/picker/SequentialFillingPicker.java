package ru.dragonestia.picker.repository.impl.picker;

import lombok.RequiredArgsConstructor;
import ru.dragonestia.picker.api.exception.NoRoomsAvailableException;
import ru.dragonestia.picker.api.model.node.PickingMethod;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.impl.container.NodeContainer;
import ru.dragonestia.picker.repository.impl.container.RoomContainer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public class SequentialFillingPicker implements RoomPicker {

    private final NodeContainer container;
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

        throw new NoRoomsAvailableException(container.getNode().getIdentifier());
    }

    @Override
    public PickingMethod getPickingMode() {
        return PickingMethod.SEQUENTIAL_FILLING;
    }
}
