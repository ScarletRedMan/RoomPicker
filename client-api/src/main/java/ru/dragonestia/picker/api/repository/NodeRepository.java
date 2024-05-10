package ru.dragonestia.picker.api.repository;

import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.model.node.INode;
import ru.dragonestia.picker.api.model.node.NodeDefinition;
import ru.dragonestia.picker.api.repository.query.node.FindNodeById;
import ru.dragonestia.picker.api.repository.query.node.GetAllNodes;
import ru.dragonestia.picker.api.repository.query.node.RemoveNodesByIds;
import ru.dragonestia.picker.api.repository.response.PickedRoomResponse;
import ru.dragonestia.picker.api.repository.type.NodeIdentifier;
import ru.dragonestia.picker.api.repository.type.EntityIdentifier;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface NodeRepository {

    @NotNull List<INode> allNodes(@NotNull GetAllNodes request);

    @NotNull Optional<INode> findNodeById(@NotNull FindNodeById request);

    void removeNodesById(@NotNull RemoveNodesByIds removeNodesByIds);

    void removeNode(@NotNull INode node);

    void saveNode(@NotNull NodeDefinition definition);

    @NotNull PickedRoomResponse pickRoom(@NotNull NodeIdentifier identifier, @NotNull Set<EntityIdentifier> users);
}
