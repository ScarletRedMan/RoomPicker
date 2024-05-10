package ru.dragonestia.picker.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.dragonestia.picker.api.exception.InvalidRoomIdentifierException;
import ru.dragonestia.picker.api.exception.NodeNotFoundException;
import ru.dragonestia.picker.api.exception.NotPersistedNodeException;
import ru.dragonestia.picker.api.exception.RoomAlreadyExistException;
import ru.dragonestia.picker.api.repository.response.PickedRoomResponse;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.model.user.User;
import ru.dragonestia.picker.repository.InstanceRepository;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.repository.UserRepository;
import ru.dragonestia.picker.service.RoomService;
import ru.dragonestia.picker.storage.InstanceAndRoomStorage;
import ru.dragonestia.picker.util.NamingValidator;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final InstanceRepository instanceRepository;
    private final UserRepository userRepository;
    private final NamingValidator namingValidator;
    private final InstanceAndRoomStorage storage;

    @Override
    public void create(Room room) throws InvalidRoomIdentifierException, RoomAlreadyExistException, NotPersistedNodeException {
        namingValidator.validateRoomId(room.getInstanceIdentifier(), room.getIdentifier());

        var node = instanceRepository.findById(room.getInstanceIdentifier()).orElseThrow(() -> new NodeNotFoundException(room.getInstanceIdentifier()));
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
    public PickedRoomResponse pickAvailable(Instance instance, Set<User> users) {
        var room = roomRepository.pick(instance, users);
        var roomUsers = userRepository.usersOf(room);

        return new PickedRoomResponse(
                room.getInstanceIdentifier(),
                room.getIdentifier(),
                room.getPayload(),
                room.getMaxSlots(),
                roomUsers.size(),
                room.isLocked(),
                roomUsers.stream().map(User::getIdentifier).collect(Collectors.toSet())
        );
    }

    @Override
    public void updateState(Room room) {
        storage.saveRoom(room);
    }
}
