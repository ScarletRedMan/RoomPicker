package ru.dragonestia.picker.api.repository.query.node;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.model.node.NodeDetails;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GetAllNodes {

    public static GetAllNodes JUST = GetAllNodes.builder().build();
    public static GetAllNodes WITH_ALL_DETAILS = GetAllNodes.builder()
            .setDetails(Stream.of(NodeDetails.values()).collect(Collectors.toSet())).build();

    private final Set<NodeDetails> details;

    private GetAllNodes(Set<NodeDetails> details) {
        this.details = details;
    }

    public @NotNull Set<NodeDetails> getDetails() {
        return details;
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Set<NodeDetails> details = new HashSet<>();

        private Builder() {}

        @Contract("_ -> this")
        public @NotNull Builder setDetails(@NotNull Set<NodeDetails> details) {
            this.details = details;
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder appendDetail(@NotNull NodeDetails detail) {
            this.details.add(detail);
            return this;
        }

        public @NotNull GetAllNodes build() {
            return new GetAllNodes(Collections.unmodifiableSet(details));
        }
    }
}
