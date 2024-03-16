package ru.dragonestia.picker.repository.impl.picker;

import ru.dragonestia.picker.api.model.node.PickingMethod;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.UserRepository;
import ru.dragonestia.picker.repository.impl.collection.DynamicSortedMap;
import ru.dragonestia.picker.repository.impl.container.RoomContainer;

import java.util.Collection;

public class LeastPickedPicker implements RoomPicker {

    private final DynamicSortedMap<RoomWrapper> map = new DynamicSortedMap<>();

    @Override
    public void add(RoomContainer container) {
        synchronized (map) {
            map.put(new RoomWrapper(container));
        }
    }

    @Override
    public void remove(RoomContainer container) {
        synchronized (map) {
            map.removeById(container.getRoom().getIdentifier());
        }
    }

    @Override
    public RoomContainer pick(Collection<User> users) {
        RoomWrapper wrapper;

        synchronized (map) {
            try {
                wrapper = map.getMinimum();

                if (!wrapper.canAddUnits(users.size())) throw new RuntimeException();
            } catch (RuntimeException ex) {
                throw new RuntimeException("There are no rooms available");
            }
        }

        return wrapper.getItem();
    }

    public void updateUsersAmount(Room room, int users) {
        synchronized (map) {
            map.updateItem(room.getIdentifier(), prevValue -> users);
        }
    }

    @Override
    public PickingMethod getPickingMode() {
        return PickingMethod.LEAST_PICKED;
    }
}
