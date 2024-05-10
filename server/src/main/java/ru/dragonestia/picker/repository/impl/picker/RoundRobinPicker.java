package ru.dragonestia.picker.repository.impl.picker;

import lombok.RequiredArgsConstructor;
import ru.dragonestia.picker.api.exception.NoRoomsAvailableException;
import ru.dragonestia.picker.api.model.node.PickingMethod;
import ru.dragonestia.picker.model.entity.Entity;
import ru.dragonestia.picker.repository.impl.collection.QueuedLinkedList;
import ru.dragonestia.picker.repository.impl.container.InstanceContainer;
import ru.dragonestia.picker.repository.impl.container.RoomContainer;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class RoundRobinPicker implements RoomPicker {

    private final InstanceContainer container;
    private final AtomicInteger addition = new AtomicInteger(0);
    private final QueuedLinkedList<RoomWrapper> list = new QueuedLinkedList<>(wrapper -> wrapper.canAddUnits(addition.get()));

    @Override
    public void add(RoomContainer container) {
        synchronized (list) {
            list.add(new RoomWrapper(container));
        }
    }

    @Override
    public void remove(RoomContainer container) {
        synchronized (list) {
            list.removeById(container.getRoom().getIdentifier());
        }
    }

    @Override
    public RoomContainer pick(Collection<Entity> entities) {
        int amount = entities.size();
        RoomWrapper wrapper;

        synchronized (list) {
            try {
                addition.set(amount);
                wrapper = list.pick();
            } catch (RuntimeException ex) {
                throw new NoRoomsAvailableException(container.getInstance().getIdentifier());
            }
        }

        return wrapper.getItem();
    }

    @Override
    public PickingMethod getPickingMode() {
        return PickingMethod.ROUND_ROBIN;
    }
}
