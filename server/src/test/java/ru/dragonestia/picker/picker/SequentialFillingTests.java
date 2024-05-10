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
import ru.dragonestia.picker.api.exception.NoRoomsAvailableException;
import ru.dragonestia.picker.config.FillingNodesConfig;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.repository.EntityRepository;
import ru.dragonestia.picker.util.UserFiller;

import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@Import({FillingNodesConfig.class, UserFiller.class})
public class SequentialFillingTests {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private UserFiller userFiller;

    @Qualifier("seqNode")
    @Autowired
    private Instance instance;

    @Timeout(value = 1, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @ParameterizedTest
    @ArgumentsSource(PickingArgumentProvider.class)
    void testPicking(String expectedRoomId, int usersAmount) {
        var expectedRoomUsers = entityRepository.entitiesOf(roomRepository.find(instance, expectedRoomId).orElseThrow()).size();

        var room = roomRepository.pick(instance, userFiller.createRandomUsers(usersAmount));
        var slots = room.getMaxSlots();
        var users = entityRepository.entitiesOf(room);
        Assertions.assertTrue(slots == -1 || slots >= users.size()); // check slots limitation

        System.out.printf("Room(%s) has %s/%s users. Expected: %s(%s), added: %s%n", room.getIdentifier(), users.size(), slots, expectedRoomId, expectedRoomUsers, usersAmount);
        Assertions.assertEquals(expectedRoomId, room.getIdentifier());
    }

    public static class PickingArgumentProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of( // note: dump taken from FillingNodesConfig
                    Arguments.of("room-1-0", 1),
                    Arguments.of("room-2-0", 2),
                    Arguments.of("room-1-1", 1),
                    Arguments.of("room-4-0", 4),
                    Arguments.of("room-1-2", 1),
                    Arguments.of("room-2-1", 1),
                    Arguments.of("room-2-1", 1)
            );
        }
    }

    @Timeout(value = 1, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @Test
    void testNoOneRoomExpected() { // Take 9 users. expected none result
        Assertions.assertThrows(NoRoomsAvailableException.class, () -> roomRepository.pick(instance, userFiller.createRandomUsers(9)));
    }
}
