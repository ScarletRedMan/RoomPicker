package ru.dragonestia.picker.cp.repository;

import ru.dragonestia.picker.cp.model.Node;

import java.util.List;
import java.util.Optional;

public interface NodeRepository {

    void register(Node node);

    List<Node> all();

    Optional<Node> find(String nodeId);

    void remove(String nodeId);
}
