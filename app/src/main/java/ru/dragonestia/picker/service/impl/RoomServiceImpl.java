package ru.dragonestia.picker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.service.RoomService;
import ru.dragonestia.picker.util.NamingValidator;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public void create(Room room) {
        if (!NamingValidator.validateRoomId(room.getId())) {
            throw new Error("Invalid room id format");
        }

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
    public int countAvailable(Node node, int requiredSlots) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Room pickAvailable(Node node, List<User> users) {
        throw new RuntimeException("Not implemented");
    }
}
