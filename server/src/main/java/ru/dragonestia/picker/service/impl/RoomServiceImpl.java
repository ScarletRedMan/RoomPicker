package ru.dragonestia.picker.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.dragonestia.picker.api.exception.InvalidRoomIdentifierException;
import ru.dragonestia.picker.api.exception.InstanceNotFoundException;
import ru.dragonestia.picker.api.exception.NotPersistedNodeException;
import ru.dragonestia.picker.api.exception.RoomAlreadyExistException;
import ru.dragonestia.picker.api.repository.response.PickedRoomResponse;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.model.entity.Entity;
import ru.dragonestia.picker.repository.InstanceRepository;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.repository.EntityRepository;
import ru.dragonestia.picker.service.RoomService;
import ru.dragonestia.picker.storage.InstanceAndRoomStorage;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final InstanceRepository instanceRepository;
    private final EntityRepository entityRepository;
    private final InstanceAndRoomStorage storage;

    @Override
    public void create(Room room) throws InvalidRoomIdentifierException, RoomAlreadyExistException, NotPersistedNodeException {
        var node = instanceRepository.findById(room.getInstanceIdentifier()).orElseThrow(() -> new InstanceNotFoundException(room.getInstanceIdentifier()));
        if (!node.isPersist() && room.isPersist()) {
            throw new NotPersistedNodeException(node.getIdentifier(), room.getIdentifier());
        }

        roomRepository.create(room);
        storage.saveRoom(room);
    }

    @Override
    public void remove(Room room) {
        roomRepository.remove(room);
        storage.removeRoom(room);
    }

    @Override
    public Optional<Room> find(Instance instance, String roomId) {
        return roomRepository.find(instance, roomId);
    }

    @Override
    public Collection<Room> all(Instance instance) {
        return roomRepository.all(instance);
    }

    @Override
    public PickedRoomResponse pickAvailable(Instance instance, Set<Entity> entities) {
        var room = roomRepository.pick(instance, entities);
        var roomUsers = entityRepository.entitiesOf(room);

        return new PickedRoomResponse(
                room.getInstanceIdentifier(),
                room.getIdentifier(),
                room.getPayload(),
                room.getMaxSlots(),
                roomUsers.size(),
                room.isLocked(),
                roomUsers.stream().map(Entity::getIdentifier).collect(Collectors.toSet())
        );
    }

    @Override
    public void updateState(Room room) {
        storage.saveRoom(room);
    }
}
