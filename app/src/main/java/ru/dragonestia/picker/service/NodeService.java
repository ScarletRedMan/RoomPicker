package ru.dragonestia.picker.service;

import ru.dragonestia.picker.api.exception.InvalidNodeIdentifierException;
import ru.dragonestia.picker.api.exception.NodeAlreadyExistException;
import ru.dragonestia.picker.model.Node;

import java.util.List;
import java.util.Optional;

public interface NodeService {

    void create(Node node) throws InvalidNodeIdentifierException, NodeAlreadyExistException;

    void remove(Node node);

    List<Node> all();

    Optional<Node> find(String nodeId);
}
