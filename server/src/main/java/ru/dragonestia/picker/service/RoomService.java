package ru.dragonestia.picker.service;

import ru.dragonestia.picker.api.exception.InvalidRoomIdentifierException;
import ru.dragonestia.picker.api.exception.RoomAlreadyExistException;
import ru.dragonestia.picker.api.model.room.RoomDetails;
import ru.dragonestia.picker.api.model.room.ShortResponseRoom;
import ru.dragonestia.picker.api.repository.response.PickedRoomResponse;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoomService {

    void create(Room room) throws InvalidRoomIdentifierException, RoomAlreadyExistException;

    void remove(Room room);

    Optional<Room> find(Node node, String roomId);

    Collection<Room> all(Node node);

    List<ShortResponseRoom> getAllRoomsWithDetailsResponse(Node node, Set<RoomDetails> details);

    PickedRoomResponse pickAvailable(Node node, Set<User> users);

    void updateState(Room room);
}
