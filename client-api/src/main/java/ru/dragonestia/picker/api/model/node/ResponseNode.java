package ru.dragonestia.picker.api.model.node;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.Transient;
import java.util.HashMap;
import java.util.Map;

@Schema(title = "Node")
public class ResponseNode implements INode {

    @Schema(description = "Node identifier", example = "test-node")
    private String id;

    @Schema(description = "Picking method for users between rooms", example = "LEAST_PICKED")
    private PickingMethod method;

    @Schema(description = "Additional data requested (Key-Value)")
    private Map<NodeDetails, String> details;

    @Internal
    public ResponseNode() {}

    public ResponseNode(@NotNull String id, @NotNull PickingMethod pickingMethod) {
        this.id = id;
        this.method = pickingMethod;
        this.details = new HashMap<>();
    }

    @Override
    public @NotNull String getIdentifier() {
        return id;
    }

    @Override
    public @NotNull PickingMethod getPickingMethod() {
        return method;
    }

    @Override
    public @Nullable Boolean isPersist() {
        var val = getDetail(NodeDetails.PERSIST);
        return val == null? null : "true".equals(val);
    }

    @Override
    public @Nullable String getDetail(@NotNull NodeDetails detail) {
        return details.get(detail);
    }

    public void putDetail(@NotNull NodeDetails detail, @NotNull String value) {
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
        if (object instanceof ResponseNode other) {
            return id.equals(other.id);
        }
        return false;
    }

    @Override
    public String toString() {
        return "[ResponseNode id='%s' pickingMethod=%s]".formatted(id, method);
    }
}
