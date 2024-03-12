package ru.dragonestia.picker.api.impl.repository;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.impl.util.RestTemplate;
import ru.dragonestia.picker.api.model.node.INode;
import ru.dragonestia.picker.api.model.node.NodeDefinition;
import ru.dragonestia.picker.api.repository.NodeRepository;
import ru.dragonestia.picker.api.repository.request.node.FindNodeById;
import ru.dragonestia.picker.api.repository.request.node.GetAllNodes;
import ru.dragonestia.picker.api.repository.request.node.RemoveNodesByIds;
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
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public @NotNull Optional<INode> findNodeById(@NotNull FindNodeById data) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void removeNodesById(@NotNull RemoveNodesByIds data) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void removeNode(@NotNull INode node) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void saveNode(@NotNull NodeDefinition definition) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public @NotNull PickedRoomResponse pickRoom(@NotNull NodeIdentifier identifier, @NotNull Set<UserIdentifier> users) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
