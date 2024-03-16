package ru.dragonestia.picker.aspect;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.repository.UserRepository;
import ru.dragonestia.picker.repository.impl.ContainerRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Aspect
@RequiredArgsConstructor
@Log4j2
public class UserMetricsAspect {

    private final ContainerRepository containerRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final MeterRegistry meterRegistry;

    private final AtomicInteger totalUsers = new AtomicInteger(0);
    private final Map<String, NodeData> data = new ConcurrentHashMap<>();

    @PostConstruct
    void init() {
        meterRegistry.gauge("roompicker_total_users", totalUsers);
    }

    @After(value = "execution(* ru.dragonestia.picker.repository.UserRepository.linkWithRoom(ru.dragonestia.picker.model.Room, ..)) && args(room, ..)", argNames = "room")
    void onLinkUsers(Room room) {
        countAllUsers(room);
    }

    @After(value = "execution(void ru.dragonestia.picker.repository.UserRepository.unlinkWithRoom(ru.dragonestia.picker.model.Room, ..)) && args(room, ..)", argNames = "room")
    void onUnlinkUsers(Room room) {
        countAllUsers(room);
    }

    private void countAllUsers(Room room) {
        totalUsers.set(userRepository.countAllUsers());
    }

    @After(value = "execution(void ru.dragonestia.picker.repository.NodeRepository.create(ru.dragonestia.picker.model.Node)) && args(node)", argNames = "node")
    void onCreateNode(Node node) {
        var nodeId = node.getIdentifier();
        var gauge = Gauge.builder("roompicker_node_users_total", () -> data.get(nodeId).users())
                .tag("nodeId", nodeId)
                .register(meterRegistry);

        var counter = Counter.builder("roompicker_picks")
                .tag("nodeId", nodeId)
                .baseUnit("1s")
                .register(meterRegistry);

        var lockedGauge = Gauge.builder("roompicker_locked_rooms", () -> data.get(nodeId).locked())
                .tag("nodeId", nodeId)
                .register(meterRegistry);

        var roomsGauge = Gauge.builder("roompicker_rooms", () -> roomRepository.all(node).size())
                .tag("nodeId", nodeId)
                .register(meterRegistry);

        data.put(nodeId, new NodeData(gauge, new AtomicInteger(0), counter, new AtomicInteger(0), lockedGauge, roomsGauge));
    }

    @After(value = "execution(* ru.dragonestia.picker.repository.NodeRepository.delete(ru.dragonestia.picker.model.Node)) && args(node)", argNames = "node")
    void onDeleteNode(Node node) {
        var data = this.data.remove(node.getIdentifier());

        meterRegistry.remove(data.usersGauge());
        meterRegistry.remove(data.picksPerMinute());
        meterRegistry.remove(data.lockedGauge());
        meterRegistry.remove(data.roomsGauge());
    }

    @AfterReturning(value = "execution(* ru.dragonestia.picker.repository.RoomRepository.pick(ru.dragonestia.picker.model.Node, *)) && args(node, ..)", argNames = "node")
    void onPickRoom(Node node) {
        data.get(node.getIdentifier()).picksPerMinute().increment();
    }

    @Scheduled(fixedDelay = 3_000)
    void updateUserMetrics() {
        userRepository.countUsersForNodes().forEach((nodeId, users) -> {
            Optional.ofNullable(data.get(nodeId)).ifPresent(node -> node.users().set(users));
        });

        containerRepository.all().forEach(nodeContainer -> {
            var locked = data.get(nodeContainer.getNode().getIdentifier()).locked();
            locked.set(0);

            nodeContainer.allRooms().forEach(roomContainer -> {
                if (roomContainer.canBePicked(1)) return;

                locked.incrementAndGet();
            });
        });
    }

    private record NodeData(Gauge usersGauge, AtomicInteger users, Counter picksPerMinute, AtomicInteger locked, Gauge lockedGauge, Gauge roomsGauge) {}
}
