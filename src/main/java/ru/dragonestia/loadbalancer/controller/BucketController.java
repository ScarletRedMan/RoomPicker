package ru.dragonestia.loadbalancer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dragonestia.loadbalancer.controller.response.BucketListResponse;
import ru.dragonestia.loadbalancer.service.BucketService;
import ru.dragonestia.loadbalancer.service.NodeService;

@RestController
@RequestMapping("/nodes/{nodeIdentifier}/buckets")
@RequiredArgsConstructor
public class BucketController {

    private final NodeService nodeService;
    private final BucketService bucketService;

    @GetMapping
    ResponseEntity<BucketListResponse> allBuckets(@PathVariable(name = "nodeIdentifier") String nodeId) {
        var nodeOpt = nodeService.findNode(nodeId);
        return nodeOpt.map(node -> ResponseEntity.ok(new BucketListResponse(nodeId,
                bucketService.allBuckets(node).stream()
                        .map(bucket -> new BucketListResponse.BucketInfo(bucket.getIdentifier(), bucket.getSlots().getSlots()))
                        .toList()
        ))).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
