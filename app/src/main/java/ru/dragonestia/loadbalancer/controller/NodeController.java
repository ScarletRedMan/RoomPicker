package ru.dragonestia.loadbalancer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.loadbalancer.controller.response.NodeDetailsResponse;
import ru.dragonestia.loadbalancer.controller.response.NodeListResponse;
import ru.dragonestia.loadbalancer.controller.response.NodeRegisterResponse;
import ru.dragonestia.loadbalancer.model.Node;
import ru.dragonestia.loadbalancer.model.type.LoadBalancingMethod;
import ru.dragonestia.loadbalancer.service.NodeService;
import ru.dragonestia.loadbalancer.util.NamingValidator;

@RestController
@RequestMapping("/nodes")
@RequiredArgsConstructor
public class NodeController {

    private final NodeService nodeService;

    @GetMapping
    NodeListResponse allNodes() {
        return new NodeListResponse(nodeService.allNodes());
    }

    @PostMapping
    NodeRegisterResponse registerNode(@RequestParam(name = "identifier") String identifier,
                                      @RequestParam(name = "method") LoadBalancingMethod method) {

        try {
            nodeService.createNode(new Node(identifier, method));
        } catch (IllegalArgumentException ex) {
            return new NodeRegisterResponse(false, ex.getMessage());
        } catch (Error error) {
            new NodeRegisterResponse(false, error.getMessage());
        }

        return new NodeRegisterResponse(true, "");
    }

    @GetMapping("/{identifier}")
    ResponseEntity<NodeDetailsResponse> nodeDetails(@PathVariable("identifier") String identifier) {
        if (!NamingValidator.validateNodeIdentifier(identifier)) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(404));
        }

        var nodeOpt = nodeService.findNode(identifier);
        return nodeOpt.map(node -> ResponseEntity.ok(new NodeDetailsResponse(node)))
                .orElseGet(() -> new ResponseEntity<>(HttpStatusCode.valueOf(404)));
    }

    @DeleteMapping("/{identifier}")
    ResponseEntity<?> removeNode(@PathVariable("identifier") String identifier) {
        if (!NamingValidator.validateNodeIdentifier(identifier)) {
            return ResponseEntity.ok().build();
        }

        var nodeOpt = nodeService.findNode(identifier);
        nodeOpt.ifPresent(nodeService::removeNode);

        return ResponseEntity.ok().build();
    }
}
