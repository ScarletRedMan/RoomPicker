package ru.dragonestia.picker.repository.impl.picker;

import ru.dragonestia.picker.model.entity.Entity;
import ru.dragonestia.picker.model.instance.type.PickingMethod;
import ru.dragonestia.picker.repository.impl.container.RoomContainer;

public interface RoomPicker extends Picker<RoomContainer, Entity> {

    PickingMethod getPickingMode();
}
