package ru.dragonestia.picker.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import ru.dragonestia.picker.api.exception.NodeAlreadyExistException;
import ru.dragonestia.picker.api.exception.NodeNotFoundException;
import ru.dragonestia.picker.api.exception.NotPersistedNodeException;
import ru.dragonestia.picker.api.exception.RoomAlreadyExistException;
import ru.dragonestia.picker.api.model.node.PickingMethod;
import ru.dragonestia.picker.api.model.room.IRoom;
import ru.dragonestia.picker.api.repository.type.NodeIdentifier;
import ru.dragonestia.picker.api.repository.type.RoomIdentifier;
import ru.dragonestia.picker.api.repository.type.UserIdentifier;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.model.user.User;
import ru.dragonestia.picker.model.factory.RoomFactory;

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
        instance = new Instance(NodeIdentifier.of("test-rooms"), PickingMethod.SEQUENTIAL_FILLING, false);

        try {
            instanceService.create(instance);
        } catch (NodeAlreadyExistException ignore) {}
    }

    @WithMockUser(roles = {"NODE_MANAGEMENT"})
    @Test
    void test_createAndRemove() {
        var room = roomFactory.create(RoomIdentifier.of("test-room"), instance, IRoom.UNLIMITED_SLOTS, "", false);
        roomService.create(room);

        Assertions.assertTrue(roomService.find(instance, room.getIdentifier()).isPresent());
        Assertions.assertThrows(RoomAlreadyExistException.class, () -> roomService.create(room));

        roomService.remove(room);

        Assertions.assertFalse(roomService.find(instance, room.getIdentifier()).isPresent());
    }

    @WithMockUser(roles = {"NODE_MANAGEMENT"})
    @Test
    void test_allRooms() {
        var rooms = List.of(
                roomFactory.create(RoomIdentifier.of("test-room1"), instance, 1, "", false),
                roomFactory.create(RoomIdentifier.of("test-room2"), instance, 2, "", false),
                roomFactory.create(RoomIdentifier.of("test-room3"), instance, 3, "", false),
                roomFactory.create(RoomIdentifier.of("test-room4"), instance, IRoom.UNLIMITED_SLOTS, "", false)
        );

        rooms.forEach(room -> roomService.create(room));

        var list = roomService.all(instance);

        Assertions.assertEquals(rooms.size(), list.size());
        Assertions.assertTrue(rooms.containsAll(list));
    }

    @WithMockUser(roles = {"NODE_MANAGEMENT"})
    @Test
    void test_exceptNotPersistedNode() {
        Assertions.assertThrows(NotPersistedNodeException.class, () -> {
            roomService.create(roomFactory.create(RoomIdentifier.of("1"), instance, IRoom.UNLIMITED_SLOTS, "", true));
        });
    }

    @WithMockUser(roles = {"NODE_MANAGEMENT"})
    @Test
    void test_pickRoom() {
        var rooms = List.of(
                roomFactory.create(RoomIdentifier.of("test-room1"), instance, 1, "", false),
                roomFactory.create(RoomIdentifier.of("test-room2"), instance, 2, "", false),
                roomFactory.create(RoomIdentifier.of("test-room3"), instance, 3, "", false),
                roomFactory.create(RoomIdentifier.of("test-room4"), instance, IRoom.UNLIMITED_SLOTS, "", false)
        );

        rooms.forEach(room -> roomService.create(room));

        var users = Set.of(
                new User(UserIdentifier.of("1")),
                new User(UserIdentifier.of("2")),
                new User(UserIdentifier.of("3")),
                new User(UserIdentifier.of("4")),
                new User(UserIdentifier.of("5")),
                new User(UserIdentifier.of("6"))
        );


        Assertions.assertEquals("test-room4", roomService.pickAvailable(instance, users).roomId());
    }

    @WithMockUser(roles = {"NODE_MANAGEMENT"})
    @Test
    void test_removeNode() {
        instanceService.remove(instance);

        Assertions.assertThrows(NodeNotFoundException.class, () -> roomService.all(instance));
    }

    @WithMockUser(roles = {"NODE_MANAGEMENT"})
    @Test
    void test_nodeDoesNotExists() {
        var node = new Instance(NodeIdentifier.of("bruh"), PickingMethod.ROUND_ROBIN, false);
        var room = roomFactory.create(RoomIdentifier.of("test"), node, IRoom.UNLIMITED_SLOTS, "", false);

        Assertions.assertThrows(NodeNotFoundException.class, () -> roomService.create(room));
        Assertions.assertThrows(NodeNotFoundException.class, () -> roomService.remove(room));
        Assertions.assertThrows(NodeNotFoundException.class, () -> roomService.find(node, "Bruh"));
        Assertions.assertThrows(NodeNotFoundException.class, () -> roomService.pickAvailable(node, Set.of(new User(UserIdentifier.of("1")))));
    }
}
