package ru.dragonestia.picker.api.repository;

import ru.dragonestia.picker.api.exception.InvalidNodeIdentifierException;
import ru.dragonestia.picker.api.exception.NodeAlreadyExistException;
import ru.dragonestia.picker.api.repository.details.NodeDetails;
import ru.dragonestia.picker.api.repository.response.type.RNode;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface NodeRepository {

    Set<NodeDetails> ALL_DETAILS = Set.of();

    void register(RNode node) throws InvalidNodeIdentifierException, NodeAlreadyExistException;

    default List<RNode> all() {
        return all(Set.of());
    }

    List<RNode> all(Set<NodeDetails> details);

    Optional<RNode> find(String nodeId);

    void remove(String nodeId);
}
