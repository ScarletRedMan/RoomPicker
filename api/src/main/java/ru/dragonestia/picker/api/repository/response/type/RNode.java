package ru.dragonestia.picker.api.repository.response.type;

import ru.dragonestia.picker.api.repository.response.type.type.PickingMode;
import ru.dragonestia.picker.api.repository.details.NodeDetails;

import java.util.HashMap;
import java.util.Map;

public class RNode {

    private String id;
    private PickingMode mode;
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

    public String getDetails(NodeDetails detail, String value) {
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
