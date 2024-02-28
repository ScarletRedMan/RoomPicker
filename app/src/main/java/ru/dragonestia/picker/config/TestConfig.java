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
import ru.dragonestia.picker.api.repository.response.type.type.PickingMode;
import ru.dragonestia.picker.interceptor.DebugInterceptor;
import ru.dragonestia.picker.model.Room;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.model.type.SlotLimit;
import ru.dragonestia.picker.repository.RoomRepository;
import ru.dragonestia.picker.repository.NodeRepository;
import ru.dragonestia.picker.repository.UserRepository;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Profile("test")
@Configuration
@RequiredArgsConstructor
public class TestConfig implements WebMvcConfigurer {

    private final NodeRepository nodeRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    private final Random rand = new Random(0);

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(new DebugInterceptor());
    }

    @Bean
    void createNodes() {
        createNodeWithContent(new Node("game-servers", PickingMode.ROUND_ROBIN, false));
        createNodeWithContent(new Node("game-lobbies", PickingMode.LEAST_PICKED, false));
        createNodeWithContent(new Node("hub", PickingMode.SEQUENTIAL_FILLING, false));
    }

    @SneakyThrows
    private void createNodeWithContent(Node node) {
        nodeRepository.create(node);
        var json = new ObjectMapper().writer().withDefaultPrettyPrinter();

        for (int i = 1; i <= 5; i++) {
            var slots = 5 * i;
            var room = Room.create("test-" + i, node, SlotLimit.of(slots), json.writeValueAsString(generatePayload()), false);
            roomRepository.create(room);

            for (int j = 0, n = rand.nextInt(slots + 1); j < n; j++) {
                var user = new User("test-user-" + rand.nextInt(20));
                userRepository.linkWithRoom(room, List.of(user), false);
            }
        }

        for (int i = 0; i < 5; i++) {
            var room = Room.create(randomUUID().toString(), node, SlotLimit.unlimited(), json.writeValueAsString(generatePayload()), false);
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
