package ru.dragonestia.picker.repository;

import ru.dragonestia.picker.exception.RoomAreFullException;
import ru.dragonestia.picker.model.entity.EntityId;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.model.entity.Entity;

import java.util.Collection;
import java.util.Map;

public interface EntityRepository {

    void linkWithRoom(Room room, Collection<EntityId> entities, boolean force) throws RoomAreFullException;

    void unlinkWithRoom(Room room, Collection<EntityId> entities);

    Collection<Room> findAllLinkedEntityRooms(EntityId entity);

    Collection<Entity> entitiesOf(Room room);

    Collection<Entity> search(EntityId input);

    int countAllEntities();

    Map<String, Integer> countEntitiesForInstances();
}
