package ru.dragonestia.picker.repository;

import ru.dragonestia.picker.api.exception.NoRoomsAvailableException;
import ru.dragonestia.picker.api.exception.RoomAlreadyExistException;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.model.entity.Entity;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface RoomRepository {

    void create(Room room) throws RoomAlreadyExistException;

    void remove(Room room);

    Optional<Room> find(Instance instance, String identifier);

    Collection<Room> all(Instance instance);

    Room pick(Instance instance, Set<Entity> entities) throws NoRoomsAvailableException;
}
