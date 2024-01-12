package ru.dragonestia.picker.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.model.type.PickingMode;
import ru.dragonestia.picker.model.type.SlotLimit;
import ru.dragonestia.picker.repository.NodeRepository;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.repository.UserRepository;

import java.util.List;

@Profile("test_pickers")
@Configuration
@RequiredArgsConstructor
public class TestPickersConfig {

    private final NodeRepository nodeRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Bean
    void createSequentialFillingNode() {
        var node = new Node("seq", PickingMode.SEQUENTIAL_FILLING);
        nodeRepository.create(node);

        fillNode(node);
    }

    @Bean
    void createRoundRobinNode() {
        var node = new Node("round", PickingMode.ROUND_ROBIN);
        nodeRepository.create(node);

        fillNode(node);
    }

    @Bean
    void createLeastPickerNode() {
        var node = new Node("least", PickingMode.LEAST_PICKED);
        nodeRepository.create(node);

        fillNode(node);
    }

    private void fillNode(Node node) {
        for (int i = 0, n = 5; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                var room = Room.create("room-" + i + "-" + j, node, SlotLimit.of(n), "");
                roomRepository.create(room);

                for (int k = n - i - 1; k >= 0; k--) {
                    var user = new User("user-" + k);
                    userRepository.linkWithRoom(room, List.of(user), false);
                }
            }
        }
    }
}
