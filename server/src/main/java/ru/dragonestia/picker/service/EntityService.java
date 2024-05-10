package ru.dragonestia.picker.service;

import ru.dragonestia.picker.exception.RoomAreFullException;
import ru.dragonestia.picker.model.entity.EntityId;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.model.entity.Entity;

import java.util.Collection;
import java.util.List;

public interface EntityService {

    Collection<Room> getEntityRooms(EntityId id);

    void linkEntitiesWithRoom(Room room, Collection<EntityId> entities, boolean force) throws RoomAreFullException;

    void unlinkEntitiesFromRoom(Room room, Collection<EntityId> entities);

    Collection<Entity> getRoomEntities(Room room);

    List<Entity> searchEntities(EntityId input);
}
