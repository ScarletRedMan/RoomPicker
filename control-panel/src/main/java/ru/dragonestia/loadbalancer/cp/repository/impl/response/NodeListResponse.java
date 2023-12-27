package ru.dragonestia.loadbalancer.cp.repository.impl.response;

import ru.dragonestia.loadbalancer.cp.model.Node;

import java.util.List;

public record NodeListResponse(List<Node> nodes) {}
