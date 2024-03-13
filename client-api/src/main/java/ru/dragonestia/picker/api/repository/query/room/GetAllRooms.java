package ru.dragonestia.picker.api.repository.query.room;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.model.room.RoomDetails;
import ru.dragonestia.picker.api.repository.type.NodeIdentifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GetAllRooms {

    private final String nodeId;
    private final Set<RoomDetails> details;

    private GetAllRooms(String nodeId, Set<RoomDetails> details) {
        this.nodeId = nodeId;
        this.details = details;
    }

    public @NotNull String getNodeId() {
        return nodeId;
    }

    public @NotNull Set<RoomDetails> getDetails() {
        return details;
    }

    @Contract("_ -> new")
    public static @NotNull GetAllRooms just(@NotNull NodeIdentifier nodeIdentifier) {
        return GetAllRooms.builder().setNodeId(nodeIdentifier).build();
    }

    @Contract("_ -> new")
    public static @NotNull GetAllRooms withAllDetails(@NotNull NodeIdentifier nodeIdentifier) {
        return GetAllRooms.builder()
                .setNodeId(nodeIdentifier)
                .setDetails(Arrays.stream(RoomDetails.values()).collect(Collectors.toSet()))
                .build();
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String nodeId = null;
        private Set<RoomDetails> details = new HashSet<>();

        private Builder() {}

        @Contract("_ -> this")
        public @NotNull Builder setNodeId(@NotNull NodeIdentifier identifier) {
            nodeId = identifier.getValue();
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder setDetails(@NotNull Set<RoomDetails> details) {
            this.details = details;
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder appendDetail(@NotNull RoomDetails detail) {
            details.add(detail);
            return this;
        }

        public @NotNull GetAllRooms build() {
            if (nodeId == null) {
                throw new NullPointerException("Node id is null");
            }

            return new GetAllRooms(nodeId, Collections.unmodifiableSet(details));
        }
    }
}
