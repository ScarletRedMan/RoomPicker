package ru.dragonestia.picker.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.dragonestia.picker.api.exception.NodeAlreadyExistException;
import ru.dragonestia.picker.api.exception.NodeNotFoundException;
import ru.dragonestia.picker.api.exception.NotPersistedNodeException;
import ru.dragonestia.picker.api.exception.RoomAlreadyExistException;
import ru.dragonestia.picker.api.repository.response.type.type.PickingMode;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.model.type.SlotLimit;

import java.util.List;

@SpringBootTest
public class RoomServiceTests {

    @Autowired
    private NodeService nodeService;

    @Autowired
    private RoomService roomService;

    private Node node;

    @BeforeEach
    void init() {
        node = new Node("test-rooms", PickingMode.SEQUENTIAL_FILLING, false);

        try {
            nodeService.create(node);
        } catch (NodeAlreadyExistException ignore) {}
    }

    @Test
    void test_createAndRemove() {
        var room = Room.create("test-room", node, SlotLimit.unlimited(), "", false);
        roomService.create(room);

        Assertions.assertTrue(roomService.find(node, room.getId()).isPresent());
        Assertions.assertThrows(RoomAlreadyExistException.class, () -> roomService.create(room));

        roomService.remove(room);

        Assertions.assertFalse(roomService.find(node, room.getId()).isPresent());
    }

    @Test
    void test_allRooms() {
        var rooms = List.of(
                Room.create("test-room1", node, SlotLimit.of(1), "", false),
                Room.create("test-room2", node, SlotLimit.of(2), "", false),
                Room.create("test-room3", node, SlotLimit.of(3), "", false),
                Room.create("test-room4", node, SlotLimit.unlimited(), "", false)
        );

        rooms.forEach(room -> roomService.create(room));

        var list = roomService.all(node);

        Assertions.assertEquals(rooms.size(), list.size());
        Assertions.assertTrue(rooms.containsAll(list));
    }

    @Test
    void test_exceptNotPersistedNode() {
        Assertions.assertThrows(NotPersistedNodeException.class, () -> roomService.create(Room.create("1", node, SlotLimit.unlimited(), "", true)));
    }

    @Test
    void test_pickRoom() {
        var rooms = List.of(
                Room.create("test-room1", node, SlotLimit.of(1), "", false),
                Room.create("test-room2", node, SlotLimit.of(2), "", false),
                Room.create("test-room3", node, SlotLimit.of(3), "", false),
                Room.create("test-room4", node, SlotLimit.unlimited(), "", false)
        );

        rooms.forEach(room -> roomService.create(room));

        var users = List.of(
                new User("1"),
                new User("2"),
                new User("3"),
                new User("4"),
                new User("5"),
                new User("6")
        );


        Assertions.assertEquals("test-room4", roomService.pickAvailable(node, users).getId());
    }

    @Test
    void test_removeNode() {
        nodeService.remove(node);

        Assertions.assertThrows(NodeNotFoundException.class, () -> roomService.all(node));
    }

    @Test
    void test_nodeDoesNotExists() {
        var node = new Node("Bruh", PickingMode.ROUND_ROBIN, false);
        var room = Room.create("test", node, SlotLimit.unlimited(), "", false);

        Assertions.assertThrows(NodeNotFoundException.class, () -> roomService.create(room));
        Assertions.assertThrows(NodeNotFoundException.class, () -> roomService.remove(room));
        Assertions.assertThrows(NodeNotFoundException.class, () -> roomService.find(node, "Bruh"));
        Assertions.assertThrows(NodeNotFoundException.class, () -> roomService.pickAvailable(node, List.of(new User("1"))));
    }
}
