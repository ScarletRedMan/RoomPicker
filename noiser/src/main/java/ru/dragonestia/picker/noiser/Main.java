package ru.dragonestia.picker.noiser;

import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.model.entity.EntityId;
import ru.dragonestia.picker.api.model.instance.InstanceId;
import ru.dragonestia.picker.api.model.instance.type.PickingMethod;
import ru.dragonestia.picker.api.model.room.RoomId;

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
    private final List<InstanceId> nodes;

    private final Map<InstanceId, AtomicInteger> totalUsers = new ConcurrentHashMap<>();
    private final int expectedUsers = 10000;

    public Main() {
        client = new RoomPickerClient("http://localhost:8080", "admin", "qwerty123");
        nodes = initNodes();
    }

    private void removeAll() {
        client.getInstanceRepository().allInstancesIds()
                .forEach(node -> client.getInstanceRepository().deleteInstance(node));
    }

    private List<InstanceId> initNodes() {
        removeAll();

        var list = new ArrayList<InstanceId>();

        for (int i = 0; i < 5; i++) {
            var nodeId = InstanceId.of("test-node-" + i);
            client.getInstanceRepository().createInstance(nodeId, PickingMethod.values()[i % PickingMethod.values().length], false);
            totalUsers.put(nodeId, new AtomicInteger(0));
            list.add(nodeId);
        }

        return list;
    }

    private void initRooms() {
        final int perNode = expectedUsers / nodes.size();
        final int roomsPerNode = perNode / 10;

        for (var instanceId: nodes) {
            for (int i = 0; i < roomsPerNode; i++) {
                client.getRoomRepository().createRoom(instanceId, RoomId.of(UUID.randomUUID().toString()), 50, "", false, false);
            }
        }
    }

    private void pickUsers() {
        for (var nodeId: nodes) {
            var usersInNode = totalUsers.get(nodeId);

            try {
                synchronized (usersInNode) {
                    var users = new HashSet<EntityId>();
                    var maxAdd = Math.min(10, (expectedUsers / nodes.size()) - usersInNode.get());

                    if (maxAdd == 0) return;
                    var add = maxAdd == 1 ? 1 : (random.nextInt(maxAdd - 1) + 1);
                    for (int i = 0; i < add; i++) {
                        users.add(EntityId.of(UUID.randomUUID().toString()));
                    }

                    var request = client.getInstanceRepository().pickRoom(nodeId, users);
                    usersInNode.addAndGet(add);
                    var roomId = request.getRoom().id();

                    System.out.printf("Added %s(total %s) users to %s/%s%n", add, usersInNode.get(), nodeId.getValue(), roomId.getValue());

                    scheduler.schedule(() -> {
                        try {
                            client.getEntityRepository().unlinkEntitiesFromRoom(nodeId, roomId, users);

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