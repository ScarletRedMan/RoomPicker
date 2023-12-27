package ru.dragonestia.picker.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.dragonestia.picker.interceptor.DebugInterceptor;
import ru.dragonestia.picker.model.Bucket;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.model.type.LoadBalancingMethod;
import ru.dragonestia.picker.model.type.SlotLimit;
import ru.dragonestia.picker.repository.BucketRepository;
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
    private final BucketRepository bucketRepository;
    private final UserRepository userRepository;

    private final Random rand = new Random(0);

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(new DebugInterceptor());
    }

    @Bean
    void createNodes() {
        createNodeWithContent(new Node("game-servers", LoadBalancingMethod.ROUND_ROBIN));
        createNodeWithContent(new Node("game-lobbies", LoadBalancingMethod.LEAST_PICKED));
        createNodeWithContent(new Node("hub", LoadBalancingMethod.SEQUENTIAL_FILLING));
    }

    private void createNodeWithContent(Node node) {
        nodeRepository.createNode(node);

        for (int i = 1; i <= 5; i++) {
            var slots = 5 * i;
            var bucket = Bucket.create("test-" + i, node, SlotLimit.of(slots), "Some payload");
            bucketRepository.createBucket(bucket);

            for (int j = 0, n = rand.nextInt(slots + 1); j < n; j++) {
                var user = new User("test-user-" + rand.nextInt(20));
                userRepository.linkWithBucket(bucket, List.of(user), false);
            }
        }

        for (int i = 0; i < 5; i++) {
            var bucket = Bucket.create(randomUUID().toString(), node, SlotLimit.unlimited(), "Some payload");
            bucket.setLocked((i & 1) == 0);
            bucketRepository.createBucket(bucket);
        }
    }

    private UUID randomUUID() {
        byte[] randomBytes = new byte[16];
        rand.nextBytes(randomBytes);
        return UUID.nameUUIDFromBytes(randomBytes);
    }
}
