package ru.dragonestia.picker.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.api.exception.NoRoomsAvailableException;
import ru.dragonestia.picker.api.exception.InstanceNotFoundException;
import ru.dragonestia.picker.api.exception.RoomAlreadyExistException;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.model.entity.Entity;
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
    public void create(Room room) throws RoomAlreadyExistException {
        containerRepository.findById(room.getInstanceIdentifier())
                .orElseThrow(() -> new InstanceNotFoundException(room.getInstanceIdentifier()))
                .addRoom(room);
    }

    @Override
    public void remove(Room room) {
        containerRepository.findById(room.getInstanceIdentifier())
                .orElseThrow(() -> new InstanceNotFoundException(room.getInstanceIdentifier()))
                .removeRoom(room);
    }

    @Override
    public Optional<Room> find(Instance instance, String identifier) {
        return containerRepository.findById(instance.getIdentifier())
                .orElseThrow(() -> new InstanceNotFoundException(instance.getIdentifier()))
                .findRoomById(identifier)
                .map(RoomContainer::getRoom);
    }

    @Override
    public Collection<Room> all(Instance instance) {
        return containerRepository.findById(instance.getIdentifier())
                .orElseThrow(() -> new InstanceNotFoundException(instance.getIdentifier()))
                .allRooms()
                .stream().map(RoomContainer::getRoom).toList();
    }

    @Override
    public Room pick(Instance instance, Set<Entity> entities) throws NoRoomsAvailableException {
        return containerRepository.findById(instance.getIdentifier())
                .orElseThrow(() -> new InstanceNotFoundException(instance.getIdentifier()))
                .pick(entities);
    }
}
