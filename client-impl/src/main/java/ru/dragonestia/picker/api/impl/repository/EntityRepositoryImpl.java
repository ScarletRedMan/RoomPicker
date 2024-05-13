package ru.dragonestia.picker.api.impl.repository;

import ru.dragonestia.picker.api.impl.util.RestTemplate;
import ru.dragonestia.picker.api.model.entity.EntityId;
import ru.dragonestia.picker.api.model.instance.InstanceId;
import ru.dragonestia.picker.api.model.room.Room;
import ru.dragonestia.picker.api.model.room.RoomId;
import ru.dragonestia.picker.api.repository.EntityRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class EntityRepositoryImpl implements EntityRepository {

    private final RestTemplate restTemplate;

    public EntityRepositoryImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<EntityId> searchUsers(EntityId input) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<Room> getRooms(EntityId entity) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Map<EntityId, List<Room>> getRooms(Collection<EntityId> entities) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<EntityId> getRoomEntities(InstanceId instanceId, RoomId roomId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void linkEntitiesWithRoom(InstanceId instanceId, RoomId roomId, Collection<EntityId> entities, boolean force) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void unlinkEntitiesFromRoom(InstanceId instanceId, RoomId roomId, Collection<EntityId> entities) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
