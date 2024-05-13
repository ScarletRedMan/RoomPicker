package ru.dragonestia.picker.api.impl.repository;

import ru.dragonestia.picker.api.impl.util.RestTemplate;
import ru.dragonestia.picker.api.model.instance.InstanceId;
import ru.dragonestia.picker.api.model.room.Room;
import ru.dragonestia.picker.api.model.room.RoomId;
import ru.dragonestia.picker.api.repository.RoomRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class RoomRepositoryImpl implements RoomRepository {

    private final RestTemplate restTemplate;

    public RoomRepositoryImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<RoomId> allRoomsIds(InstanceId instanceId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Room getRoom(InstanceId instanceId, RoomId roomId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Map<RoomId, Room> getRooms(InstanceId instanceId, Collection<RoomId> rooms) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void createRoom(InstanceId instanceId, RoomId roomId, int slots, String payload, boolean locked, boolean persist) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteRoom(InstanceId instanceId, RoomId roomId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteRooms(InstanceId instanceId, Collection<RoomId> rooms) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void lockRoom(InstanceId instanceId, RoomId roomId, boolean newState) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
