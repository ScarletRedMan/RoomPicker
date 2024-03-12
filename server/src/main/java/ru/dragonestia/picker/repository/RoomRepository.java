package ru.dragonestia.picker.repository;

import ru.dragonestia.picker.api.exception.RoomAlreadyExistException;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RoomRepository {

    void create(Room room) throws RoomAlreadyExistException;

    void remove(Room room);

    Optional<Room> find(Node node, String identifier);

    List<Room> all(Node node);

    Optional<Room> pickFree(Node node, Collection<User> users);

    void onCreateNode(Node node);

    List<Room> onRemoveNode(Node node);
}
