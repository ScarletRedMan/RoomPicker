package ru.dragonestia.picker.api.repository;

import ru.dragonestia.picker.api.model.entity.EntityId;
import ru.dragonestia.picker.api.model.instance.InstanceId;
import ru.dragonestia.picker.api.model.room.Room;
import ru.dragonestia.picker.api.model.room.RoomId;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface EntityRepository {

    List<EntityId> searchUsers(EntityId input);

    List<Room> getRooms(EntityId entity);

    Map<EntityId, List<Room>> getRooms(Collection<EntityId> entities);

    List<EntityId> getRoomEntities(InstanceId instanceId, RoomId roomId);

    default List<EntityId> getRoomEntities(Room room) {
        return getRoomEntities(room.instanceId(), room.id());
    }

    void linkEntitiesWithRoom(InstanceId instanceId, RoomId roomId, Collection<EntityId> entities, boolean force);

    default void linkEntitiesWithRoom(InstanceId instanceId, RoomId roomId, Collection<EntityId> entities) {
        linkEntitiesWithRoom(instanceId, roomId, entities, false);
    }

    default void linkEntitiesWithRoom(Room room, Collection<EntityId> entities) {
        linkEntitiesWithRoom(room.instanceId(), room.id(), entities);
    }

    default void linkEntitiesWithRoom(Room room, Collection<EntityId> entities, boolean force) {
        linkEntitiesWithRoom(room.instanceId(), room.id(), entities, force);
    }

    void unlinkEntitiesFromRoom(InstanceId instanceId, RoomId roomId, Collection<EntityId> entities);

    default void unlinkEntitiesFromRoom(Room room, Collection<EntityId> entities) {
        unlinkEntitiesFromRoom(room.instanceId(), room.id(), entities);
    }
}
