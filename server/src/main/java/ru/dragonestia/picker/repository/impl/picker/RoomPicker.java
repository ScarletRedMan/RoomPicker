package ru.dragonestia.picker.repository.impl.picker;

import ru.dragonestia.picker.api.model.node.PickingMethod;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.impl.container.RoomContainer;

public interface RoomPicker extends Picker<RoomContainer, User> {

    PickingMethod getPickingMode();
}
