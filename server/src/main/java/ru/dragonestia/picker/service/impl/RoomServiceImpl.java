package ru.dragonestia.picker.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.dragonestia.picker.api.exception.InvalidRoomIdentifierException;
import ru.dragonestia.picker.api.exception.NodeNotFoundException;
import ru.dragonestia.picker.api.exception.NotPersistedNodeException;
import ru.dragonestia.picker.api.exception.RoomAlreadyExistException;
import ru.dragonestia.picker.api.model.room.RoomDetails;
import ru.dragonestia.picker.api.model.room.ShortResponseRoom;
import ru.dragonestia.picker.api.repository.response.PickedRoomResponse;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.NodeRepository;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.repository.UserRepository;
import ru.dragonestia.picker.service.RoomService;
import ru.dragonestia.picker.storage.NodeAndRoomStorage;
import ru.dragonestia.picker.util.DetailsExtractor;
import ru.dragonestia.picker.util.NamingValidator;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final NodeRepository nodeRepository;
    private final UserRepository userRepository;
    private final DetailsExtractor detailsExtractor;
    private final NamingValidator namingValidator;
    private final NodeAndRoomStorage storage;

    @Override
    public void create(Room room) throws InvalidRoomIdentifierException, RoomAlreadyExistException, NotPersistedNodeException {
        namingValidator.validateRoomId(room.getNodeIdentifier(), room.getIdentifier());

        var node = nodeRepository.find(room.getNodeIdentifier()).orElseThrow(() -> new NodeNotFoundException(room.getNodeIdentifier()));
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
    public Optional<Room> find(Node node, String roomId) {
        return roomRepository.find(node, roomId);
    }

    @Override
    public List<Room> all(Node node) {
        return roomRepository.all(node);
    }

    @Override
    public List<ShortResponseRoom> getAllRoomsWithDetailsResponse(Node node, Set<RoomDetails> details) {
        var response = new LinkedList<ShortResponseRoom>();
        for (var room: all(node)) {
            response.add(detailsExtractor.extract(room, details));
        }
        return response;
    }

    @Override
    public PickedRoomResponse pickAvailable(Node node, List<User> users) {
        var room = roomRepository.pickFree(node, users)
                .orElseThrow(() -> new RuntimeException("There are no rooms available"));
        var roomUsers = userRepository.usersOf(room);

        return new PickedRoomResponse(
                room.getNodeIdentifier(),
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
