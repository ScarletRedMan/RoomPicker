package ru.dragonestia.picker.repository;

import ru.dragonestia.picker.api.exception.NodeAlreadyExistException;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.Room;

import java.util.List;
import java.util.Optional;

public interface NodeRepository {

    void create(Node node) throws NodeAlreadyExistException;

    List<Room> delete(Node node);

    Optional<Node> find(String nodeId);

    List<Node> all();
}
