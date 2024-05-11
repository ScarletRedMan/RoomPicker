package ru.dragonestia.picker.storage;

import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.model.instance.InstanceId;
import ru.dragonestia.picker.model.room.Room;

public interface InstanceAndRoomStorage {

    void loadAll();

    void saveInstance(Instance instance);

    void removeInstance(InstanceId id);

    void saveRoom(Room room);

    void removeRoom(Room room);
}
