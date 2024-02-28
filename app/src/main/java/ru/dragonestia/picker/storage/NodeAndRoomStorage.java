package ru.dragonestia.picker.storage;

import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.Room;

public interface NodeAndRoomStorage {

    void loadAll();

    void saveNode(Node node);

    void removeNode(Node node);

    void saveRoom(Room room);

    void removeRoom(Room room);
}
