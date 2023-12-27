package ru.dragonestia.picker.service;

import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.User;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    void create(Room room);

    void remove(Room room);

    Optional<Room> find(Node node, String roomId);

    List<Room> all(Node node);

    default int countAvailable(Node node) {
        return countAvailable(node, 1);
    }

    int countAvailable(Node node, int requiredSlots);

    Room pickAvailable(Node node, List<User> users);
}
