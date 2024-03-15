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
import ru.dragonestia.picker.repository.UserRepository;

import java.util.Map;
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
    private final Map<String, Gauge> nodeGauges = new ConcurrentHashMap<>();
    private final Map<String, Integer> nodeUsers = new ConcurrentHashMap<>();
    private final Map<String, Counter> pickPerMinute = new ConcurrentHashMap<>();

    @PostConstruct
    void init() {
        meterRegistry.gauge("roompicker_total_users", totalUsers);
    }

    @After("execution(* ru.dragonestia.picker.repository.UserRepository.linkWithRoom(..))")
    void onLinkUsers() {
        countAllUsers();
    }

    @After("execution(void ru.dragonestia.picker.repository.UserRepository.unlinkWithRoom(..))")
    void onUnlinkUsers() {
        countAllUsers();
    }

    @After("execution(void ru.dragonestia.picker.repository.UserRepository.onRemoveRoom(..))")
    void onRemoveRoom() {
        countAllUsers();
    }

    private void countAllUsers() {
        totalUsers.set(userRepository.countAllUsers());
    }

    @After(value = "execution(void ru.dragonestia.picker.repository.NodeRepository.create(ru.dragonestia.picker.model.Node)) && args(node)", argNames = "node")
    void onCreateNode(Node node) {
        var nodeId = node.getIdentifier();
        var gauge = Gauge.builder("roompicker_node_users_total", () -> nodeUsers.getOrDefault(nodeId, 0))
                .tag("nodeId", nodeId)
                .register(meterRegistry);

        nodeGauges.put(nodeId, gauge);

        var counter = Counter.builder("roompicker_picks")
                .tag("nodeId", nodeId)
                .baseUnit("1s")
                .register(meterRegistry);
        pickPerMinute.put(nodeId, counter);
    }

    @After(value = "execution(* ru.dragonestia.picker.repository.NodeRepository.delete(ru.dragonestia.picker.model.Node)) && args(node)", argNames = "node")
    void onDeleteNode(Node node) {
        meterRegistry.remove(nodeGauges.remove(node.getIdentifier()));
        meterRegistry.remove(pickPerMinute.remove(node.getIdentifier()));
        nodeUsers.remove(node.getIdentifier());
    }

    @AfterReturning(value = "execution(* ru.dragonestia.picker.repository.RoomRepository.pickFree(ru.dragonestia.picker.model.Node, *)) && args(node, ..)", argNames = "node")
    void onPickRoom(Node node) {
        pickPerMinute.get(node.getIdentifier()).increment();
    }

    @Scheduled(fixedDelay = 3_000)
    void updateUserMetrics() {
        nodeUsers.putAll(userRepository.countUsersForNodes());
    }
}
