package ru.dragonestia.picker.api.repository;

import ru.dragonestia.picker.api.exception.*;
import ru.dragonestia.picker.api.model.node.ResponseNode;
import ru.dragonestia.picker.api.model.room.ResponseRoom;
import ru.dragonestia.picker.api.model.room.RoomDetails;
import ru.dragonestia.picker.api.model.room.ShortResponseRoom;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoomRepository {

    Set<RoomDetails> ALL_DETAILS = Set.of(RoomDetails.COUNT_USERS, RoomDetails.PERSIST);

    void register(ResponseRoom room, boolean persist) throws NodeNotFoundException, InvalidRoomIdentifierException, RoomAlreadyExistException;

    void remove(ResponseRoom room) throws NodeNotFoundException;

    void remove(ResponseNode node, ShortResponseRoom room) throws NodeNotFoundException;

    default List<ShortResponseRoom> all(ResponseNode node) throws NodeNotFoundException {
        return all(node, Set.of());
    }

    List<ShortResponseRoom> all(ResponseNode node, Set<RoomDetails> details) throws NodeNotFoundException;

    Optional<ResponseRoom> find(ResponseNode node, String roomId) throws NodeNotFoundException;

    void lock(ResponseRoom room, boolean value) throws NodeNotFoundException, RoomNotFoundException;
}
