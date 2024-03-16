package ru.dragonestia.picker.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.dragonestia.picker.api.model.node.INode;
import ru.dragonestia.picker.api.model.node.NodeDetails;
import ru.dragonestia.picker.api.model.node.PickingMethod;
import ru.dragonestia.picker.api.model.node.ResponseNode;
import ru.dragonestia.picker.api.repository.type.NodeIdentifier;

public class Node implements INode {

    private final String identifier;
    private final PickingMethod pickingMethod;
    private final boolean persist;

    public Node(@NotNull NodeIdentifier identifier, @NotNull PickingMethod pickingMethod, boolean persist) {
        this.identifier = identifier.getValue();
        this.pickingMethod = pickingMethod;
        this.persist = persist;
    }

    @Override
    public @NotNull String getIdentifier() {
        return identifier;
    }

    @Override
    public @NotNull PickingMethod getPickingMethod() {
        return pickingMethod;
    }

    @Override
    public @NotNull Boolean isPersist() {
        return persist;
    }

    @Override
    public @Nullable String getDetail(@NotNull NodeDetails detail) {
        throw new UnsupportedOperationException();
    }

    public @NotNull ResponseNode toResponseObject() {
        return new ResponseNode(identifier, pickingMethod);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof Node other) {
            return identifier.equals(other.identifier);
        }
        return false;
    }

    @Override
    public String toString() {
        return "{Node id='%s'}".formatted(identifier);
    }
}
