package ru.dragonestia.loadbalancer.web.repository.impl.response;

import ru.dragonestia.loadbalancer.web.model.Node;

import java.util.List;

public record NodeListResponse(List<Node> nodes) {}
