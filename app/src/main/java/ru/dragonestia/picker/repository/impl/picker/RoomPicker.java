package ru.dragonestia.picker.repository.impl.picker;

import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.model.type.PickingMode;

public interface RoomPicker extends Picker<Room, User> {

    PickingMode getPickingMode();
}
