package ru.dragonestia.picker.service;

import ru.dragonestia.picker.api.exception.InvalidRoomIdentifierException;
import ru.dragonestia.picker.api.exception.RoomAlreadyExistException;
import ru.dragonestia.picker.api.repository.response.PickedRoomResponse;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.model.entity.Entity;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface RoomService {

    void create(Room room) throws InvalidRoomIdentifierException, RoomAlreadyExistException;

    void remove(Room room);

    Optional<Room> find(Instance instance, String roomId);

    Collection<Room> all(Instance instance);

    PickedRoomResponse pickAvailable(Instance instance, Set<Entity> entities);

    void updateState(Room room);
}
