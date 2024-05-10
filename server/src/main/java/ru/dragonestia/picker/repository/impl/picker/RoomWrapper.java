package ru.dragonestia.picker.repository.impl.picker;

import ru.dragonestia.picker.repository.impl.collection.QueuedLinkedList;
import ru.dragonestia.picker.repository.impl.collection.DynamicSortedMap;
import ru.dragonestia.picker.repository.impl.container.RoomContainer;

import java.util.function.Consumer;

public class RoomWrapper implements ItemWrapper<RoomContainer>, QueuedLinkedList.Item, DynamicSortedMap.Item {

    private final RoomContainer container;
    private Consumer<Integer> setter;

    public RoomWrapper(RoomContainer container) {
        this.container = container;
    }

    @Override
    public String getId() {
        return container.getRoom().getId().getValue();
    }

    @Override
    public int countUnits() {
        return container.countEntities();
    }

    @Override
    public int maxUnits() {
        return container.getRoom().getSlots();
    }

    @Override
    public RoomContainer getItem() {
        return container;
    }

    @Override
    public boolean canAddUnits(int amount) {
        return container.canBePicked(amount);
    }

    @Override
    public int getScore() {
        return countUnits();
    }

    @Override
    public void updateScore(int value) {
        if (setter == null) return;

        setter.accept(value);
    }

    @Override
    public void setOnUpdateScore(Consumer<Integer> setter) {
        this.setter = setter;
    }

    @Override
    public boolean canBeUsed(int units) {
        return canAddUnits(units);
    }
}
