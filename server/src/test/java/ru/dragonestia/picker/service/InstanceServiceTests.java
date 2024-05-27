package ru.dragonestia.picker.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import ru.dragonestia.picker.exception.AlreadyExistsException;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.model.instance.InstanceId;
import ru.dragonestia.picker.model.instance.type.PickingMethod;

import java.util.List;

@SpringBootTest
public class InstanceServiceTests {

    @Autowired
    private InstanceService instanceService;

    @WithMockUser(roles = {"NODE_MANAGEMENT"})
    @Test
    void test_nodeCreateAndRemove() {
        var node = new Instance(InstanceId.of("test"), PickingMethod.SEQUENTIAL_FILLING, false);

        Assertions.assertDoesNotThrow(() -> instanceService.create(node));
        Assertions.assertTrue(instanceService.find(node.getId()).isPresent());
        Assertions.assertThrows(AlreadyExistsException.class, () -> instanceService.create(node));

        instanceService.remove(node.getId());

        Assertions.assertFalse(() -> instanceService.find(node.getId()).isPresent());
    }

    @WithMockUser(roles = {"NODE_MANAGEMENT"})
    @Test
    void test_allNodes() {
        instanceService.all().forEach(node -> instanceService.remove(node.getId()));

        var nodes = List.of(
                new Instance(InstanceId.of("test1"), PickingMethod.SEQUENTIAL_FILLING, false),
                new Instance(InstanceId.of("test2"), PickingMethod.ROUND_ROBIN, false),
                new Instance(InstanceId.of("test3"), PickingMethod.ROUND_ROBIN, false)
        );

        nodes.forEach(node -> instanceService.create(node));

        var list = instanceService.all();

        Assertions.assertEquals(nodes.size(), list.size());
        Assertions.assertTrue(list.containsAll(nodes));

        nodes.forEach(node -> instanceService.remove(node.getId()));

        Assertions.assertEquals(0, instanceService.all().size());
    }
}
