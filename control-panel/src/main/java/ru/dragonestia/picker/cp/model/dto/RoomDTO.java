package ru.dragonestia.picker.cp.model.dto;

import ru.dragonestia.picker.cp.model.Node;

import java.net.URI;

public record RoomDTO(String id, int slots, boolean locked) {

    public URI uriAPI(Node node) {
        return uriAPI(node.id());
    }

    public URI uriAPI(String nodeId) {
        return URI.create("/nodes/" + nodeId + "/rooms/" + id);
    }
}
