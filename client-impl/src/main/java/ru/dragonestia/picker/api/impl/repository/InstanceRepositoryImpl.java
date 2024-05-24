package ru.dragonestia.picker.api.impl.repository;

import ru.dragonestia.picker.api.impl.util.RestTemplate;
import ru.dragonestia.picker.api.impl.util.type.HttpMethod;
import ru.dragonestia.picker.api.model.entity.EntityId;
import ru.dragonestia.picker.api.model.instance.Instance;
import ru.dragonestia.picker.api.model.instance.InstanceId;
import ru.dragonestia.picker.api.model.instance.type.PickingMethod;
import ru.dragonestia.picker.api.repository.InstanceRepository;
import ru.dragonestia.picker.api.repository.response.ResponseObject;

import java.util.*;

public class InstanceRepositoryImpl implements InstanceRepository {

    private final RestTemplate rest;

    public InstanceRepositoryImpl(RestTemplate rest) {
        this.rest = rest;
    }

    @Override
    public List<InstanceId> allInstancesIds() {
        return Arrays.stream(rest.query("/instances", HttpMethod.GET, String[].class))
                .map(InstanceId::of)
                .toList();
    }

    @Override
    public Instance getInstance(InstanceId id) {
        return rest.query("/instances/target/" + id.getValue(), HttpMethod.GET, ResponseObject.RInstance.class).convert();
    }

    @Override
    public Map<InstanceId, Instance> getInstances(Collection<InstanceId> ids) {
        var map = new HashMap<InstanceId, Instance>();
        Arrays.stream(rest.query("/instances/target/list", HttpMethod.GET, ResponseObject.RInstance[].class, params -> {
            params.put("id", String.join(",", ids.stream().map(InstanceId::getValue).toList()));
        })).map(ResponseObject.RInstance::convert).forEach(instance -> map.put(instance.id(), instance));
        return map;
    }

    @Override
    public void createInstance(InstanceId id, PickingMethod method, boolean persist) {
        rest.query("/instances", HttpMethod.POST, params -> {
            params.put("instanceId", id.getValue());
            params.put("method", method.name());
            params.put("persist", Boolean.toString(persist
            ));
        });
    }

    @Override
    public void deleteInstance(InstanceId id) {
        rest.query("/instances/target/" + id.getValue(), HttpMethod.DELETE);
    }

    @Override
    public void deleteInstances(Collection<InstanceId> ids) {
        rest.query("/instances/list", HttpMethod.DELETE, params -> {
            params.put("id", String.join(",", ids.stream().map(InstanceId::getValue).toList()));
        });
    }

    @Override
    public ResponseObject.PickedRoom pickRoom(InstanceId id, Collection<EntityId> entities, boolean dontReturnEntities) {
        return rest.queryPostWithBody("/instances/target/" + id.getValue() + "/pick", ResponseObject.PickedRoom.class, params -> {
            params.put("dontReturnEntities", Boolean.toString(dontReturnEntities));
        }, String.join(",", entities.stream().map(EntityId::getValue).toList()));
    }
}
