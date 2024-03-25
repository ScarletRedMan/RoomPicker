package ru.dragonestia.picker.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import ru.dragonestia.picker.api.exception.NodeAlreadyExistException;
import ru.dragonestia.picker.api.model.node.PickingMethod;
import ru.dragonestia.picker.api.repository.type.NodeIdentifier;
import ru.dragonestia.picker.model.Node;

import java.util.List;

@SpringBootTest
public class NodeServiceTests {

    @Autowired
    private NodeService nodeService;

    @WithMockUser(roles = {"NODE_MANAGEMENT"})
    @Test
    void test_nodeCreateAndRemove() {
        var node = new Node(NodeIdentifier.of("test"), PickingMethod.SEQUENTIAL_FILLING, false);

        Assertions.assertDoesNotThrow(() -> nodeService.create(node));
        Assertions.assertTrue(nodeService.find(node.getIdentifier()).isPresent());
        Assertions.assertThrows(NodeAlreadyExistException.class, () -> nodeService.create(node));

        nodeService.remove(node);

        Assertions.assertFalse(() -> nodeService.find(node.getIdentifier()).isPresent());
    }

    @WithMockUser(roles = {"NODE_MANAGEMENT"})
    @Test
    void test_allNodes() {
        nodeService.all().forEach(node -> nodeService.remove(node));

        var nodes = List.of(
                new Node(NodeIdentifier.of("test1"), PickingMethod.SEQUENTIAL_FILLING, false),
                new Node(NodeIdentifier.of("test2"), PickingMethod.ROUND_ROBIN, false),
                new Node(NodeIdentifier.of("test3"), PickingMethod.ROUND_ROBIN, false)
        );

        nodes.forEach(node -> nodeService.create(node));

        var list = nodeService.all();

        Assertions.assertEquals(nodes.size(), list.size());
        Assertions.assertTrue(list.containsAll(nodes));

        nodes.forEach(node -> nodeService.remove(node));

        Assertions.assertEquals(0, nodeService.all().size());
    }
}
