package ru.dragonestia.picker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.controller.response.ResponseObject;
import ru.dragonestia.picker.exception.DoesNotExistsException;
import ru.dragonestia.picker.model.entity.EntityId;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.model.instance.InstanceId;
import ru.dragonestia.picker.model.instance.type.PickingMethod;
import ru.dragonestia.picker.service.EntityService;
import ru.dragonestia.picker.service.InstanceService;
import ru.dragonestia.picker.service.RoomService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/instances")
@RequiredArgsConstructor
public class InstanceController {

    private final InstanceService instanceService;
    private final RoomService roomService;
    private final EntityService entityService;

    @GetMapping
    List<String> listInstances() {
        return instanceService.all().stream().map(instance -> instance.getId().getValue()).toList();
    }

    @GetMapping("/target/{instanceId}")
    ResponseObject.Instance targetInstanceDetails(@PathVariable String instanceId) {
        var id = InstanceId.of(instanceId);
        return instanceService.find(id)
                .map(ResponseObject.Instance::of)
                .orElseThrow(() -> DoesNotExistsException.forInstance(id));
    }

    @GetMapping("/list")
    List<ResponseObject.Instance> listInstancesDetails(@RequestParam List<String> id) {
        return id.stream().map(this::targetInstanceDetails).toList();
    }

    @PostMapping
    ResponseEntity<Void> createInstance(@RequestParam String instanceId,
                                        @RequestParam PickingMethod method,
                                        @RequestParam(defaultValue = "false") boolean persist) {
        instanceService.create(new Instance(InstanceId.of(instanceId), method, persist));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/target/{instanceId}")
    ResponseEntity<Void> deleteInstance(@PathVariable String instanceId) {
        instanceService.remove(InstanceId.of(instanceId));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/list")
    ResponseEntity<Void> deleteInstances(@RequestParam List<String> id) {
        for (var instanceId: id) {
            deleteInstance(instanceId);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/target/{instanceId}/pick")
    ResponseObject.PickedRoom pickRoom(@PathVariable String instanceId,
                                       @RequestParam(defaultValue = "false") boolean dontReturnEntities,
                                       @RequestBody List<String> entities) {
        var room = roomService.pick(InstanceId.of(instanceId), entities.stream().map(EntityId::of).collect(Collectors.toSet()));
        List<String> returnEntities = new ArrayList<>();
        if (!dontReturnEntities) {
            for (var entity: entityService.getRoomEntities(room)) {
                returnEntities.add(entity.getId().getValue());
            }
        }
        return new ResponseObject.PickedRoom(ResponseObject.Room.of(room), returnEntities);
    }
}
