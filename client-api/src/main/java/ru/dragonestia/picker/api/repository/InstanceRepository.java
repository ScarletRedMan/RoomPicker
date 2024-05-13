package ru.dragonestia.picker.api.repository;

import ru.dragonestia.picker.api.model.entity.EntityId;
import ru.dragonestia.picker.api.model.instance.Instance;
import ru.dragonestia.picker.api.model.instance.InstanceId;
import ru.dragonestia.picker.api.model.instance.type.PickingMethod;
import ru.dragonestia.picker.api.repository.response.ResponseObject;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface InstanceRepository {

    List<InstanceId> allInstancesIds();

    Instance getInstance(InstanceId id);

    Map<InstanceId, Instance> getInstances(Collection<InstanceId> ids);

    void createInstance(InstanceId id, PickingMethod method, boolean persist);

    void deleteInstance(InstanceId id);

    default void deleteInstance(Instance instance) {
        deleteInstance(instance.id());
    }

    void deleteInstances(Collection<InstanceId> ids);

    default void justDeleteInstances(Collection<Instance> instances) {
        deleteInstances(instances.stream().map(Instance::id).toList());
    }

    default ResponseObject.PickedRoom pickRoom(InstanceId id, Collection<EntityId> entities) {
        return pickRoom(id, entities, false);
    }

    ResponseObject.PickedRoom pickRoom(InstanceId id, Collection<EntityId> entities, boolean dontReturnEntities);
}
