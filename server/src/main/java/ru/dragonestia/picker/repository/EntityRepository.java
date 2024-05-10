package ru.dragonestia.picker.repository;

import ru.dragonestia.picker.api.exception.RoomAreFullException;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.model.entity.Entity;

import java.util.Collection;
import java.util.Map;

public interface EntityRepository {

    void linkWithRoom(Room room, Collection<Entity> entities, boolean force) throws RoomAreFullException;

    void unlinkWithRoom(Room room, Collection<Entity> entities);

    Collection<Room> findAllLinkedEntityRooms(Entity entity);

    Collection<Entity> entitiesOf(Room room);

    Collection<Entity> search(String input);

    int countAllEntities();

    Map<String, Integer> countEntitiesForNodes();
}
