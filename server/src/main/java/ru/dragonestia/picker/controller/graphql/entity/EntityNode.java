package ru.dragonestia.picker.controller.graphql.entity;

import jakarta.validation.constraints.NotNull;
import ru.dragonestia.picker.model.Node;

public class EntityNode {

    private final Node node;

    public EntityNode(Node node) {
        this.node = node;
    }

    public @NotNull String getId() {
        return node.getIdentifier();
    }

    public @NotNull String getMethod() {
        return node.getPickingMethod().name();
    }
}
