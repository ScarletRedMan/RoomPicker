package ru.dragonestia.picker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.controller.response.NodeDetailsResponse;
import ru.dragonestia.picker.controller.response.NodeListResponse;
import ru.dragonestia.picker.controller.response.NodeRegisterResponse;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.type.PickingMode;
import ru.dragonestia.picker.service.NodeService;
import ru.dragonestia.picker.util.NamingValidator;

@RestController
@RequestMapping("/nodes")
@RequiredArgsConstructor
public class NodeController {

    private final NodeService nodeService;

    @GetMapping
    NodeListResponse allNodes() {
        return new NodeListResponse(nodeService.all());
    }

    @PostMapping
    NodeRegisterResponse registerNode(@RequestParam(name = "nodeId") String nodeId,
                                      @RequestParam(name = "method") PickingMode method) {

        try {
            nodeService.create(new Node(nodeId, method));
        } catch (IllegalArgumentException ex) {
            return new NodeRegisterResponse(false, ex.getMessage());
        } catch (Error error) {
            new NodeRegisterResponse(false, error.getMessage());
        }

        return new NodeRegisterResponse(true, "");
    }

    @GetMapping("/{nodeId}")
    ResponseEntity<NodeDetailsResponse> nodeDetails(@PathVariable("nodeId") String nodeId) {
        if (!NamingValidator.validateNodeId(nodeId)) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(404));
        }

        var nodeOpt = nodeService.find(nodeId);
        return nodeOpt.map(node -> ResponseEntity.ok(new NodeDetailsResponse(node)))
                .orElseGet(() -> new ResponseEntity<>(HttpStatusCode.valueOf(404)));
    }

    @DeleteMapping("/{nodeId}")
    ResponseEntity<?> removeNode(@PathVariable("nodeId") String nodeId) {
        if (!NamingValidator.validateNodeId(nodeId)) {
            return ResponseEntity.ok().build();
        }

        var nodeOpt = nodeService.find(nodeId);
        nodeOpt.ifPresent(nodeService::remove);

        return ResponseEntity.ok().build();
    }
}
