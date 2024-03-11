package ru.dragonestia.picker.api.repository;

import ru.dragonestia.picker.api.exception.*;
import ru.dragonestia.picker.api.model.node.ResponseNode;
import ru.dragonestia.picker.api.model.room.RoomDetails;
import ru.dragonestia.picker.api.repository.response.type.RRoom;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoomRepository {

    Set<RoomDetails> ALL_DETAILS = Set.of(RoomDetails.COUNT_USERS, RoomDetails.PERSIST);

    void register(RRoom room, boolean persist) throws NodeNotFoundException, InvalidRoomIdentifierException, RoomAlreadyExistException;

    void remove(RRoom room) throws NodeNotFoundException;

    void remove(ResponseNode node, RRoom.Short room) throws NodeNotFoundException;

    default List<RRoom.Short> all(ResponseNode node) throws NodeNotFoundException {
        return all(node, Set.of());
    }

    List<RRoom.Short> all(ResponseNode node, Set<RoomDetails> details) throws NodeNotFoundException;

    Optional<RRoom> find(ResponseNode node, String roomId) throws NodeNotFoundException;

    void lock(RRoom room, boolean value) throws NodeNotFoundException, RoomNotFoundException;
}
