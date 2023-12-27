package ru.dragonestia.picker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.type.PickingMode;
import ru.dragonestia.picker.model.type.SlotLimit;

import java.util.HashMap;

public class RoomServiceTest {

    private RoomService roomService;
    private HashMap<String, Room> roomMap;

    @BeforeEach
    void setup() {
        roomMap = new HashMap<>();
        roomService = Mockito.mock(RoomService.class);
        Mockito.doAnswer(invocation -> {
            var room = invocation.getArgument(0, Room.class);

            return null;
        }).when(roomService).create(Mockito.any(Room.class));
    }

    Node createNode() {
        return new Node("test-node", PickingMode.ROUND_ROBIN);
    }

    @Test
    void test() {
        var node = createNode();

        roomService.create(Room.create("test-room", node, SlotLimit.unlimited(), ""));
    }
}
