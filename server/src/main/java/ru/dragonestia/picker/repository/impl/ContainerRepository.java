package ru.dragonestia.picker.repository.impl;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.api.exception.InstanceAlreadyExistException;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.repository.impl.container.InstanceContainer;
import ru.dragonestia.picker.repository.impl.type.EntityTransaction;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ContainerRepository {

    private final Map<String, InstanceContainer> containers = new ConcurrentHashMap<>();

    private EntityTransaction.Listener transactionListener = transaction -> {};

    public void create(Instance instance) throws InstanceAlreadyExistException {
        if (containers.containsKey(instance.getIdentifier())) {
            throw new InstanceAlreadyExistException(instance.getIdentifier());
        }

        var container = new InstanceContainer(instance, transactionListener);
        containers.put(instance.getIdentifier(), container);
    }

    public void remove(@NotNull String instanceId) {
        containers.remove(instanceId);
    }

    public @NotNull Optional<InstanceContainer> findById(@NotNull String instanceId) {
        return Optional.ofNullable(containers.get(instanceId));
    }

    public @NotNull Collection<InstanceContainer> all() {
        return containers.values();
    }

    public void setTransactionListener(@NotNull EntityTransaction.Listener transactionListener) {
        this.transactionListener = transactionListener;
    }
}
