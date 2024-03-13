package ru.dragonestia.picker.api.model.node;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.dragonestia.picker.api.repository.type.NodeIdentifier;

public class NodeDefinition implements INode {

    private final String identifier;
    private PickingMethod pickingMethod = PickingMethod.SEQUENTIAL_FILLING;
    private boolean persist = false;

    public NodeDefinition(@NotNull NodeIdentifier identifier) {
        this.identifier = identifier.getValue();
    }

    @Override
    public @NotNull String getIdentifier() {
        return identifier;
    }

    @Override
    public @NotNull PickingMethod getPickingMethod() {
        return pickingMethod;
    }

    @Contract("_ -> this")
    public @NotNull NodeDefinition setPickingMethod(@NotNull PickingMethod pickingMethod) {
        this.pickingMethod = pickingMethod;
        return this;
    }

    @Override
    public @NotNull Boolean isPersist() {
        return persist;
    }

    @Contract("_ -> this")
    public @NotNull NodeDefinition setPersist(boolean value) {
        persist = value;
        return this;
    }

    @Override
    public @Nullable String getDetail(@NotNull NodeDetails detail) {
        throw new UnsupportedOperationException();
    }
}
