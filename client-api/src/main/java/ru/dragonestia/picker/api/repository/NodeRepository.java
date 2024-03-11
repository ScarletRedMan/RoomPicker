package ru.dragonestia.picker.api.repository;

import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.model.node.INode;
import ru.dragonestia.picker.api.model.node.NodeDefinition;
import ru.dragonestia.picker.api.repository.request.node.FindNodeById;
import ru.dragonestia.picker.api.repository.request.node.GetAllNodes;
import ru.dragonestia.picker.api.repository.request.node.RemoveNodesByIds;

import java.util.List;
import java.util.Optional;

public interface NodeRepository {

    @NotNull List<INode> allNodes(@NotNull GetAllNodes request);

    @NotNull Optional<INode> findNodeById(@NotNull FindNodeById request);

    void removeNodesById(@NotNull RemoveNodesByIds removeNodesByIds);

    void removeNode(@NotNull INode node);

    void saveNode(@NotNull NodeDefinition definition);
}
