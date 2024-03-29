package ru.dragonestia.picker.api.model.room;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.dragonestia.picker.api.repository.type.RoomPath;

import java.beans.Transient;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Schema(title = "Room (Short)")
public class ShortResponseRoom implements IRoom {

    @Schema(description = "Room identifier", example = "test-room")
    private String id;

    @Schema(description = "Node identifier", example = "test-node")
    private String nodeId;

    @Schema(description = "Slots for users. -1 - unlimited slots", example = "25")
    private int slots;

    @Schema(description = "Does picking skip this room?")
    private boolean locked;

    @Schema(description = "Additional data requested (Key-Value)")
    private Map<RoomDetails, String> details;

    @Internal
    public ShortResponseRoom() {}

    public ShortResponseRoom(String id, String nodeId, int slots, boolean locked) {
        this.id = id;
        this.nodeId = nodeId;
        this.slots = slots;
        this.locked = locked;
        this.details = new HashMap<>();
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
        return null;
    }

    @Override
    public @Nullable String getPayload() {
        return null;
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
        return Objects.hash(id, nodeId);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof ShortResponseRoom other) {
            return id.equals(other.id) && nodeId.equals(other.nodeId);
        }
        return false;
    }

    @Override
    public String toString() {
        return "[ShortResponseRoom id='%s' nodeId='%s' slots=%s]".formatted(id, nodeId, slots);
    }
}
