package ru.dragonestia.picker.repository.impl.picker;

import lombok.RequiredArgsConstructor;
import ru.dragonestia.picker.exception.NoRoomsAvailableException;
import ru.dragonestia.picker.model.instance.type.PickingMethod;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.model.entity.Entity;
import ru.dragonestia.picker.repository.impl.collection.DynamicSortedMap;
import ru.dragonestia.picker.repository.impl.container.InstanceContainer;
import ru.dragonestia.picker.repository.impl.container.RoomContainer;

import java.util.Collection;

@RequiredArgsConstructor
public class LeastPickedPicker implements RoomPicker {

    private final InstanceContainer container;
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
            map.removeById(container.getRoom().getId().getValue());
        }
    }

    @Override
    public RoomContainer pick(Collection<Entity> entities) {
        RoomWrapper wrapper;

        synchronized (map) {
            try {
                wrapper = map.getMinimum();

                if (!wrapper.canAddUnits(entities.size())) throw new RuntimeException();
            } catch (RuntimeException ex) {
                throw new NoRoomsAvailableException(container.getInstance().getId());
            }
        }

        return wrapper.getItem();
    }

    public void updateEntitiesAmount(Room room, int users) {
        synchronized (map) {
            map.updateItem(room.getId().getValue(), prevValue -> users);
        }
    }

    @Override
    public PickingMethod getPickingMode() {
        return PickingMethod.LEAST_PICKED;
    }
}
