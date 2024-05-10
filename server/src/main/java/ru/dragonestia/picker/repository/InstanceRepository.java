package ru.dragonestia.picker.repository;

import ru.dragonestia.picker.exception.AlreadyExistsException;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.model.instance.InstanceId;

import java.util.List;
import java.util.Optional;

public interface InstanceRepository {

    void create(Instance instance) throws AlreadyExistsException;

    void delete(InstanceId id);

    Optional<Instance> findById(InstanceId id);

    List<Instance> all();
}
