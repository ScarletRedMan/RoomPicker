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
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.repository.EntityRepository;
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
    private final EntityRepository entityRepository;
    private final MeterRegistry meterRegistry;

    private final AtomicInteger totalUsers = new AtomicInteger(0);
    private final Map<String, NodeData> data = new ConcurrentHashMap<>();

    @PostConstruct
    void init() {
        meterRegistry.gauge("roompicker_total_users", totalUsers);
    }

    @After(value = "execution(* ru.dragonestia.picker.repository.EntityRepository.linkWithRoom(ru.dragonestia.picker.model.room.Room, ..)) && args(room, ..)", argNames = "room")
    void onLinkUsers(Room room) {
        countAllUsers(room);
    }

    @After(value = "execution(void ru.dragonestia.picker.repository.EntityRepository.unlinkWithRoom(ru.dragonestia.picker.model.room.Room, ..)) && args(room, ..)", argNames = "room")
    void onUnlinkUsers(Room room) {
        countAllUsers(room);
    }

    private void countAllUsers(Room room) {
        totalUsers.set(entityRepository.countAllEntities());
    }

    @After(value = "execution(void ru.dragonestia.picker.repository.InstanceRepository.create(ru.dragonestia.picker.model.instance.Instance)) && args(instance)", argNames = "instance")
    void onCreateNode(Instance instance) {
        var nodeId = instance.getId();
        var gauge = Gauge.builder("roompicker_node_users_total", () -> data.get(nodeId.getValue()).users())
                .tag("instanceId", nodeId.getValue())
                .register(meterRegistry);

        var counter = Counter.builder("roompicker_picks")
                .tag("instanceId", nodeId.getValue())
                .baseUnit("1s")
                .register(meterRegistry);

        var lockedGauge = Gauge.builder("roompicker_locked_rooms", () -> data.get(nodeId.getValue()).locked())
                .tag("instanceId", nodeId.getValue())
                .register(meterRegistry);

        var roomsGauge = Gauge.builder("roompicker_rooms", () -> roomRepository.all(instance.getId()).size())
                .tag("instanceId", nodeId.getValue())
                .register(meterRegistry);

        data.put(nodeId.getValue(), new NodeData(gauge, new AtomicInteger(0), counter, new AtomicInteger(0), lockedGauge, roomsGauge));
    }

    @After(value = "execution(* ru.dragonestia.picker.repository.InstanceRepository.delete(ru.dragonestia.picker.model.instance.Instance)) && args(instance)", argNames = "instance")
    void onDeleteNode(Instance instance) {
        var data = this.data.remove(instance.getId().getValue());

        meterRegistry.remove(data.usersGauge());
        meterRegistry.remove(data.picksPerMinute());
        meterRegistry.remove(data.lockedGauge());
        meterRegistry.remove(data.roomsGauge());
    }

    @AfterReturning(value = "execution(* ru.dragonestia.picker.repository.RoomRepository.pick(ru.dragonestia.picker.model.instance.Instance, *)) && args(instance, ..)", argNames = "instance")
    void onPickRoom(Instance instance) {
        data.get(instance.getId().getValue()).picksPerMinute().increment();
    }

    @Scheduled(fixedDelay = 3_000)
    void updateUserMetrics() {
        entityRepository.countEntitiesForInstances().forEach((nodeId, users) -> {
            Optional.ofNullable(data.get(nodeId)).ifPresent(node -> node.users().set(users));
        });

        containerRepository.all().forEach(nodeContainer -> {
            var locked = data.get(nodeContainer.getInstance().getId().getValue()).locked();
            locked.set(0);

            nodeContainer.allRooms().forEach(roomContainer -> {
                if (roomContainer.canBePicked(1)) return;

                locked.incrementAndGet();
            });
        });
    }

    private record NodeData(Gauge usersGauge, AtomicInteger users, Counter picksPerMinute, AtomicInteger locked, Gauge lockedGauge, Gauge roomsGauge) {}
}
