package ru.dragonestia.picker.api.impl.repository;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.exception.NodeNotFoundException;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.impl.util.EnumUtils;
import ru.dragonestia.picker.api.impl.util.RestTemplate;
import ru.dragonestia.picker.api.impl.util.type.HttpMethod;
import ru.dragonestia.picker.api.model.node.INode;
import ru.dragonestia.picker.api.model.node.NodeDefinition;
import ru.dragonestia.picker.api.repository.NodeRepository;
import ru.dragonestia.picker.api.repository.request.node.FindNodeById;
import ru.dragonestia.picker.api.repository.request.node.GetAllNodes;
import ru.dragonestia.picker.api.repository.request.node.RemoveNodesByIds;
import ru.dragonestia.picker.api.repository.response.NodeDetailsResponse;
import ru.dragonestia.picker.api.repository.response.NodeListResponse;
import ru.dragonestia.picker.api.repository.response.PickedRoomResponse;
import ru.dragonestia.picker.api.repository.type.NodeIdentifier;
import ru.dragonestia.picker.api.repository.type.UserIdentifier;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class NodeRepositoryImpl implements NodeRepository {

    private final RestTemplate rest;

    @Internal
    public NodeRepositoryImpl(RoomPickerClient client) {
        rest = client.getRestTemplate();
    }

    @Override
    public @NotNull List<INode> allNodes(@NotNull GetAllNodes data) {
        return rest.query("/nodes", HttpMethod.GET, NodeListResponse.class, params -> {
            params.put("requiredDetails", EnumUtils.enumSetToString(data.getDetails()));
        }).nodes().stream().map(node -> (INode) node).toList();
    }

    @Override
    public @NotNull Optional<INode> findNodeById(@NotNull FindNodeById data) {
        try {
            var response = rest.query("/nodes/" + data.getId(), HttpMethod.GET, NodeDetailsResponse.class, params -> {
                params.put("requiredDetails", EnumUtils.enumSetToString(data.getDetails()));
            });
            return Optional.of(response.node());
        } catch (NodeNotFoundException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void removeNodesById(@NotNull RemoveNodesByIds data) {
        if (data.getNodeIds().isEmpty()) return;

        rest.query("/nodes", HttpMethod.DELETE, params -> {
            params.put("toDelete", String.join(",", data.getNodeIds()));
        });
    }

    @Override
    public void removeNode(@NotNull INode node) {
        rest.query("/nodes/" + node.getIdentifier(), HttpMethod.DELETE, params -> {});
    }

    @Override
    public void saveNode(@NotNull NodeDefinition definition) {
        rest.query("/nodes", HttpMethod.POST, params -> {
            params.put("nodeId", definition.getIdentifier());
            params.put("method", definition.getPickingMethod().name());
            params.put("persist", Boolean.toString(definition.isPersist()));
        });
    }

    @Override
    public @NotNull PickedRoomResponse pickRoom(@NotNull NodeIdentifier identifier, @NotNull Set<UserIdentifier> users) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
