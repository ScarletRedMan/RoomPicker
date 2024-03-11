package ru.dragonestia.picker.api.model.node;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface INode {

    @NotNull String getIdentifier();

    @NotNull PickingMethod getPickingMethod();

    @Nullable Boolean isPersist();

    @Nullable String getDetail(@NotNull NodeDetails detail);
}
