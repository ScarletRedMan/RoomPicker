package ru.dragonestia.picker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.api.exception.NodeNotFoundException;
import ru.dragonestia.picker.api.repository.response.type.type.PickingMode;
import ru.dragonestia.picker.api.repository.response.NodeDetailsResponse;
import ru.dragonestia.picker.api.repository.response.NodeListResponse;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.service.NodeService;
import ru.dragonestia.picker.service.RoomService;
import ru.dragonestia.picker.util.DetailsParser;
import ru.dragonestia.picker.util.NamingValidator;

import java.util.Arrays;

@RestController
@RequestMapping("/nodes")
@RequiredArgsConstructor
public class NodeController {

    private final NodeService nodeService;
    private final RoomService roomService;
    private final DetailsParser detailsParser;
    private final NamingValidator namingValidator;

    @GetMapping
    NodeListResponse allNodes(@RequestParam(name = "requiredDetails", required = false, defaultValue = "") String detailsSeq) {
        return new NodeListResponse(nodeService.getAllNodesWithDetailsResponse(detailsParser.parseNodeDetails(detailsSeq)));
    }

    @PostMapping
    ResponseEntity<?> registerNode(@RequestParam(name = "nodeId") String nodeId,
                                      @RequestParam(name = "method") PickingMode method) {

        nodeService.create(new Node(nodeId, method));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{nodeId}")
    ResponseEntity<NodeDetailsResponse> nodeDetails(@PathVariable("nodeId") String nodeId) {
        namingValidator.validateNodeId(nodeId);

        return nodeService.find(nodeId)
                .map(node -> ResponseEntity.ok(new NodeDetailsResponse(node.toResponseObject())))
                .orElseThrow(() -> new NodeNotFoundException(nodeId));
    }

    @DeleteMapping("/{nodeId}")
    ResponseEntity<?> removeNode(@PathVariable("nodeId") String nodeId) {
        namingValidator.validateNodeId(nodeId);

        nodeService.find(nodeId).ifPresent(nodeService::remove);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{nodeId}/pick")
    ResponseEntity<?> pickRoom(@PathVariable("nodeId") String nodeId,
                               @RequestParam(name = "userIds") String userIds) {

        namingValidator.validateNodeId(nodeId);

        var node = nodeService.find(nodeId).orElseThrow(() -> new NodeNotFoundException(nodeId));
        var users = namingValidator.validateUserIds(Arrays.stream(userIds.split(",")).toList());
        var room = roomService.pickAvailable(node, users);

        return ResponseEntity.ok(room); // TODO: make other json schema
    }
}
