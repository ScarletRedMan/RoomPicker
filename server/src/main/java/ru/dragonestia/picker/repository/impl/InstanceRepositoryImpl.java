package ru.dragonestia.picker.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.exception.AlreadyExistsException;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.model.instance.InstanceId;
import ru.dragonestia.picker.repository.InstanceRepository;
import ru.dragonestia.picker.repository.impl.container.InstanceContainer;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InstanceRepositoryImpl implements InstanceRepository {

    private final ContainerRepository containerRepository;

    @Override
    public void create(Instance instance) throws AlreadyExistsException {
        containerRepository.create(instance);
    }

    @Override
    public void delete(InstanceId instanceId) {
        containerRepository.remove(instanceId);
    }

    @Override
    public Optional<Instance> findById(InstanceId id) {
        return containerRepository.findById(id).map(InstanceContainer::getInstance);
    }

    @Override
    public List<Instance> all() {
        return containerRepository.all().stream().map(InstanceContainer::getInstance).toList();
    }
}
