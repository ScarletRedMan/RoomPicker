package ru.dragonestia.picker.noiser;

import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.model.node.NodeDefinition;
import ru.dragonestia.picker.api.model.node.PickingMethod;
import ru.dragonestia.picker.api.model.room.RoomDefinition;
import ru.dragonestia.picker.api.repository.query.node.GetAllNodes;
import ru.dragonestia.picker.api.repository.query.user.UnlinkUsersFromRoom;
import ru.dragonestia.picker.api.repository.type.NodeIdentifier;
import ru.dragonestia.picker.api.repository.type.RoomIdentifier;
import ru.dragonestia.picker.api.repository.type.EntityIdentifier;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class Main {

    private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(8);
    private final Random random = new Random();
    private final RoomPickerClient client;
    private final List<NodeIdentifier> nodes;

    private final Map<NodeIdentifier, AtomicInteger> totalUsers = new ConcurrentHashMap<>();
    private final int expectedUsers = 10000;

    public Main() {
        client = new RoomPickerClient("http://localhost:8080", "admin", "qwerty123");
        nodes = initNodes();
    }

    private void removeAll() {
        client.getNodeRepository().allNodes(GetAllNodes.JUST)
                .forEach(node -> client.getNodeRepository().removeNode(node));
    }

    private List<NodeIdentifier> initNodes() {
        removeAll();

        var list = new ArrayList<NodeIdentifier>();

        for (int i = 0; i < 5; i++) {
            var node = new NodeDefinition(NodeIdentifier.of("test-node-" + i))
                    .setPickingMethod(PickingMethod.values()[i % PickingMethod.values().length]);

            client.getNodeRepository().saveNode(node);

            var nodeId = node.getIdentifierObject();
            totalUsers.put(nodeId, new AtomicInteger(0));
            list.add(nodeId);
        }

        return list;
    }

    private void initRooms() {
        final int perNode = expectedUsers / nodes.size();
        final int roomsPerNode = perNode / 10;

        for (var nodeId: nodes) {
            for (int i = 0; i < roomsPerNode; i++) {
                client.getRoomRepository().saveRoom(
                        new RoomDefinition(nodeId, RoomIdentifier.of(UUID.randomUUID().toString())).setMaxSlots(50)
                );
            }
        }
    }

    private void pickUsers() {
        for (var nodeId: nodes) {
            var usersInNode = totalUsers.get(nodeId);

            try {
                synchronized (usersInNode) {
                    var users = new HashSet<EntityIdentifier>();
                    var maxAdd = Math.min(10, (expectedUsers / nodes.size()) - usersInNode.get());

                    if (maxAdd == 0) return;
                    var add = maxAdd == 1 ? 1 : (random.nextInt(maxAdd - 1) + 1);
                    for (int i = 0; i < add; i++) {
                        users.add(EntityIdentifier.of(UUID.randomUUID().toString()));
                    }

                    var request = client.getNodeRepository().pickRoom(nodeId, users);
                    usersInNode.addAndGet(add);
                    var roomId = RoomIdentifier.of(request.roomId());

                    System.out.printf("Added %s(total %s) users to %s/%s%n", add, usersInNode.get(), nodeId.getValue(), roomId.getValue());

                    scheduler.schedule(() -> {
                        try {
                            client.getUserRepository().unlinkUsersFromRoom(UnlinkUsersFromRoom.builder()
                                    .setNodeId(nodeId)
                                    .setRoomId(roomId)
                                    .setUsers(users)
                                    .build());

                            usersInNode.addAndGet(-add);
                            System.out.printf("Reduced %s users from %s/%s%n", add, nodeId.getValue(), roomId.getValue());
                        } catch (Exception ex) {
                            System.out.println("Error(" + ex.getClass().getSimpleName() + "): " + ex.getMessage());
                        }
                    }, random.nextInt(10) + 1, TimeUnit.SECONDS);
                }
            } catch (Exception ex) {
                System.out.println("Error(" + ex.getClass().getSimpleName() + "): " + ex.getMessage());
            }
        }
    }

    public void startNoise() throws InterruptedException {
        initRooms();

        while (true) {
            pickUsers();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Main().startNoise();
    }
}