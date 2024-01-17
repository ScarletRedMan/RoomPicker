package ru.dragonestia.picker.service;

import ru.dragonestia.picker.api.exception.InvalidRoomIdentifierException;
import ru.dragonestia.picker.api.exception.RoomAlreadyExistException;
import ru.dragonestia.picker.api.repository.details.RoomDetails;
import ru.dragonestia.picker.api.repository.response.type.RRoom;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoomService {

    void create(Room room) throws InvalidRoomIdentifierException, RoomAlreadyExistException;

    void remove(Room room);

    Optional<Room> find(Node node, String roomId);

    List<Room> all(Node node);

    List<RRoom.Short> getAllRoomsWithDetailsResponse(Node node, Set<RoomDetails> details);

    default int countAvailable(Node node) {
        return countAvailable(node, 1);
    }

    int countAvailable(Node node, int requiredSlots);

    Room pickAvailable(Node node, List<User> users);
}
