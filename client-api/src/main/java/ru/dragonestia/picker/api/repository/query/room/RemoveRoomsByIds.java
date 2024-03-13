package ru.dragonestia.picker.api.repository.query.room;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.repository.type.NodeIdentifier;
import ru.dragonestia.picker.api.repository.type.RoomIdentifier;

import java.util.Set;
import java.util.stream.Collectors;

public class RemoveRoomsByIds {

    private final String nodeId;
    private final Set<String> roomsIds;

    private RemoveRoomsByIds(String nodeId, Set<String> roomIds) {
        this.nodeId = nodeId;
        this.roomsIds = roomIds;
    }

    public @NotNull String getNodeId() {
        return nodeId;
    }

    public @NotNull Set<String> getRoomsIds() {
        return roomsIds;
    }

    public static @NotNull RemoveRoomsByIds just(@NotNull NodeIdentifier nodeId, @NotNull RoomIdentifier roomId) {
        return RemoveRoomsByIds.builder().setNodeId(nodeId).appendRoomId(roomId).build();
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String nodeId = null;
        private Set<RoomIdentifier> roomsIds;

        private Builder() {}

        @Contract("_ -> this")
        public @NotNull Builder setNodeId(@NotNull NodeIdentifier identifier) {
            nodeId = identifier.getValue();
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder setRoomsIds(@NotNull Set<RoomIdentifier> roomIds) {
            this.roomsIds = roomIds;
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder appendRoomId(@NotNull RoomIdentifier roomId) {
            roomsIds.add(roomId);
            return this;
        }

        public @NotNull RemoveRoomsByIds build() {
            if (nodeId == null) {
                throw new NullPointerException("Node id is null");
            }

            return new RemoveRoomsByIds(nodeId, roomsIds.stream().map(o -> o.getValue()).collect(Collectors.toSet()));
        }
    }
}
