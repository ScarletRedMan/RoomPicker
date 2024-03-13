package ru.dragonestia.picker.api.repository.query.node;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.model.node.NodeDetails;
import ru.dragonestia.picker.api.repository.type.NodeIdentifier;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FindNodeById {

    private final String id;
    private final Set<NodeDetails> details;

    private FindNodeById(String id, Set<NodeDetails> details) {
        this.id = id;
        this.details = details;
    }

    public @NotNull String getId() {
        return id;
    }

    public @NotNull Set<NodeDetails> getDetails() {
        return details;
    }

    @Contract("_ -> new")
    public static @NotNull FindNodeById justFind(@NotNull NodeIdentifier identifier) {
        return FindNodeById.builder().setId(identifier.getValue()).build();
    }

    @Contract("_ -> new")
    public static @NotNull FindNodeById findWithAllDetails(@NotNull NodeIdentifier identifier) {
        return FindNodeById.builder()
                .setId(identifier.getValue())
                .setDetails(Stream.of(NodeDetails.values()).collect(Collectors.toSet()))
                .build();
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String id = null;
        private Set<NodeDetails> details = new HashSet<>();

        private Builder() {}

        @Contract("_ -> this")
        public @NotNull Builder setId(@NotNull String id) {
            this.id = id;
            return this;
        }

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

        public @NotNull FindNodeById build() {
            if (id == null) {
                throw new NullPointerException("Id is null");
            }

            return new FindNodeById(id, Collections.unmodifiableSet(details));
        }
    }
}
