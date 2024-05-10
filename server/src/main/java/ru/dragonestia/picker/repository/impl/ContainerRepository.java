package ru.dragonestia.picker.repository.impl;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.api.exception.NodeAlreadyExistException;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.repository.impl.container.InstanceContainer;
import ru.dragonestia.picker.repository.impl.type.UserTransaction;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ContainerRepository {

    private final Map<String, InstanceContainer> containers = new ConcurrentHashMap<>();

    private UserTransaction.Listener transactionListener = transaction -> {};

    public void create(Instance instance) throws NodeAlreadyExistException {
        if (containers.containsKey(instance.getIdentifier())) {
            throw new NodeAlreadyExistException(instance.getIdentifier());
        }

        var container = new InstanceContainer(instance, transactionListener);
        containers.put(instance.getIdentifier(), container);
    }

    public void remove(@NotNull String nodeId) {
        containers.remove(nodeId);
    }

    public @NotNull Optional<InstanceContainer> findById(@NotNull String nodeId) {
        return Optional.ofNullable(containers.get(nodeId));
    }

    public @NotNull Collection<InstanceContainer> all() {
        return containers.values();
    }

    public void setTransactionListener(@NotNull UserTransaction.Listener transactionListener) {
        this.transactionListener = transactionListener;
    }
}
