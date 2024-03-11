package ru.dragonestia.picker.api.repository.response.type;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.dragonestia.picker.api.model.room.RoomDetails;

import java.beans.Transient;
import java.util.HashMap;
import java.util.Map;

@Schema(title = "Room")
public class RRoom {

    public final static int INFINITE_SLOTS = -1;

    @Schema(description = "Room identifier", example = "test-room")
    private String id;

    @Schema(description = "Node identifier", example = "test-node")
    private String nodeId;

    @Schema(description = "Slots for users. -1 - unlimited slots", example = "25")
    private int slots;

    @Schema(description = "Payload. Some data")
    private String payload;

    @Schema(description = "Does picking skip this room?")
    private boolean locked = false;

    @Schema(description = "Additional data requested (Key-Value)")
    private Map<RoomDetails, String> details;

    private RRoom() {}

    public RRoom(String id, String nodeId, int slots, String payload) {
        this.id = id;
        this.nodeId = nodeId;
        this.slots = slots;
        this.payload = payload;
        this.details = new HashMap<>();
    }

    public RRoom(String id, RNode node, int limit, String payload) {
        this(id, node.getId(), limit, payload);
    }

    public String getId() {
        return id;
    }

    public String getNodeId() {
        return nodeId;
    }

    public int getSlots() {
        return slots;
    }

    public String getPayload() {
        return payload;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean value) {
        locked = value;
    }

    @Transient
    public boolean isUnlimited() {
        return slots == INFINITE_SLOTS;
    }

    public void putDetail(RoomDetails detail, String value) {
        details.put(detail, value);
    }

    public String getDetail(RoomDetails detail) {
        return details.get(detail);
    }

    public Map<RoomDetails, String> getDetails() {
        return details;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof RRoom other) {
            return id.equals(other.id);
        }
        return false;
    }

    @Schema(title = "Room (Short)")
    public record Short(
            @Schema(description = "Room identifier", example = "test-room") String id,
            @Schema(description = "Node identifier", example = "test-node") String nodeId,
            @Schema(description = "Slots for users. -1 - unlimited slots", example = "25") int slots,
            @Schema(description = "Does picking skip this room?") boolean locked,
            @Schema(description = "Additional data requested (Key-Value)") Map<RoomDetails, String> details
    ) {}
}
