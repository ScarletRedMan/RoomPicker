package ru.dragonestia.picker.service.impl.picker;

import java.util.Collection;

public interface Picker<ITEM, UNIT> {

    void add(ITEM item);

    void remove(ITEM item);

    ITEM pick(Collection<UNIT> units);
}
