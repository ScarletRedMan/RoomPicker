package ru.dragonestia.picker.repository.impl.picker;

import ru.dragonestia.picker.api.model.node.PickingMethod;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.impl.collection.QueuedLinkedList;
import ru.dragonestia.picker.repository.impl.container.RoomContainer;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinPicker implements RoomPicker {

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
    public RoomContainer pick(Collection<User> users) {
        int amount = users.size();
        RoomWrapper wrapper;

        synchronized (list) {
            try {
                addition.set(amount);
                wrapper = list.pick();
            } catch (RuntimeException ex) {
                throw new RuntimeException("There are no rooms available");
            }
        }

        return wrapper.getItem();
    }

    @Override
    public PickingMethod getPickingMode() {
        return PickingMethod.ROUND_ROBIN;
    }
}
