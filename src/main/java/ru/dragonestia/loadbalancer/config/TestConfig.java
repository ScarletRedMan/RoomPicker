package ru.dragonestia.loadbalancer.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.dragonestia.loadbalancer.interceptor.DebugInterceptor;
import ru.dragonestia.loadbalancer.model.Bucket;
import ru.dragonestia.loadbalancer.model.Node;
import ru.dragonestia.loadbalancer.model.type.LoadBalancingMethod;
import ru.dragonestia.loadbalancer.model.type.SlotLimit;
import ru.dragonestia.loadbalancer.repository.BucketRepository;
import ru.dragonestia.loadbalancer.repository.NodeRepository;

import java.util.UUID;

@Profile("test")
@Configuration
@RequiredArgsConstructor
public class TestConfig implements WebMvcConfigurer {

    private final NodeRepository nodeRepository;
    private final BucketRepository bucketRepository;

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
            bucketRepository.createBucket(Bucket.create("test-" + i, node, SlotLimit.of(5 * i), "Some payload"));
        }

        for (int i = 0; i < 5; i++) {
            bucketRepository.createBucket(Bucket.create(UUID.randomUUID().toString(), node, SlotLimit.unlimited(), "Some payload"));
        }
    }
}
