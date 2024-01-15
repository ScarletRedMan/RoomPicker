package ru.dragonestia.picker.repository.impl.picker;

import ru.dragonestia.picker.api.model.type.PickingMode;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;

public interface RoomPicker extends Picker<Room, User> {

    PickingMode getPickingMode();
}
