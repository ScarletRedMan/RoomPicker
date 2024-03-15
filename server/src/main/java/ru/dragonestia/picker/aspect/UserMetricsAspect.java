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
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.event.UpdateRoomLockStateEvent;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.repository.UserRepository;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Aspect
@RequiredArgsConstructor
@Log4j2
public class UserMetricsAspect {

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

    @AfterReturning(value = "execution(void ru.dragonestia.picker.repository.RoomRepository.create(ru.dragonestia.picker.model.Room)) && args(room)", argNames = "room")
    void onCreateRoom(Room room) {
        checkRoom(room);
    }

    @After(value = "execution(void ru.dragonestia.picker.repository.UserRepository.onRemoveRoom(ru.dragonestia.picker.model.Room)) && args(room, ..)", argNames = "room")
    void onRemoveRoom(Room room) {
        countAllUsers(room);
    }

    private void countAllUsers(Room room) {
        totalUsers.set(userRepository.countAllUsers());

        checkRoom(room);
    }

    private void checkRoom(Room room) {
        var set = data.get(room.getNodeIdentifier()).locked();
        if (room.isLocked()) {
            set.add(room);
            return;
        }
        if (!room.hasUnlimitedSlots() && userRepository.usersOf(room).size() >= room.getMaxSlots()) {
            set.add(room);
            return;
        }
        set.remove(room);
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

        var lockedGauge = Gauge.builder("roompicker_locked_rooms", () -> data.get(nodeId).locked().size())
                .tag("nodeId", nodeId)
                .register(meterRegistry);

        data.put(nodeId, new NodeData(gauge, new AtomicInteger(0), counter, new HashSet<>(), lockedGauge));
    }

    @After(value = "execution(* ru.dragonestia.picker.repository.NodeRepository.delete(ru.dragonestia.picker.model.Node)) && args(node)", argNames = "node")
    void onDeleteNode(Node node) {
        var data = this.data.remove(node.getIdentifier());

        meterRegistry.remove(data.usersGauge());
        meterRegistry.remove(data.picksPerMinute());
        meterRegistry.remove(data.lockedGauge());
    }

    @AfterReturning(value = "execution(* ru.dragonestia.picker.repository.RoomRepository.pickFree(ru.dragonestia.picker.model.Node, *)) && args(node, ..)", argNames = "node")
    void onPickRoom(Node node) {
        data.get(node.getIdentifier()).picksPerMinute().increment();
    }

    @EventListener
    void onRoomChangeLockState(UpdateRoomLockStateEvent event) {
        checkRoom(event.room());
    }

    @Scheduled(fixedDelay = 3_000)
    void updateUserMetrics() {
        userRepository.countUsersForNodes().forEach((nodeId, users) -> data.get(nodeId).users().set(users));
    }

    private record NodeData(Gauge usersGauge, AtomicInteger users, Counter picksPerMinute, Set<Room> locked, Gauge lockedGauge) {}
}
