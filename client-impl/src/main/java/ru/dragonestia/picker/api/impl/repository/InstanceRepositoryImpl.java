package ru.dragonestia.picker.api.impl.repository;

import ru.dragonestia.picker.api.impl.util.RestTemplate;
import ru.dragonestia.picker.api.model.entity.EntityId;
import ru.dragonestia.picker.api.model.instance.Instance;
import ru.dragonestia.picker.api.model.instance.InstanceId;
import ru.dragonestia.picker.api.model.instance.type.PickingMethod;
import ru.dragonestia.picker.api.repository.InstanceRepository;
import ru.dragonestia.picker.api.repository.response.ResponseObject;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class InstanceRepositoryImpl implements InstanceRepository {

    private final RestTemplate restTemplate;

    public InstanceRepositoryImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<InstanceId> allInstancesIds() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Instance getInstance(InstanceId id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Map<InstanceId, Instance> getInstances(Collection<InstanceId> ids) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void createInstance(InstanceId id, PickingMethod method, boolean persist) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteInstance(InstanceId id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteInstances(Collection<InstanceId> ids) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public ResponseObject.PickedRoom pickRoom(InstanceId id, Collection<EntityId> entities, boolean dontReturnEntities) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
