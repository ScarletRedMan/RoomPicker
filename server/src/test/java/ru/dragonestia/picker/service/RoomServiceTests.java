package ru.dragonestia.picker.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import ru.dragonestia.picker.exception.AlreadyExistsException;
import ru.dragonestia.picker.exception.ConflictingPersistParametersException;
import ru.dragonestia.picker.exception.DoesNotExistsException;
import ru.dragonestia.picker.model.entity.EntityId;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.model.instance.InstanceId;
import ru.dragonestia.picker.model.instance.type.PickingMethod;
import ru.dragonestia.picker.model.room.RoomId;
import ru.dragonestia.picker.model.room.factory.RoomFactory;

import java.util.List;
import java.util.Set;

@SpringBootTest
public class RoomServiceTests {

    @Autowired
    private InstanceService instanceService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomFactory roomFactory;

    private Instance instance;

    @BeforeEach
    void init() {
        instance = new Instance(InstanceId.of("test-rooms"), PickingMethod.SEQUENTIAL_FILLING, false);

        try {
            instanceService.create(instance);
        } catch (AlreadyExistsException ignore) {}
    }

    @WithMockUser(roles = {"NODE_MANAGEMENT"})
    @Test
    void test_createAndRemove() {
        var room = roomFactory.create(RoomId.of("test-room"), instance, -1, "", false);
        roomService.create(room);

        Assertions.assertTrue(roomService.find(instance.getId(), room.getId()).isPresent());
        Assertions.assertThrows(AlreadyExistsException.class, () -> roomService.create(room));

        roomService.remove(room);

        Assertions.assertFalse(roomService.find(instance.getId(), room.getId()).isPresent());
    }

    @WithMockUser(roles = {"NODE_MANAGEMENT"})
    @Test
    void test_allRooms() {
        var rooms = List.of(
                roomFactory.create(RoomId.of("test-room1"), instance, 1, "", false),
                roomFactory.create(RoomId.of("test-room2"), instance, 2, "", false),
                roomFactory.create(RoomId.of("test-room3"), instance, 3, "", false),
                roomFactory.create(RoomId.of("test-room4"), instance, -1, "", false)
        );

        rooms.forEach(room -> roomService.create(room));

        var list = roomService.all(instance.getId());

        Assertions.assertEquals(rooms.size(), list.size());
        Assertions.assertTrue(rooms.containsAll(list));
    }

    @WithMockUser(roles = {"NODE_MANAGEMENT"})
    @Test
    void test_exceptNotPersistedNode() {
        Assertions.assertThrows(ConflictingPersistParametersException.class, () -> {
            roomService.create(roomFactory.create(RoomId.of("1"), instance, -1, "", true));
        });
    }

    @WithMockUser(roles = {"NODE_MANAGEMENT"})
    @Test
    void test_pickRoom() {
        var rooms = List.of(
                roomFactory.create(RoomId.of("test-room1"), instance, 1, "", false),
                roomFactory.create(RoomId.of("test-room2"), instance, 2, "", false),
                roomFactory.create(RoomId.of("test-room3"), instance, 3, "", false),
                roomFactory.create(RoomId.of("test-room4"), instance, -1, "", false)
        );

        rooms.forEach(room -> roomService.create(room));

        var users = Set.of(
                EntityId.of("1"),
                EntityId.of("2"),
                EntityId.of("3"),
                EntityId.of("4"),
                EntityId.of("5"),
                EntityId.of("6")
        );


        Assertions.assertEquals("test-room4", roomService.pick(instance.getId(), users).getId().getValue());
    }

    @WithMockUser(roles = {"NODE_MANAGEMENT"})
    @Test
    void test_removeNode() {
        instanceService.remove(instance.getId());

        Assertions.assertThrows(DoesNotExistsException.class, () -> roomService.all(instance.getId()));
    }

    @WithMockUser(roles = {"NODE_MANAGEMENT"})
    @Test
    void test_nodeDoesNotExists() {
        var node = new Instance(InstanceId.of("bruh"), PickingMethod.ROUND_ROBIN, false);
        var room = roomFactory.create(RoomId.of("test"), node, -1, "", false);

        Assertions.assertThrows(DoesNotExistsException.class, () -> roomService.create(room));
        Assertions.assertThrows(DoesNotExistsException.class, () -> roomService.remove(room));
        Assertions.assertThrows(RuntimeException.class, () -> roomService.find(node.getId(), RoomId.of("Bruh")).orElseThrow());
        Assertions.assertThrows(DoesNotExistsException.class, () -> roomService.pick(node.getId(), Set.of(EntityId.of("1"))));
    }
}
