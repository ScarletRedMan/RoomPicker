package ru.dragonestia.picker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.dragonestia.picker.api.repository.response.type.type.PickingMode;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.model.type.SlotLimit;
import ru.dragonestia.picker.repository.NodeRepository;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.repository.UserRepository;

import java.util.List;

@TestConfiguration
public class FillingNodesConfig {

    /* All nodes have these rooms:

    Room 'room-0-0' has 5/5 users
    Room 'room-0-1' has 5/5 users
    Room 'room-0-2' has 5/5 users
    Room 'room-1-0' has 4/5 users
    Room 'room-1-1' has 4/5 users
    Room 'room-1-2' has 4/5 users
    Room 'room-2-0' has 3/5 users
    Room 'room-2-1' has 3/5 users
    Room 'room-2-2' has 3/5 users
    Room 'room-3-0' has 2/5 users
    Room 'room-3-1' has 2/5 users
    Room 'room-3-2' has 2/5 users
    Room 'room-4-0' has 1/5 users
    Room 'room-4-1' has 1/5 users
    Room 'room-4-2' has 1/5 users
    */

    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    private Node seqNode;
    private Node roundNode;
    private Node leastNode;

    @Bean
    void createSequentialFillingNode() {
        var node = new Node("seq", PickingMode.SEQUENTIAL_FILLING, false);
        nodeRepository.create(node);

        fillNode(node);

        seqNode = node;
    }

    @Bean
    void createRoundRobinNode() {
        var node = new Node("round", PickingMode.ROUND_ROBIN, false);
        nodeRepository.create(node);

        fillNode(node);

        roundNode = node;
    }

    @Bean
    void createLeastPickerNode() {
        var node = new Node("least", PickingMode.LEAST_PICKED, false);
        nodeRepository.create(node);

        fillNode(node);

        leastNode = node;
    }

    private void fillNode(Node node) {
        for (int i = 0, n = 5; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                var roomId = "room-" + i + "-" + j;
                var room = Room.create(roomId, node, SlotLimit.of(n), "", false);
                roomRepository.create(room);

                var users = n - i;
                for (int k = users - 1; k >= 0; k--) {
                    var user = new User("user-" + k);
                    userRepository.linkWithRoom(room, List.of(user), false);
                }

                //System.out.printf("Room '%s' has %s/%s users%n", roomId, users, n);
            }
        }
    }

    @Bean
    Node seqNode() {
        return seqNode;
    }

    @Bean
    Node roundNode() {
        return roundNode;
    }

    @Bean
    Node leastNode() {
        return leastNode;
    }
}
