package ru.dragonestia.picker.api.repository.request.room;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.model.room.RoomDetails;
import ru.dragonestia.picker.api.repository.type.NodeIdentifier;
import ru.dragonestia.picker.api.repository.type.RoomIdentifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class FindRoomById {

    private final String nodeId;
    private final String id;
    private final Set<RoomDetails> details;

    private FindRoomById(String nodeId, String id, Set<RoomDetails> details) {
        this.id = id;
        this.nodeId = nodeId;
        this.details = details;
    }

    public @NotNull String getNodeId() {
        return nodeId;
    }

    public @NotNull String getId() {
        return id;
    }

    public @NotNull Set<RoomDetails> getDetails() {
        return details;
    }

    @Contract("_, _ -> new")
    public static @NotNull FindRoomById just(@NotNull NodeIdentifier nodeId, @NotNull RoomIdentifier roomId) {
        return FindRoomById.builder()
                .setNodeId(nodeId)
                .setRoomId(roomId)
                .build();
    }

    @Contract("_, _ -> new")
    public static @NotNull FindRoomById withAllDetails(@NotNull NodeIdentifier nodeId, @NotNull RoomIdentifier roomId) {
        return FindRoomById.builder()
                .setNodeId(nodeId)
                .setRoomId(roomId)
                .setDetails(Arrays.stream(RoomDetails.values()).collect(Collectors.toSet()))
                .build();
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String nodeId = null;
        private String roomId = null;
        private Set<RoomDetails> details = new HashSet<>();

        private Builder() {}

        @Contract("_ -> this")
        public @NotNull Builder setNodeId(@NotNull NodeIdentifier identifier) {
            nodeId = identifier.getValue();
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder setRoomId(@NotNull RoomIdentifier identifier) {
            roomId = identifier.getValue();
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

        public @NotNull FindRoomById build() {
            if (nodeId == null) {
                throw new NullPointerException("Node id is null");
            }
            if (roomId == null) {
                throw new NullPointerException("Room id is null");
            }

            return new FindRoomById(nodeId, roomId, Collections.unmodifiableSet(details));
        }
    }
}
