package ru.dragonestia.picker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.controller.response.ResponseObject;
import ru.dragonestia.picker.model.instance.type.PickingMethod;

import java.util.List;

@RestController
@RequestMapping("/instances")
@RequiredArgsConstructor
public class InstanceController {

    @GetMapping
    List<String> listInstances() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @GetMapping("/target/{instanceId}")
    ResponseObject.Instance targetInstanceDetails(@PathVariable String instanceId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @GetMapping("/list")
    List<ResponseObject.Instance> listInstancesDetails(@RequestParam List<String> id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PostMapping
    ResponseEntity<Void> createInstance(@RequestParam String instanceId,
                                        @RequestParam PickingMethod method,
                                        @RequestParam(defaultValue = "false") boolean persist) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @DeleteMapping("/target/{instanceId}")
    ResponseEntity<Void> deleteInstance(@PathVariable String instanceId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @DeleteMapping("/list")
    ResponseEntity<Void> deleteInstances(@RequestParam List<String> id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PostMapping("/target/{instanceId}/pick")
    ResponseObject.PickedRoom pickRoom(@PathVariable String instanceId, @RequestBody List<String> entities) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
