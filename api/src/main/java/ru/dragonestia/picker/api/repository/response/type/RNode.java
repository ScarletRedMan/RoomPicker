package ru.dragonestia.picker.api.repository.response.type;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.dragonestia.picker.api.repository.response.type.type.PickingMode;
import ru.dragonestia.picker.api.repository.details.NodeDetails;

import java.util.HashMap;
import java.util.Map;

@Schema(title = "Node")
public class RNode {

    @Schema(description = "Node identifier", example = "test-node")
    private String id;

    @Schema(description = "Picking mode method for users between rooms", example = "LEAST_PICKED")
    private PickingMode mode;

    @Schema(description = "Additional data requested (Key-Value)")
    private Map<NodeDetails, String> details;

    private RNode() {}

    public RNode(String id, PickingMode mode) {
        this.id = id;
        this.mode = mode;
        this.details = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public PickingMode getMode() {
        return mode;
    }

    public void putDetail(NodeDetails detail, String value) {
        details.put(detail, value);
    }

    public String getDetails(NodeDetails detail) {
        return details.get(detail);
    }

    public Map<NodeDetails, String> getDetails() {
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
        if (object instanceof RNode other) {
            return id.equals(other.id);
        }
        return false;
    }
}
