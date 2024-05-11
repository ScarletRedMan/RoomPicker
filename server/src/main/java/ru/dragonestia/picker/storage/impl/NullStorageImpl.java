package ru.dragonestia.picker.storage.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.model.instance.InstanceId;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.storage.InstanceAndRoomStorage;

@Profile("test")
@Component
public class NullStorageImpl implements InstanceAndRoomStorage {

    @Override
    public void loadAll() {}

    @Override
    public void saveInstance(Instance instance) {}

    @Override
    public void removeInstance(InstanceId id) {}

    @Override
    public void saveRoom(Room room) {}

    @Override
    public void removeRoom(Room room) {}
}
