package ru.dragonestia.loadbalancer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.dragonestia.loadbalancer.model.Bucket;
import ru.dragonestia.loadbalancer.model.Node;
import ru.dragonestia.loadbalancer.model.type.LoadBalancingMethod;
import ru.dragonestia.loadbalancer.model.type.SlotLimit;

import java.util.HashMap;

public class BucketServiceTest {

    private BucketService bucketService;
    private HashMap<String, Bucket> bucketMap;

    @BeforeEach
    void setup() {
        bucketMap = new HashMap<>();
        bucketService = Mockito.mock(BucketService.class);
        Mockito.doAnswer(invocation -> {
            var bucket = invocation.getArgument(0, Bucket.class);

            return null;
        }).when(bucketService).createBucket(Mockito.any(Bucket.class));
    }

    Node createNode() {
        return new Node("test-node", LoadBalancingMethod.ROUND_ROBIN);
    }

    @Test
    void test() {
        var node = createNode();

        bucketService.createBucket(Bucket.create("test-bucket", node, SlotLimit.unlimited(), ""));
    }
}
