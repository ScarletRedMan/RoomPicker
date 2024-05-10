package ru.dragonestia.picker.service;

import ru.dragonestia.picker.exception.AlreadyExistsException;
import ru.dragonestia.picker.exception.InvalidIdentifierException;
import ru.dragonestia.picker.model.entity.EntityId;
import ru.dragonestia.picker.model.instance.InstanceId;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.model.room.RoomId;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface RoomService {

    void create(Room room) throws InvalidIdentifierException, AlreadyExistsException;

    void remove(Room room);

    Optional<Room> find(InstanceId instanceId, RoomId roomId);

    Collection<Room> all(InstanceId instanceId);

    Room pick(InstanceId instanceId, Set<EntityId> entities);

    void updateState(Room room);
}
