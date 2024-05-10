package ru.dragonestia.picker.api.repository.query.user;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.repository.type.NodeIdentifier;
import ru.dragonestia.picker.api.repository.type.RoomIdentifier;
import ru.dragonestia.picker.api.repository.type.EntityIdentifier;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UnlinkUsersFromRoom {

    private final String nodeId;
    private final String roomId;
    private final Set<String> users;

    private UnlinkUsersFromRoom(String nodeId, String roomId, Set<String> users) {
        this.nodeId = nodeId;
        this.roomId = roomId;
        this.users = users;
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

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String nodeId = null;
        private String roomId = null;
        private Set<EntityIdentifier> users = new HashSet<>();

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
        public @NotNull Builder setUsers(@NotNull Set<EntityIdentifier> users) {
            this.users = users;
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder appendUser(@NotNull EntityIdentifier user) {
            users.add(user);
            return this;
        }

        public @NotNull UnlinkUsersFromRoom build() {
            if (nodeId == null) {
                throw new NullPointerException("Node id is null");
            }
            if (roomId == null) {
                throw new NullPointerException("Room id is null");
            }

            return new UnlinkUsersFromRoom(nodeId,
                    roomId,
                    users.stream().map(o -> o.getValue()).collect(Collectors.toSet()));
        }
    }
}
