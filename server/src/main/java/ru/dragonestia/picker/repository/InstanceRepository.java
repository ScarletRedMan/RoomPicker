package ru.dragonestia.picker.repository;

import ru.dragonestia.picker.api.exception.NodeAlreadyExistException;
import ru.dragonestia.picker.model.instance.Instance;

import java.util.List;
import java.util.Optional;

public interface InstanceRepository {

    void create(Instance instance) throws NodeAlreadyExistException;

    void delete(Instance instance);

    Optional<Instance> findById(String nodeId);

    List<Instance> all();
}
