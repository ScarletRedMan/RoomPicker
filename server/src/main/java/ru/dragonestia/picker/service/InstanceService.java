package ru.dragonestia.picker.service;

import org.springframework.security.access.prepost.PreAuthorize;
import ru.dragonestia.picker.api.exception.InvalidInstanceIdentifierException;
import ru.dragonestia.picker.api.exception.InstanceAlreadyExistException;
import ru.dragonestia.picker.model.instance.Instance;

import java.util.List;
import java.util.Optional;

public interface InstanceService {

    @PreAuthorize("hasRole('NODE_MANAGEMENT')")
    void create(Instance instance) throws InvalidInstanceIdentifierException, InstanceAlreadyExistException;

    @PreAuthorize("hasRole('NODE_MANAGEMENT')")
    void remove(Instance instance);

    List<Instance> all();

    Optional<Instance> find(String nodeId);
}
