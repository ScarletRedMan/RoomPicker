package ru.dragonestia.picker.api.repository.query.node;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.repository.type.NodeIdentifier;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RemoveNodesByIds {

    private final Set<String> nodeIds;

    private RemoveNodesByIds(Set<String> nodeIds) {
        this.nodeIds = nodeIds;
    }

    public @NotNull Set<String> getNodeIds() {
        return nodeIds;
    }

    @Contract("_ -> new")
    public static @NotNull RemoveNodesByIds just(@NotNull NodeIdentifier nodeIdentifier) {
        return RemoveNodesByIds.builder().appendNodeId(nodeIdentifier).build();
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Set<NodeIdentifier> nodeIds = new HashSet<>();

        private Builder() {}

        @Contract("_ -> this")
        public @NotNull Builder setNodeIds(@NotNull HashSet<NodeIdentifier> nodeIds) {
            this.nodeIds = nodeIds;
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder appendNodeId(@NotNull NodeIdentifier nodeId) {
            nodeIds.add(nodeId);
            return this;
        }

        public @NotNull RemoveNodesByIds build() {
            return new RemoveNodesByIds(nodeIds.stream().map(obj -> obj.getValue()).collect(Collectors.toSet()));
        }
    }
}
