package ru.dragonestia.picker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.api.exception.NodeNotFoundException;
import ru.dragonestia.picker.api.model.node.PickingMethod;
import ru.dragonestia.picker.api.repository.response.NodeDetailsResponse;
import ru.dragonestia.picker.api.repository.response.NodeListResponse;
import ru.dragonestia.picker.api.repository.response.PickedRoomResponse;
import ru.dragonestia.picker.api.repository.type.NodeIdentifier;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.service.NodeService;
import ru.dragonestia.picker.service.RoomService;
import ru.dragonestia.picker.util.DetailsParser;
import ru.dragonestia.picker.util.NamingValidator;

import java.util.Arrays;

@Tag(name = "Nodes", description = "Node management")
@RestController
@RequestMapping("/nodes")
@RequiredArgsConstructor
public class NodeController {

    private final NodeService nodeService;
    private final RoomService roomService;
    private final DetailsParser detailsParser;
    private final NamingValidator namingValidator;

    @Operation(summary = "Get all nodes")
    @GetMapping
    NodeListResponse allNodes(
            @RequestParam(name = "requiredDetails", required = false, defaultValue = "") String detailsSeq
    ) {
        return new NodeListResponse(nodeService.getAllNodesWithDetailsResponse(detailsParser.parseNodeDetails(detailsSeq)));
    }

    @Operation(summary = "Register new node")
    @PostMapping
    ResponseEntity<?> registerNode(
            @Parameter(description = "Node identifier") @RequestParam(name = "nodeId") String nodeId,
            @Parameter(description = "Picking method method") @RequestParam(name = "method") PickingMethod method,
            @Parameter(description = "Save node") @RequestParam(name = "persist", required = false, defaultValue = "false") boolean persist
    ) {
        nodeService.create(new Node(NodeIdentifier.of(nodeId), method, persist));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get node details")
    @GetMapping("/{nodeId}")
    ResponseEntity<NodeDetailsResponse> nodeDetails(
            @Parameter(description = "Node identifier") @PathVariable("nodeId") String nodeId
    ) {
        namingValidator.validateNodeId(nodeId);

        return nodeService.find(nodeId)
                .map(node -> ResponseEntity.ok(new NodeDetailsResponse(node.toResponseObject())))
                .orElseThrow(() -> new NodeNotFoundException(nodeId));
    }

    @Operation(summary = "Unregister node")
    @DeleteMapping("/{nodeId}")
    ResponseEntity<?> removeNode(
            @Parameter(description = "Node identifier") @PathVariable("nodeId") String nodeId
    ) {
        namingValidator.validateNodeId(nodeId);

        nodeService.find(nodeId).ifPresent(nodeService::remove);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Pick node for users")
    @PostMapping("/{nodeId}/pick")
    ResponseEntity<PickedRoomResponse> pickRoom(
            @Parameter(description = "Node identifier") @PathVariable("nodeId") String nodeId,
            @Parameter(description = "Users to add", example = "user1,user3,user3") @RequestParam(name = "userIds") String userIds
    ) {
        namingValidator.validateNodeId(nodeId);

        var node = nodeService.find(nodeId).orElseThrow(() -> new NodeNotFoundException(nodeId));
        var users = namingValidator.validateUserIds(Arrays.stream(userIds.split(",")).toList());
        var response = roomService.pickAvailable(node, users);

        return ResponseEntity.ok(response);
    }
}
