package ru.dragonestia.picker.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.api.exception.NodeAlreadyExistException;
import ru.dragonestia.picker.model.node.Node;
import ru.dragonestia.picker.repository.NodeRepository;
import ru.dragonestia.picker.repository.impl.container.NodeContainer;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NodeRepositoryImpl implements NodeRepository {

    private final ContainerRepository containerRepository;

    @Override
    public void create(Node node) throws NodeAlreadyExistException {
        containerRepository.create(node);
    }

    @Override
    public void delete(Node node) {
        containerRepository.remove(node.getIdentifier());
    }

    @Override
    public Optional<Node> findById(String nodeId) {
        return containerRepository.findById(nodeId).map(NodeContainer::getNode);
    }

    @Override
    public List<Node> all() {
        return containerRepository.all().stream().map(NodeContainer::getNode).toList();
    }
}
