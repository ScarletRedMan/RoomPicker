package ru.dragonestia.picker.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.dragonestia.picker.api.exception.NodeAlreadyExistException;
import ru.dragonestia.picker.api.repository.response.type.type.PickingMode;
import ru.dragonestia.picker.model.Node;

import java.util.List;

@SpringBootTest
public class NodeServiceTests {

    @Autowired
    private NodeService nodeService;

    @Test
    void test_nodeCreateAndRemove() {
        var node = new Node("test", PickingMode.SEQUENTIAL_FILLING, false);

        Assertions.assertDoesNotThrow(() -> nodeService.create(node));
        Assertions.assertTrue(nodeService.find(node.id()).isPresent());
        Assertions.assertThrows(NodeAlreadyExistException.class, () -> nodeService.create(node));

        nodeService.remove(node);

        Assertions.assertFalse(() -> nodeService.find(node.id()).isPresent());
    }

    @Test
    void test_allNodes() {
        nodeService.all().forEach(node -> nodeService.remove(node));

        var nodes = List.of(
                new Node("test1", PickingMode.SEQUENTIAL_FILLING, false),
                new Node("test2", PickingMode.ROUND_ROBIN, false),
                new Node("test3", PickingMode.ROUND_ROBIN, false)
        );

        nodes.forEach(node -> nodeService.create(node));

        var list = nodeService.all();

        Assertions.assertEquals(nodes.size(), list.size());
        Assertions.assertTrue(list.containsAll(nodes));

        nodes.forEach(node -> nodeService.remove(node));

        Assertions.assertEquals(0, nodeService.all().size());
    }
}
