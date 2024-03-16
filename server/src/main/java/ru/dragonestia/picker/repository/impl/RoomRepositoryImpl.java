package ru.dragonestia.picker.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.api.exception.NoRoomsAvailableException;
import ru.dragonestia.picker.api.exception.NodeNotFoundException;
import ru.dragonestia.picker.api.exception.RoomAlreadyExistException;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.repository.impl.container.NodeContainer;
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
        containerRepository.findById(room.getNodeIdentifier())
                .orElseThrow(() -> new NodeNotFoundException(room.getNodeIdentifier()))
                .addRoom(room);
    }

    @Override
    public void remove(Room room) {
        containerRepository.findById(room.getNodeIdentifier())
                .orElseThrow(() -> new NodeNotFoundException(room.getNodeIdentifier()))
                .removeRoom(room);
    }

    @Override
    public Optional<Room> find(Node node, String identifier) {
        return containerRepository.findById(node.getIdentifier())
                .orElseThrow(() -> new NodeNotFoundException(node.getIdentifier()))
                .findRoomById(identifier)
                .map(RoomContainer::getRoom);
    }

    @Override
    public Collection<Room> all(Node node) {
        return containerRepository.findById(node.getIdentifier())
                .orElseThrow(() -> new NodeNotFoundException(node.getIdentifier()))
                .allRooms()
                .stream().map(RoomContainer::getRoom).toList();
    }

    @Override
    public Room pick(Node node, Set<User> users) throws NoRoomsAvailableException {
        return containerRepository.findById(node.getIdentifier())
                .orElseThrow(() -> new NodeNotFoundException(node.getIdentifier()))
                .pick(users);
    }
}
