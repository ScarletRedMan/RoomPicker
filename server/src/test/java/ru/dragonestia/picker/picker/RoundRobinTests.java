package ru.dragonestia.picker.picker;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.dragonestia.picker.config.FillingNodesConfig;
import ru.dragonestia.picker.exception.NoRoomsAvailableException;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.repository.EntityRepository;
import ru.dragonestia.picker.util.UserFiller;

import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@Import({FillingNodesConfig.class, UserFiller.class})
public class RoundRobinTests {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private UserFiller userFiller;

    @Qualifier("roundNode")
    @Autowired
    private Instance instance;

    @Timeout(value = 1, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @ParameterizedTest
    @ArgumentsSource(PickingArgumentProvider.class)
    void testPicking(String expectedRoomId, int usersAmount) {
        var room = roomRepository.pick(instance.getId(), userFiller.createRandomUsers(usersAmount));
        var slots = room.getSlots();
        var users = entityRepository.entitiesOf(room);
        Assertions.assertTrue(slots == -1 || slots >= users.size()); // check slots limitation

        Assertions.assertEquals(expectedRoomId, room.getId().getValue());
    }

    public static class PickingArgumentProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of( // note: dump taken from FillingNodesConfig
                    Arguments.of("room-2-0", 2),
                    Arguments.of("room-2-1", 2),
                    Arguments.of("room-2-2", 1),
                    Arguments.of("room-4-0", 4),
                    Arguments.of("room-4-1", 4),
                    Arguments.of("room-4-2", 4),
                    Arguments.of("room-3-0", 3)
            );
        }
    }

    @Timeout(value = 1, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @Test
    void testNoOneRoomExpected() { // Take 9 users. expected none result
        Assertions.assertThrows(NoRoomsAvailableException.class, () -> roomRepository.pick(instance.getId(), userFiller.createRandomUsers(9)));
    }
}
