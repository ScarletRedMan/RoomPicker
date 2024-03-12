package ru.dragonestia.picker.api.model.room;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.dragonestia.picker.api.repository.type.RoomPath;

import java.beans.Transient;
import java.util.HashMap;
import java.util.Map;

@Schema(title = "Room")
public class ResponseRoom implements IRoom {

    @Schema(description = "Room identifier", example = "test-room")
    private String id;

    @Schema(description = "Node identifier", example = "test-node")
    private String nodeId;

    @Schema(description = "Slots for users. -1 - unlimited slots", example = "25")
    private int slots;

    @Schema(description = "Does picking skip this room?")
    private boolean locked;

    @Schema(description = "Payload. Some data")
    private String payload;

    @Schema(description = "Additional data requested (Key-Value)")
    private Map<RoomDetails, String> details;

    public ResponseRoom() {}

    public ResponseRoom(@NotNull String id, @NotNull String nodeId, int slots, boolean locked, @NotNull String payload) {
        this.id = id;
        this.nodeId = nodeId;
        this.slots = slots;
        this.locked = locked;
        this.payload = payload;
        details = new HashMap<>();
    }

    @Override
    public @NotNull String getIdentifier() {
        return id;
    }

    @Override
    public @NotNull String getNodeIdentifier() {
        return nodeId;
    }

    @Transient
    @Override
    public @NotNull RoomPath getPath() {
        return IRoom.super.getPath();
    }

    @Override
    public int getMaxSlots() {
        return slots;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    @Override
    public @Nullable Boolean isPersist() {
        var val = getDetail(RoomDetails.PERSIST);
        return val == null? null : "true".equals(val);
    }

    @Override
    public @Nullable String getPayload() {
        return payload;
    }

    @Transient
    @Override
    public boolean hasUnlimitedSlots() {
        return IRoom.super.hasUnlimitedSlots();
    }

    @Override
    public @Nullable String getDetail(@NotNull RoomDetails detail) {
        return details.get(detail);
    }

    public void putDetail(@NotNull RoomDetails detail, @NotNull String value) {
        details.put(detail, value);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof ResponseRoom other) {
            return id.equals(other.id);
        }
        return false;
    }

    @Override
    public String toString() {
        return "[ResponseRoom id='%s' nodeId='%s' slots=%s payload.len=%s]".formatted(id, nodeId, slots, payload.length());
    }
}
