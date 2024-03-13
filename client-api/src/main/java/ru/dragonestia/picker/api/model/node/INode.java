package ru.dragonestia.picker.api.model.node;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.dragonestia.picker.api.repository.type.NodeIdentifier;

public interface INode {

    @NotNull String getIdentifier();

    default @NotNull NodeIdentifier getIdentifierObject() {
        return NodeIdentifier.of(getIdentifier());
    }

    @NotNull PickingMethod getPickingMethod();

    @Nullable Boolean isPersist();

    @Nullable String getDetail(@NotNull NodeDetails detail);
}
