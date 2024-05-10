package ru.dragonestia.picker.repository.impl.picker;

import ru.dragonestia.picker.model.room.type.SlotLimit;

public interface ItemWrapper<ITEM> {

    String getId();

    int countUnits();

    int maxUnits();

    default boolean isFull() {
        return maxUnits() != SlotLimit.UNLIMITED_VALUE && countUnits() >= maxUnits();
    }

    default boolean isEmpty() {
        return countUnits() == 0;
    }

    default boolean canAddUnits(int amount) {
        return maxUnits() == SlotLimit.UNLIMITED_VALUE || countUnits() + amount <= maxUnits();
    }

    ITEM getItem();
}
