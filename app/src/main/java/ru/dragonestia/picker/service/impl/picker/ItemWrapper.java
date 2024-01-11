package ru.dragonestia.picker.service.impl.picker;

import ru.dragonestia.picker.model.type.SlotLimit;

public interface ItemWrapper<ITEM> {

    String getIdentifier();

    int countUnits();

    int maxUnits();

    default boolean isFull() {
        return maxUnits() != SlotLimit.UNLIMITED_VALUE && countUnits() >= maxUnits();
    }

    default boolean isEmpty() {
        return countUnits() == 0;
    }

    default boolean canAddUnits(int amount) {
        return maxUnits() == SlotLimit.UNLIMITED_VALUE || countUnits() + amount < maxUnits();
    }

    ITEM getItem();
}
