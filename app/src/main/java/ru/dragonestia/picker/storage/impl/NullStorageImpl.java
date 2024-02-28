package ru.dragonestia.picker.storage.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.storage.NodeAndRoomStorage;

@Profile("test")
@Component
public class NullStorageImpl implements NodeAndRoomStorage {

    @Override
    public void loadAll() {}

    @Override
    public void saveNode(Node node) {}

    @Override
    public void removeNode(Node node) {}

    @Override
    public void saveRoom(Room room) {}

    @Override
    public void removeRoom(Room room) {}
}
