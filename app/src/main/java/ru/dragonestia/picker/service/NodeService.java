package ru.dragonestia.picker.service;

import ru.dragonestia.picker.model.Node;

import java.util.List;
import java.util.Optional;

public interface NodeService {

    void createNode(Node node);

    void removeNode(Node node);

    List<Node> allNodes();

    Optional<Node> findNode(String identifier);
}
