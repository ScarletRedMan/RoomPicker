package ru.dragonestia.picker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.dragonestia.picker.model.entity.EntityId;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.model.entity.Entity;
import ru.dragonestia.picker.model.instance.InstanceId;
import ru.dragonestia.picker.model.instance.type.PickingMethod;
import ru.dragonestia.picker.model.room.RoomId;
import ru.dragonestia.picker.model.room.factory.RoomFactory;
import ru.dragonestia.picker.repository.InstanceRepository;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.repository.EntityRepository;

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
    private InstanceRepository instanceRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private RoomFactory roomFactory;

    private Instance seqInstance;
    private Instance roundInstance;
    private Instance leastInstance;

    @Bean
    void createSequentialFillingNode() {
        var node = new Instance(InstanceId.of("seq"), PickingMethod.SEQUENTIAL_FILLING, false);
        instanceRepository.create(node);

        fillNode(node);

        seqInstance = node;
    }

    @Bean
    void createRoundRobinNode() {
        var node = new Instance(InstanceId.of("round"), PickingMethod.ROUND_ROBIN, false);
        instanceRepository.create(node);

        fillNode(node);

        roundInstance = node;
    }

    @Bean
    void createLeastPickerNode() {
        var node = new Instance(InstanceId.of("least"), PickingMethod.LEAST_PICKED, false);
        instanceRepository.create(node);

        fillNode(node);

        leastInstance = node;
    }

    private void fillNode(Instance instance) {
        for (int i = 0, n = 5; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                var roomId = "room-" + i + "-" + j;
                var room = roomFactory.create(RoomId.of(roomId), instance, n, "", false);
                roomRepository.create(room);

                var users = n - i;
                for (int k = users - 1; k >= 0; k--) {
                    var user = EntityId.of("user-" + k);
                    entityRepository.linkWithRoom(room, List.of(user), false);
                }

                //System.out.printf("Room '%s' has %s/%s users%n", roomId, users, n);
            }
        }
    }

    @Bean
    Instance seqNode() {
        return seqInstance;
    }

    @Bean
    Instance roundNode() {
        return roundInstance;
    }

    @Bean
    Instance leastNode() {
        return leastInstance;
    }
}
