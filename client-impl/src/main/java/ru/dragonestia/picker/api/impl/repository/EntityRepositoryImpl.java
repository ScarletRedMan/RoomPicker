package ru.dragonestia.picker.api.impl.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.dragonestia.picker.api.impl.util.RestTemplate;
import ru.dragonestia.picker.api.impl.util.type.HttpMethod;
import ru.dragonestia.picker.api.model.entity.EntityId;
import ru.dragonestia.picker.api.model.instance.InstanceId;
import ru.dragonestia.picker.api.model.room.Room;
import ru.dragonestia.picker.api.model.room.RoomId;
import ru.dragonestia.picker.api.repository.EntityRepository;
import ru.dragonestia.picker.api.repository.response.ResponseObject;

import java.util.*;

public class EntityRepositoryImpl implements EntityRepository {

    private final RestTemplate rest;

    public EntityRepositoryImpl(RestTemplate rest) {
        this.rest = rest;
    }

    @Override
    public List<EntityId> searchUsers(EntityId input) {
        return rest.queryWithRequest("/entities/search", HttpMethod.GET, new TypeReference<List<String>>(){}, params -> {
            params.put("input", input.getValue());
        }).stream().map(EntityId::of).toList();
    }

    @Override
    public List<Room> getRooms(EntityId entity) {
        return rest.queryWithRequest("/entities/target/rooms",
                HttpMethod.GET,
                new TypeReference<List<ResponseObject.RRoom>>() {},
                params -> {
                    params.put("id", entity.getValue());
                }).stream().map(ResponseObject.RRoom::covert).toList();
    }

    @Override
    public Map<EntityId, List<Room>> getRooms(Collection<EntityId> entities) {
        var map = new HashMap<EntityId, List<Room>>();
        rest.queryWithRequest("/entities/list/rooms", HttpMethod.GET, new TypeReference<Map<String, List<ResponseObject.RRoom>>>() {}, params -> {
            params.put("id", String.join(",", entities.stream().map(EntityId::getValue).toList()));
        }).forEach((id, rooms) -> map.put(EntityId.of(id), rooms.stream().map(ResponseObject.RRoom::covert).toList()));
        return map;
    }

    @Override
    public List<EntityId> getRoomEntities(InstanceId instanceId, RoomId roomId) {
        return rest.queryWithRequest("/instances/%s/rooms/target/%s/users".formatted(instanceId.getValue(), roomId.getValue()), HttpMethod.GET, new TypeReference<List<String>>(){}, params -> {
            params.put("instanceId", instanceId.getValue());
            params.put("roomId", roomId.getValue());
        }).stream().map(EntityId::of).toList();
    }

    @Override
    public void linkEntitiesWithRoom(InstanceId instanceId, RoomId roomId, Collection<EntityId> entities, boolean force) {
        rest.query("/instances/%s/rooms/target/%s/users".formatted(instanceId.getValue(), roomId.getValue()), HttpMethod.POST, params -> {
            params.put("instanceId", instanceId.getValue());
            params.put("roomId", roomId.getValue());
            params.put("entities", String.join(",", entities.stream().map(EntityId::getValue).toList()));
            params.put("force", Boolean.toString(force));
        });
    }

    @Override
    public void unlinkEntitiesFromRoom(InstanceId instanceId, RoomId roomId, Collection<EntityId> entities) {
        rest.query("/instances/%s/rooms/target/%s/users".formatted(instanceId.getValue(), roomId.getValue()), HttpMethod.DELETE, params -> {
            params.put("instanceId", instanceId.getValue());
            params.put("roomId", roomId.getValue());
            params.put("entities", String.join(",", entities.stream().map(EntityId::getValue).toList()));
        });
    }
}
