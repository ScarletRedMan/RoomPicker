package ru.dragonestia.picker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.dragonestia.picker.interceptor.DebugInterceptor;
import ru.dragonestia.picker.model.entity.EntityId;
import ru.dragonestia.picker.model.instance.Instance;
import ru.dragonestia.picker.model.entity.Entity;
import ru.dragonestia.picker.model.instance.InstanceId;
import ru.dragonestia.picker.model.instance.type.PickingMethod;
import ru.dragonestia.picker.model.room.RoomId;
import ru.dragonestia.picker.model.room.factory.RoomFactory;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.repository.InstanceRepository;
import ru.dragonestia.picker.repository.EntityRepository;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Profile("test")
@Configuration
@RequiredArgsConstructor
public class TestConfig implements WebMvcConfigurer {

    private final InstanceRepository instanceRepository;
    private final RoomRepository roomRepository;
    private final EntityRepository entityRepository;
    private final RoomFactory roomFactory;

    private final Random rand = new Random(0);

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(new DebugInterceptor());
    }

    @Bean
    void createInstances() {
        createInstanceWithContent(new Instance(InstanceId.of("game-servers"), PickingMethod.ROUND_ROBIN, false));
        createInstanceWithContent(new Instance(InstanceId.of("game-lobbies"), PickingMethod.LEAST_PICKED, false));
        createInstanceWithContent(new Instance(InstanceId.of("hub"), PickingMethod.SEQUENTIAL_FILLING, false));
    }

    @SneakyThrows
    private void createInstanceWithContent(Instance instance) {
        instanceRepository.create(instance);
        var json = new ObjectMapper().writer().withDefaultPrettyPrinter();

        for (int i = 1; i <= 5; i++) {
            var slots = 5 * i;
            var room = roomFactory.create(RoomId.of("test-" + i), instance, slots, json.writeValueAsString(generatePayload()), false);
            roomRepository.create(room);

            for (int j = 0, n = rand.nextInt(slots + 1); j < n; j++) {
                var user = EntityId.of("test-user-" + rand.nextInt(20));
                entityRepository.linkWithRoom(room, List.of(user), false);
            }
        }

        for (int i = 0; i < 5; i++) {
            var room = roomFactory.create(RoomId.random(), instance, -1, json.writeValueAsString(generatePayload()), false);
            room.setLocked((i & 1) == 0);
            roomRepository.create(room);
        }
    }

    private UUID randomUUID() {
        byte[] randomBytes = new byte[16];
        rand.nextBytes(randomBytes);
        return UUID.nameUUIDFromBytes(randomBytes);
    }

    private SomePayload generatePayload() {
        return new SomePayload("Game server",
                "game.dragonestia.ru:" + rand.nextInt(19000, 25000), randomUUID(),
                new Base64().encodeAsString(randomUUID().toString().getBytes()));
    }

    private record SomePayload(String name, String host, UUID gameId, String secureKey) {}
}
