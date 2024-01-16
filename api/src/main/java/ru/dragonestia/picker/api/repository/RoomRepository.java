package ru.dragonestia.picker.api.repository;

import ru.dragonestia.picker.api.exception.*;
import ru.dragonestia.picker.api.model.Node;
import ru.dragonestia.picker.api.model.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {

    void register(Room room) throws NodeNotFoundException, InvalidRoomIdentifierException, RoomAlreadyExistException;

    void remove(Room room) throws NodeNotFoundException;

    void remove(Node node, Room.Short room) throws NodeNotFoundException;

    List<Room.Short> all(Node node) throws NodeNotFoundException;

    Optional<Room> find(Node node, String roomId) throws NodeNotFoundException;

    void lock(Room room, boolean value) throws NodeNotFoundException, RoomNotFoundException;
}
