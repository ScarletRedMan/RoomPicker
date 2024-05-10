package ru.dragonestia.picker.repository.impl;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.exception.AlreadyExistsException;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.model.instance.InstanceId;
import ru.dragonestia.picker.repository.impl.container.InstanceContainer;
import ru.dragonestia.picker.repository.impl.type.EntityTransaction;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ContainerRepository {

    private final Map<InstanceId, InstanceContainer> containers = new ConcurrentHashMap<>();

    private EntityTransaction.Listener transactionListener = transaction -> {};

    public void create(Instance instance) throws AlreadyExistsException {
        if (containers.containsKey(instance.getId())) {
            throw AlreadyExistsException.forInstance(instance.getId());
        }

        var container = new InstanceContainer(instance, transactionListener);
        containers.put(instance.getId(), container);
    }

    public void remove(InstanceId id) {
        containers.remove(id);
    }

    public @NotNull Optional<InstanceContainer> findById(InstanceId id) {
        return Optional.ofNullable(containers.get(id));
    }

    public @NotNull Collection<InstanceContainer> all() {
        return containers.values();
    }

    public void setTransactionListener(@NotNull EntityTransaction.Listener transactionListener) {
        this.transactionListener = transactionListener;
    }
}
