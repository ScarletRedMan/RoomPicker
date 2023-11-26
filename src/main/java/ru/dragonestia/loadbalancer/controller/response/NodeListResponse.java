package ru.dragonestia.loadbalancer.controller.response;

import ru.dragonestia.loadbalancer.model.Node;

import java.util.List;

public record NodeListResponse(List<Node> nodes) {}
