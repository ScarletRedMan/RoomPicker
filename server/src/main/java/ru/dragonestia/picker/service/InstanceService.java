package ru.dragonestia.picker.service;

import org.springframework.security.access.prepost.PreAuthorize;
import ru.dragonestia.picker.exception.AlreadyExistsException;
import ru.dragonestia.picker.exception.InvalidIdentifierException;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.model.instance.InstanceId;

import java.util.List;
import java.util.Optional;

public interface InstanceService {

    @PreAuthorize("hasRole('NODE_MANAGEMENT')")
    void create(Instance instance) throws InvalidIdentifierException, AlreadyExistsException;

    @PreAuthorize("hasRole('NODE_MANAGEMENT')")
    void remove(Instance instance);

    List<Instance> all();

    Optional<Instance> find(InstanceId id);
}
