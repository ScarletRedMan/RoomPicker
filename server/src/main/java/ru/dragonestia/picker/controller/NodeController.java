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
@RequestMapping("/nodes")
@RequiredArgsConstructor
public class NodeController {

    @Operation(summary = "Get all nodes")
    @GetMapping
    NodeListResponse allNodes() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Register new node")
    @PostMapping
    ResponseEntity<?> registerNode(
            @Parameter(description = "Instance identifier") @RequestParam(name = "nodeId") String nodeId,
            @Parameter(description = "Picking method method") @RequestParam(name = "method") PickingMethod method,
            @Parameter(description = "Save node") @RequestParam(name = "persist", required = false, defaultValue = "false") boolean persist
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Get node details")
    @GetMapping("/{nodeId}")
    ResponseEntity<NodeDetailsResponse> nodeDetails(
            @Parameter(description = "Instance identifier") @PathVariable("nodeId") String nodeId
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Unregister node")
    @DeleteMapping("/{nodeId}")
    ResponseEntity<?> removeNode(
            @Parameter(description = "Instance identifier") @PathVariable("nodeId") String nodeId
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Operation(summary = "Pick node for users")
    @PostMapping("/{nodeId}/pick")
    ResponseEntity<PickedRoomResponse> pickRoom(
            @Parameter(description = "Instance identifier") @PathVariable("nodeId") String nodeId,
            @RequestBody String userIds
    ) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
