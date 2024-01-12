package ru.dragonestia.picker.picker;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.dragonestia.picker.config.FillingNodesConfig;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.repository.UserRepository;
import ru.dragonestia.picker.util.UserFiller;

import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@Import({FillingNodesConfig.class, UserFiller.class})
public class LeastPickedTests {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFiller userFiller;

    @Qualifier("leastNode")
    @Autowired
    private Node node;

    @Timeout(value = 1, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @ParameterizedTest
    @ArgumentsSource(PickingArgumentProvider.class)
    void testPicking(String expectedRoomId, int usersAmount) {
        var roomOpt = roomRepository.pickFree(node, userFiller.createRandomUsers(usersAmount));
        Assertions.assertTrue(roomOpt.isPresent());

        var room = roomOpt.get();
        var slots = room.getSlots();
        var users = userRepository.usersOf(room);
        Assertions.assertTrue(slots.isUnlimited() || slots.getSlots() >= users.size()); // check slots limitation

        Assertions.assertEquals(expectedRoomId, room.getId());
    }

    public static class PickingArgumentProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of( // note: dump taken from FillingNodesConfig
                    Arguments.of("room-4-0", 4),
                    Arguments.of("room-4-1", 4),
                    Arguments.of("room-4-2", 4),
                    Arguments.of("room-3-0", 1),
                    Arguments.of("room-3-1", 1),
                    Arguments.of("room-3-2", 1),
                    Arguments.of("room-2-0", 1),
                    Arguments.of("room-2-1", 1),
                    Arguments.of("room-2-2", 1),
                    Arguments.of("room-3-0", 1)
            );
        }
    }

    @Timeout(value = 1, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @Test
    void testNoOneRoomExpected() { // Take 9 users. expected none result
        var roomOpt = roomRepository.pickFree(node, userFiller.createRandomUsers(9));
        Assertions.assertTrue(roomOpt.isEmpty());
    }
}
