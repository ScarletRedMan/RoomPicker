package ru.dragonestia.picker.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.api.exception.NodeAlreadyExistException;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.repository.InstanceRepository;
import ru.dragonestia.picker.repository.impl.container.InstanceContainer;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InstanceRepositoryImpl implements InstanceRepository {

    private final ContainerRepository containerRepository;

    @Override
    public void create(Instance instance) throws NodeAlreadyExistException {
        containerRepository.create(instance);
    }

    @Override
    public void delete(Instance instance) {
        containerRepository.remove(instance.getIdentifier());
    }

    @Override
    public Optional<Instance> findById(String nodeId) {
        return containerRepository.findById(nodeId).map(InstanceContainer::getInstance);
    }

    @Override
    public List<Instance> all() {
        return containerRepository.all().stream().map(InstanceContainer::getInstance).toList();
    }
}
