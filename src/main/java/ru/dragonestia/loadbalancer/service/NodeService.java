package ru.dragonestia.loadbalancer.service;

import ru.dragonestia.loadbalancer.model.Node;

import java.util.List;

public interface NodeService {

    void createNode(Node node);

    void removeNode(Node node);

    List<Node> allNodes();
}
