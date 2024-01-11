package ru.dragonestia.picker.service.impl.picker;

import ru.dragonestia.picker.model.Room;

import java.util.function.Supplier;

public class RoomWrapper implements ItemWrapper<Room> {

    private final Room room;
    private final Supplier<Integer> userCountSupplier;

    public RoomWrapper(Room room, Supplier<Integer> userCountSupplier) {
        this.room = room;
        this.userCountSupplier = userCountSupplier;
    }

    @Override
    public String getIdentifier() {
        return room.getId();
    }

    @Override
    public int countUnits() {
        return userCountSupplier.get();
    }

    @Override
    public int maxUnits() {
        return room.getSlots().getSlots();
    }

    @Override
    public Room getItem() {
        return room;
    }

    @Override
    public boolean canAddUnits(int amount) {
        return ItemWrapper.super.canAddUnits(amount) && !room.isLocked();
    }
}
