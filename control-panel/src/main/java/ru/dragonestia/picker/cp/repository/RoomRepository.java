package ru.dragonestia.picker.cp.repository;

import ru.dragonestia.picker.cp.model.Room;
import ru.dragonestia.picker.cp.model.Node;
import ru.dragonestia.picker.cp.model.dto.RoomDTO;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {

    List<RoomDTO> all(Node node);

    void register(Room room);

    void remove(Room room);

    void remove(Node node, RoomDTO bucket);

    Optional<Room> find(Node node, String roomId);

    void lock(Room room, boolean value);
}
