package ru.dragonestia.picker.api.repository.request.user;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.model.user.UserDetails;
import ru.dragonestia.picker.api.repository.type.NodeIdentifier;
import ru.dragonestia.picker.api.repository.type.RoomIdentifier;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GetAllUsersFromRoom {

    private final String nodeId;
    private final String roomId;
    private final Set<UserDetails> details;

    private GetAllUsersFromRoom(String nodeId, String roomId, Set<UserDetails> details) {
        this.nodeId = nodeId;
        this.roomId = roomId;
        this.details = details;
    }

    public @NotNull String getNodeId() {
        return nodeId;
    }

    public @NotNull String getRoomId() {
        return roomId;
    }

    public @NotNull Set<UserDetails> getDetails() {
        return details;
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String nodeId = null;
        private String roomId = null;
        private Set<UserDetails> details = new HashSet<>();

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
        public @NotNull Builder setDetails(@NotNull Set<UserDetails> details) {
            this.details = details;
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder appendDetail(@NotNull UserDetails detail) {
            details.add(detail);
            return this;
        }

        public @NotNull GetAllUsersFromRoom build() {
            if (nodeId == null) {
                throw new NullPointerException("Node id is null");
            }
            if (roomId == null) {
                throw new NullPointerException("Room id is null");
            }

            return new GetAllUsersFromRoom(nodeId, roomId, Collections.unmodifiableSet(details));
        }
    }
}
