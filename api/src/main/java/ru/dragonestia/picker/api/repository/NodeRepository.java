package ru.dragonestia.picker.api.repository;

import ru.dragonestia.picker.api.exception.InvalidNodeIdentifierException;
import ru.dragonestia.picker.api.exception.NodeAlreadyExistException;
import ru.dragonestia.picker.api.model.Node;

import java.util.List;
import java.util.Optional;

public interface NodeRepository {

    void register(Node node) throws InvalidNodeIdentifierException, NodeAlreadyExistException;

    List<Node> all();

    Optional<Node> find(String nodeId);

    void remove(String nodeId);
}
