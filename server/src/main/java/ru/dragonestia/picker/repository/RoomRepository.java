package ru.dragonestia.picker.repository;

import ru.dragonestia.picker.api.exception.NoRoomsAvailableException;
import ru.dragonestia.picker.api.exception.RoomAlreadyExistException;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface RoomRepository {

    void create(Room room) throws RoomAlreadyExistException;

    void remove(Room room);

    Optional<Room> find(Node node, String identifier);

    Collection<Room> all(Node node);

    Room pick(Node node, Set<User> users) throws NoRoomsAvailableException;
}
