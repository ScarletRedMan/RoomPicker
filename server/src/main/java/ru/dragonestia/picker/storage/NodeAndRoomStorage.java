package ru.dragonestia.picker.storage;

import ru.dragonestia.picker.model.node.Node;
import ru.dragonestia.picker.model.room.Room;

public interface NodeAndRoomStorage {

    void loadAll();

    void saveNode(Node node);

    void removeNode(Node node);

    void saveRoom(Room room);

    void removeRoom(Room room);
}
