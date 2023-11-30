package ru.dragonestia.loadbalancer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.loadbalancer.controller.response.BucketListResponse;
import ru.dragonestia.loadbalancer.controller.response.BucketRegisterResponse;
import ru.dragonestia.loadbalancer.controller.response.NodeRegisterResponse;
import ru.dragonestia.loadbalancer.model.Bucket;
import ru.dragonestia.loadbalancer.model.type.SlotLimit;
import ru.dragonestia.loadbalancer.service.BucketService;
import ru.dragonestia.loadbalancer.service.NodeService;

@Log4j2
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

    @PostMapping
    ResponseEntity<BucketRegisterResponse> registerBucket(@PathVariable(name = "nodeIdentifier") String nodeId,
                                                          @RequestParam(name = "identifier") String bucketId,
                                                          @RequestParam(name = "slots") int slots,
                                                          @RequestParam(name = "payload") String payload,
                                                          @RequestParam(name = "locked", defaultValue = "false") boolean locked) {

        var nodeOpt = nodeService.findNode(nodeId);

        if (nodeOpt.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(new BucketRegisterResponse(false, "Node does not exist"));
        }

        var bucket = Bucket.create(bucketId, nodeOpt.get(), SlotLimit.of(slots), payload);
        bucket.setLocked(locked);
        try {
            bucketService.createBucket(bucket);
            return ResponseEntity.ok(new BucketRegisterResponse(true, ""));
        } catch (Error error) {
            return ResponseEntity.status(400).body(new BucketRegisterResponse(false, error.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new BucketRegisterResponse(false, ex.getMessage()));
        }
    }
}
