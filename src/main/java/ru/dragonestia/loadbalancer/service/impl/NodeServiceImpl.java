package ru.dragonestia.loadbalancer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dragonestia.loadbalancer.model.Node;
import ru.dragonestia.loadbalancer.repository.NodeRepository;
import ru.dragonestia.loadbalancer.service.NodeService;
import ru.dragonestia.loadbalancer.util.NamingValidator;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NodeServiceImpl implements NodeService {

    private final NodeRepository nodeRepository;

    @Override
    public void createNode(Node node) {
        if (!NamingValidator.validateNodeIdentifier(node.identifier())) {
            throw new Error("Invalid node identifier format");
        }

        nodeRepository.createNode(node);
    }

    @Override
    public void removeNode(Node node) {
        nodeRepository.deleteNode(node);
    }

    @Override
    public List<Node> allNodes() {
        return nodeRepository.all();
    }

    @Override
    public Optional<Node> findNode(String identifier) {
        return nodeRepository.findNode(identifier);
    }
}
