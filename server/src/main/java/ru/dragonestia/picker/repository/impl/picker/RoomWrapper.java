package ru.dragonestia.picker.repository.impl.picker;

import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.repository.impl.collection.QueuedLinkedList;
import ru.dragonestia.picker.repository.impl.collection.DynamicSortedMap;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class RoomWrapper implements ItemWrapper<Room>, QueuedLinkedList.Item, DynamicSortedMap.Item {

    private final Room room;
    private final Supplier<Integer> userCountSupplier;
    private Consumer<Integer> setter;

    public RoomWrapper(Room room, Supplier<Integer> userCountSupplier) {
        this.room = room;
        this.userCountSupplier = userCountSupplier;
    }

    @Override
    public String getId() {
        return room.getIdentifier();
    }

    @Override
    public int countUnits() {
        return userCountSupplier.get();
    }

    @Override
    public int maxUnits() {
        return room.getMaxSlots();
    }

    @Override
    public Room getItem() {
        return room;
    }

    @Override
    public boolean canAddUnits(int amount) {
        return ItemWrapper.super.canAddUnits(amount) && !room.isLocked();
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
