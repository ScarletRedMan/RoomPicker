package ru.dragonestia.loadbalancer.web.model.dto;

import ru.dragonestia.loadbalancer.web.model.Node;

import java.net.URI;

public record BucketDTO(String identifier, int slots, boolean locked) {

    public URI uriAPI(Node node) {
        return uriAPI(node.identifier());
    }

    public URI uriAPI(String nodeId) {
        return URI.create("/nodes/" + nodeId + "/buckets/" + identifier);
    }
}
