package ru.dragonestia.picker.api.repository.request.user;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.repository.type.NodeIdentifier;
import ru.dragonestia.picker.api.repository.type.RoomIdentifier;
import ru.dragonestia.picker.api.repository.type.UserIdentifier;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class LinkUsersWithRoom {

    private final String nodeId;
    private final String roomId;
    private final Set<String> users;
    private final boolean ignoreSlotLimitation;

    private LinkUsersWithRoom(String nodeId, String roomId, Set<String> users, boolean ignoreSlotLimitation) {
        this.nodeId = nodeId;
        this.roomId = roomId;
        this.users = users;
        this.ignoreSlotLimitation = ignoreSlotLimitation;
    }

    public @NotNull String getNodeId() {
        return nodeId;
    }

    public @NotNull String getRoomId() {
        return roomId;
    }

    public @NotNull Set<String> getUsers() {
        return users;
    }

    public boolean ignoreSlotLimitation() {
        return ignoreSlotLimitation;
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String nodeId = null;
        private String roomId = null;
        private Set<UserIdentifier> users = new HashSet<>();
        private boolean ignoreSlotLimitation = false;

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
        public @NotNull Builder setUsers(@NotNull Set<UserIdentifier> users) {
            this.users = users;
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder appendUser(@NotNull UserIdentifier user) {
            users.add(user);
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder setIgnoreSlotLimitation(boolean value) {
            ignoreSlotLimitation = value;
            return this;
        }

        public @NotNull LinkUsersWithRoom build() {
            if (nodeId == null) {
                throw new NullPointerException("Node id is null");
            }
            if (roomId == null) {
                throw new NullPointerException("Room id is null");
            }

            return new LinkUsersWithRoom(nodeId,
                    roomId,
                    users.stream().map(o -> o.getValue()).collect(Collectors.toSet()),
                    ignoreSlotLimitation);
        }
    }
}
