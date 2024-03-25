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
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.model.factory.RoomFactory;
import ru.dragonestia.picker.model.type.SlotLimit;

import java.util.List;
import java.util.Set;

@SpringBootTest
public class RoomServiceTests {

    @Autowired
    private NodeService nodeService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomFactory roomFactory;

    private Node node;

    @BeforeEach
    void init() {
        node = new Node(NodeIdentifier.of("test-rooms"), PickingMethod.SEQUENTIAL_FILLING, false);

        try {
            nodeService.create(node);
        } catch (NodeAlreadyExistException ignore) {}
    }

    @WithMockUser(roles = {"NODE_MANAGEMENT"})
    @Test
    void test_createAndRemove() {
        var room = roomFactory.create(RoomIdentifier.of("test-room"), node, IRoom.UNLIMITED_SLOTS, "", false);
        roomService.create(room);

        Assertions.assertTrue(roomService.find(node, room.getIdentifier()).isPresent());
        Assertions.assertThrows(RoomAlreadyExistException.class, () -> roomService.create(room));

        roomService.remove(room);

        Assertions.assertFalse(roomService.find(node, room.getIdentifier()).isPresent());
    }

    @WithMockUser(roles = {"NODE_MANAGEMENT"})
    @Test
    void test_allRooms() {
        var rooms = List.of(
                roomFactory.create(RoomIdentifier.of("test-room1"), node, 1, "", false),
                roomFactory.create(RoomIdentifier.of("test-room2"), node, 2, "", false),
                roomFactory.create(RoomIdentifier.of("test-room3"), node, 3, "", false),
                roomFactory.create(RoomIdentifier.of("test-room4"), node, IRoom.UNLIMITED_SLOTS, "", false)
        );

        rooms.forEach(room -> roomService.create(room));

        var list = roomService.all(node);

        Assertions.assertEquals(rooms.size(), list.size());
        Assertions.assertTrue(rooms.containsAll(list));
    }

    @WithMockUser(roles = {"NODE_MANAGEMENT"})
    @Test
    void test_exceptNotPersistedNode() {
        Assertions.assertThrows(NotPersistedNodeException.class, () -> {
            roomService.create(roomFactory.create(RoomIdentifier.of("1"), node, IRoom.UNLIMITED_SLOTS, "", true));
        });
    }

    @WithMockUser(roles = {"NODE_MANAGEMENT"})
    @Test
    void test_pickRoom() {
        var rooms = List.of(
                roomFactory.create(RoomIdentifier.of("test-room1"), node, 1, "", false),
                roomFactory.create(RoomIdentifier.of("test-room2"), node, 2, "", false),
                roomFactory.create(RoomIdentifier.of("test-room3"), node, 3, "", false),
                roomFactory.create(RoomIdentifier.of("test-room4"), node, IRoom.UNLIMITED_SLOTS, "", false)
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


        Assertions.assertEquals("test-room4", roomService.pickAvailable(node, users).roomId());
    }

    @WithMockUser(roles = {"NODE_MANAGEMENT"})
    @Test
    void test_removeNode() {
        nodeService.remove(node);

        Assertions.assertThrows(NodeNotFoundException.class, () -> roomService.all(node));
    }

    @WithMockUser(roles = {"NODE_MANAGEMENT"})
    @Test
    void test_nodeDoesNotExists() {
        var node = new Node(NodeIdentifier.of("bruh"), PickingMethod.ROUND_ROBIN, false);
        var room = roomFactory.create(RoomIdentifier.of("test"), node, IRoom.UNLIMITED_SLOTS, "", false);

        Assertions.assertThrows(NodeNotFoundException.class, () -> roomService.create(room));
        Assertions.assertThrows(NodeNotFoundException.class, () -> roomService.remove(room));
        Assertions.assertThrows(NodeNotFoundException.class, () -> roomService.find(node, "Bruh"));
        Assertions.assertThrows(NodeNotFoundException.class, () -> roomService.pickAvailable(node, Set.of(new User(UserIdentifier.of("1")))));
    }
}
