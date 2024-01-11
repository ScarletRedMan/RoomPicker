package ru.dragonestia.picker.repository.impl.picker;

import java.util.Collection;

public interface Picker<ITEM, UNIT> {

    void add(ITEM item);

    void remove(ITEM item);

    ITEM pick(Collection<UNIT> units);
}
