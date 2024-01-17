package ru.dragonestia.picker.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.dragonestia.picker.api.exception.InvalidRoomIdentifierException;
import ru.dragonestia.picker.api.exception.RoomAlreadyExistException;
import ru.dragonestia.picker.api.repository.details.RoomDetails;
import ru.dragonestia.picker.api.repository.details.UserDetails;
import ru.dragonestia.picker.api.repository.response.type.RRoom;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.repository.UserRepository;
import ru.dragonestia.picker.service.RoomService;
import ru.dragonestia.picker.util.NamingValidator;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final NamingValidator namingValidator;

    @Override
    public void create(Room room) throws InvalidRoomIdentifierException, RoomAlreadyExistException {
        namingValidator.validateRoomId(room.getNodeId(), room.getId());
        roomRepository.create(room);
    }

    @Override
    public void remove(Room room) {
        roomRepository.remove(room);
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
    public List<RRoom.Short> getAllRoomsWithDetailsResponse(Node node, Set<RoomDetails> details) {
        var response = new LinkedList<RRoom.Short>();
        for (var room: all(node)) {
            var responseRoom = room.toShortResponseObject();

            for (var detail: details) {
                if (detail == RoomDetails.COUNT_USERS) {
                    var users = Integer.toString(userRepository.usersOf(room).size());
                    responseRoom.details().put(RoomDetails.COUNT_USERS, users);
                }
            }

            response.add(responseRoom);
        }
        return response;
    }

    @Override
    public int countAvailable(Node node, int requiredSlots) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Room pickAvailable(Node node, List<User> users) {
        return roomRepository.pickFree(node, users)
                .orElseThrow(() -> new RuntimeException("There are no rooms available"));
    }
}
