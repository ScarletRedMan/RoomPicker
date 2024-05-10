package ru.dragonestia.picker.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.exception.AlreadyExistsException;
import ru.dragonestia.picker.exception.DoesNotExistsException;
import ru.dragonestia.picker.exception.NoRoomsAvailableException;
import ru.dragonestia.picker.model.entity.EntityId;
import ru.dragonestia.picker.model.instance.InstanceId;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.model.room.RoomId;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.repository.impl.container.RoomContainer;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepository {

    private final ContainerRepository containerRepository;

    @Override
    public void create(Room room) throws AlreadyExistsException {
        containerRepository.findById(room.getInstance().getId())
                .orElseThrow(() -> DoesNotExistsException.forInstance(room.getInstance().getId()))
                .addRoom(room);
    }

    @Override
    public void remove(InstanceId instanceId, RoomId roomId) {
        containerRepository.findById(instanceId)
                .orElseThrow(() -> DoesNotExistsException.forInstance(instanceId))
                .removeRoom(roomId);
    }

    @Override
    public Optional<Room> find(InstanceId instanceId, RoomId roomId) {
        return containerRepository.findById(instanceId)
                .orElseThrow(() -> DoesNotExistsException.forInstance(instanceId))
                .findRoomById(roomId)
                .map(RoomContainer::getRoom);
    }

    @Override
    public Collection<Room> all(InstanceId instanceId) {
        return containerRepository.findById(instanceId)
                .orElseThrow(() -> DoesNotExistsException.forInstance(instanceId))
                .allRooms()
                .stream().map(RoomContainer::getRoom).toList();
    }

    @Override
    public Room pick(InstanceId instanceId, Set<EntityId> entities) throws NoRoomsAvailableException {
        return containerRepository.findById(instanceId)
                .orElseThrow(() -> DoesNotExistsException.forInstance(instanceId))
                .pick(entities);
    }
}
