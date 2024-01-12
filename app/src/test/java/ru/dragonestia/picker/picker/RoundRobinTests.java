package ru.dragonestia.picker.picker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.dragonestia.picker.config.FillingNodesConfig;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.repository.UserRepository;
import ru.dragonestia.picker.util.UserFiller;

@SpringBootTest
@Import({FillingNodesConfig.class, UserFiller.class})
public class RoundRobinTests {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFiller userFiller;

    @Qualifier("roundNode")
    @Autowired
    private Node node;

    @Test
    void testPickingRoundRobin() {
        { // first iteration. Take 2 users. need take 'room-2-0'
            var roomOpt = roomRepository.pickFree(node, userFiller.createRandomUsers(2));
            Assertions.assertTrue(roomOpt.isPresent());

            var room = roomOpt.get();
            var slots = room.getSlots();
            var users = userRepository.usersOf(room);
            Assertions.assertTrue(slots.isUnlimited() || slots.getSlots() >= users.size()); // check slots limitation

            Assertions.assertEquals("room-2-0", room.getId());
        }

        { // second iteration. Take 2 users. need take 'room-2-1'
            var roomOpt = roomRepository.pickFree(node, userFiller.createRandomUsers(2));
            Assertions.assertTrue(roomOpt.isPresent());

            var room = roomOpt.get();
            var slots = room.getSlots();
            var users = userRepository.usersOf(room);
            Assertions.assertTrue(slots.isUnlimited() || slots.getSlots() >= users.size()); // check slots limitation

            Assertions.assertEquals("room-2-1", room.getId());
        }
    }
}
