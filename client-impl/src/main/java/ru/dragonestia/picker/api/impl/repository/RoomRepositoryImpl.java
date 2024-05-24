package ru.dragonestia.picker.api.impl.repository;

import ru.dragonestia.picker.api.impl.util.RestTemplate;
import ru.dragonestia.picker.api.impl.util.type.HttpMethod;
import ru.dragonestia.picker.api.model.instance.InstanceId;
import ru.dragonestia.picker.api.model.room.Room;
import ru.dragonestia.picker.api.model.room.RoomId;
import ru.dragonestia.picker.api.repository.RoomRepository;
import ru.dragonestia.picker.api.repository.response.ResponseObject;

import java.util.*;

public class RoomRepositoryImpl implements RoomRepository {

    private final RestTemplate rest;

    public RoomRepositoryImpl(RestTemplate rest) {
        this.rest = rest;
    }

    @Override
    public List<RoomId> allRoomsIds(InstanceId instanceId) {
        return Arrays.stream(rest.query("/instances/target/%s/rooms".formatted(instanceId.getValue()), HttpMethod.GET, String[].class))
                .map(RoomId::of)
                .toList();
    }

    @Override
    public Room getRoom(InstanceId instanceId, RoomId roomId) {
        return rest.query("/instances/target/%s/rooms/target/%s".formatted(instanceId.getValue(), roomId.getValue()), HttpMethod.GET, ResponseObject.RRoom.class).covert();
    }

    @Override
    public Map<RoomId, Room> getRooms(InstanceId instanceId, Collection<RoomId> rooms) {
        var map = new HashMap<RoomId, Room>();
        Arrays.stream(rest.query("/instances/target/%s/rooms/list".formatted(instanceId.getValue()), HttpMethod.GET, ResponseObject.RRoom[].class, params -> {
            params.put("id", String.join(",", rooms.stream().map(RoomId::getValue).toList()));
        })).map(ResponseObject.RRoom::covert).forEach(room -> map.put(room.id(), room));
        return map;
    }

    @Override
    public void createRoom(InstanceId instanceId, RoomId roomId, int slots, String payload, boolean locked, boolean persist) {
        rest.queryPostWithBody("/instances/target/%s/rooms".formatted(instanceId.getValue()), params -> {
            params.put("instanceId", instanceId.getValue());
            params.put("id", roomId.getValue());
            params.put("slots", Integer.toString(slots));
            params.put("locked", Boolean.toString(locked));
            params.put("persist", Boolean.toString(persist));
        }, payload);
    }

    @Override
    public void deleteRoom(InstanceId instanceId, RoomId roomId) {
        rest.query("/instances/target/%s/rooms/target/%s".formatted(instanceId.getValue(), roomId.getValue()), HttpMethod.DELETE);
    }

    @Override
    public void deleteRooms(InstanceId instanceId, Collection<RoomId> rooms) {
        rest.query("/instances/target/%s/rooms/list".formatted(instanceId.getValue()), HttpMethod.DELETE, params -> {
            params.put("id", String.join(",", rooms.stream().map(RoomId::getValue).toList()));
        });
    }

    @Override
    public void lockRoom(InstanceId instanceId, RoomId roomId, boolean newState) {
        rest.query("/instances/target/%s/rooms/target/%s".formatted(instanceId.getValue(), roomId.getValue()), HttpMethod.PUT, param -> {
            param.put("newState", Boolean.toString(newState));
        });
    }
}
