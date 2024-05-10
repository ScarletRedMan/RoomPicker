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
import ru.dragonestia.picker.api.model.node.PickingMethod;
import ru.dragonestia.picker.api.model.room.IRoom;
import ru.dragonestia.picker.api.repository.type.NodeIdentifier;
import ru.dragonestia.picker.api.repository.type.RoomIdentifier;
import ru.dragonestia.picker.api.repository.type.UserIdentifier;
import ru.dragonestia.picker.interceptor.DebugInterceptor;
import ru.dragonestia.picker.model.node.Node;
import ru.dragonestia.picker.model.user.User;
import ru.dragonestia.picker.model.factory.RoomFactory;
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
    private final RoomFactory roomFactory;

    private final Random rand = new Random(0);

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(new DebugInterceptor());
    }

    @Bean
    void createNodes() {
        createNodeWithContent(new Node(NodeIdentifier.of("game-servers"), PickingMethod.ROUND_ROBIN, false));
        createNodeWithContent(new Node(NodeIdentifier.of("game-lobbies"), PickingMethod.LEAST_PICKED, false));
        createNodeWithContent(new Node(NodeIdentifier.of("hub"), PickingMethod.SEQUENTIAL_FILLING, false));
    }

    @SneakyThrows
    private void createNodeWithContent(Node node) {
        nodeRepository.create(node);
        var json = new ObjectMapper().writer().withDefaultPrettyPrinter();

        for (int i = 1; i <= 5; i++) {
            var slots = 5 * i;
            var room = roomFactory.create(RoomIdentifier.of("test-" + i), node, slots, json.writeValueAsString(generatePayload()), false);
            roomRepository.create(room);

            for (int j = 0, n = rand.nextInt(slots + 1); j < n; j++) {
                var user = new User(UserIdentifier.of("test-user-" + rand.nextInt(20)));
                userRepository.linkWithRoom(room, List.of(user), false);
            }
        }

        for (int i = 0; i < 5; i++) {
            var room = roomFactory.create(RoomIdentifier.of(randomUUID().toString()), node, IRoom.UNLIMITED_SLOTS, json.writeValueAsString(generatePayload()), false);
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
