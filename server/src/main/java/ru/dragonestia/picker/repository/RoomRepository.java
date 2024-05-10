package ru.dragonestia.picker.repository;

import ru.dragonestia.picker.exception.AlreadyExistsException;
import ru.dragonestia.picker.exception.NoRoomsAvailableException;
import ru.dragonestia.picker.model.entity.EntityId;
import ru.dragonestia.picker.model.instance.InstanceId;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.model.room.RoomId;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface RoomRepository {

    void create(Room room) throws AlreadyExistsException;

    void remove(InstanceId instanceId, RoomId roomId);

    Optional<Room> find(InstanceId instanceId, RoomId roomId);

    Collection<Room> all(InstanceId instanceId);

    Room pick(InstanceId instanceId, Set<EntityId> entities) throws NoRoomsAvailableException;
}
