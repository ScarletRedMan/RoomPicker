package ru.dragonestia.picker.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.dragonestia.picker.exception.AlreadyExistsException;
import ru.dragonestia.picker.exception.ConflictingPersistParametersException;
import ru.dragonestia.picker.exception.DoesNotExistsException;
import ru.dragonestia.picker.exception.InvalidIdentifierException;
import ru.dragonestia.picker.model.entity.EntityId;
import ru.dragonestia.picker.model.instance.InstanceId;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.model.room.RoomId;
import ru.dragonestia.picker.repository.InstanceRepository;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.repository.EntityRepository;
import ru.dragonestia.picker.service.RoomService;
import ru.dragonestia.picker.storage.InstanceAndRoomStorage;

import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final InstanceRepository instanceRepository;
    private final InstanceAndRoomStorage storage;

    @Override
    public void create(Room room) throws InvalidIdentifierException, AlreadyExistsException, ConflictingPersistParametersException {
        var instance = instanceRepository.findById(room.getInstance().getId())
                .orElseThrow(() -> DoesNotExistsException.forInstance(room.getInstance().getId()));

        if (!instance.isPersist() && room.isPersist()) {
            throw ConflictingPersistParametersException.forRoom(instance.getId(), room.getId());
        }

        roomRepository.create(room);
        storage.saveRoom(room);
    }

    @Override
    public void remove(Room room) {
        roomRepository.remove(room.getInstance().getId(), room.getId());
        storage.removeRoom(room);
    }

    @Override
    public Optional<Room> find(InstanceId instanceId, RoomId roomId) {
        return roomRepository.find(instanceId, roomId);
    }

    @Override
    public Collection<Room> all(InstanceId instanceId) {
        return roomRepository.all(instanceId);
    }

    @Override
    public Room pick(InstanceId instanceId, Set<EntityId> entities) {
        return roomRepository.pick(instanceId, entities);
    }

    @Override
    public void updateState(Room room) {
        storage.saveRoom(room);
    }
}
