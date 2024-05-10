package ru.dragonestia.picker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.api.model.node.PickingMethod;
import ru.dragonestia.picker.api.repository.response.NodeDetailsResponse;
import ru.dragonestia.picker.api.repository.response.NodeListResponse;
import ru.dragonestia.picker.api.repository.response.PickedRoomResponse;

@Tag(name = "Nodes", description = "Instance management")
@RestController
@RequestMapping("/instances")
@RequiredArgsConstructor
public class InstanceController {

    @Operation(summary = "Get all nodes")
    @GetMapping
    NodeListResponse allInstances() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Register new node")
    @PostMapping
    ResponseEntity<?> registerInstance(
            @Parameter(description = "Instance identifier") @RequestParam(name = "instanceId") String instanceId,
            @Parameter(description = "Picking method method") @RequestParam(name = "method") PickingMethod method,
            @Parameter(description = "Save node") @RequestParam(name = "persist", required = false, defaultValue = "false") boolean persist
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Get node details")
    @GetMapping("/{instanceId}")
    ResponseEntity<NodeDetailsResponse> instanceDetails(
            @Parameter(description = "Instance identifier") @PathVariable("instanceId") String instanceId
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Unregister node")
    @DeleteMapping("/{instanceId}")
    ResponseEntity<?> removeInstance(
            @Parameter(description = "Instance identifier") @PathVariable("instanceId") String instanceId
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Pick node for users")
    @PostMapping("/{instanceId}/pick")
    ResponseEntity<PickedRoomResponse> pickRoom(
            @Parameter(description = "Instance identifier") @PathVariable("instanceId") String instanceId,
            @RequestBody String userIds
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
