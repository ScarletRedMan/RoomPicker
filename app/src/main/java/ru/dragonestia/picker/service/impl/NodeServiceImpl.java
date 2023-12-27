package ru.dragonestia.picker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.repository.NodeRepository;
import ru.dragonestia.picker.service.NodeService;
import ru.dragonestia.picker.util.NamingValidator;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NodeServiceImpl implements NodeService {

    private final NodeRepository nodeRepository;

    @Override
    public void create(Node node) {
        if (!NamingValidator.validateNodeId(node.id())) {
            throw new Error("Invalid node id format");
        }

        nodeRepository.create(node);
    }

    @Override
    public void remove(Node node) {
        nodeRepository.delete(node);
    }

    @Override
    public List<Node> all() {
        return nodeRepository.all();
    }

    @Override
    public Optional<Node> find(String nodeId) {
        return nodeRepository.find(nodeId);
    }
}
