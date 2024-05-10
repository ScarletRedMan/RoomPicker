package ru.dragonestia.picker.repository.impl;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.api.exception.NodeAlreadyExistException;
import ru.dragonestia.picker.model.node.Node;
import ru.dragonestia.picker.repository.impl.container.NodeContainer;
import ru.dragonestia.picker.repository.impl.type.UserTransaction;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ContainerRepository {

    private final Map<String, NodeContainer> containers = new ConcurrentHashMap<>();

    private UserTransaction.Listener transactionListener = transaction -> {};

    public void create(Node node) throws NodeAlreadyExistException {
        if (containers.containsKey(node.getIdentifier())) {
            throw new NodeAlreadyExistException(node.getIdentifier());
        }

        var container = new NodeContainer(node, transactionListener);
        containers.put(node.getIdentifier(), container);
    }

    public void remove(@NotNull String nodeId) {
        containers.remove(nodeId);
    }

    public @NotNull Optional<NodeContainer> findById(@NotNull String nodeId) {
        return Optional.ofNullable(containers.get(nodeId));
    }

    public @NotNull Collection<NodeContainer> all() {
        return containers.values();
    }

    public void setTransactionListener(@NotNull UserTransaction.Listener transactionListener) {
        this.transactionListener = transactionListener;
    }
}
