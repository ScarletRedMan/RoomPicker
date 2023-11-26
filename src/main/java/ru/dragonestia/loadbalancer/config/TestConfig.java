package ru.dragonestia.loadbalancer.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.dragonestia.loadbalancer.interceptor.DebugInterceptor;
import ru.dragonestia.loadbalancer.model.Node;
import ru.dragonestia.loadbalancer.model.type.LoadBalancingMethod;
import ru.dragonestia.loadbalancer.repository.NodeRepository;

@Profile("test")
@Configuration
@RequiredArgsConstructor
public class TestConfig implements WebMvcConfigurer {

    private final NodeRepository nodeRepository;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(new DebugInterceptor());
    }

    @Bean
    void createNodes() {
        nodeRepository.createNode(new Node("game-servers", LoadBalancingMethod.ROUND_ROBIN));
        nodeRepository.createNode(new Node("game-lobbies", LoadBalancingMethod.LEAST_PICKED));
        nodeRepository.createNode(new Node("hub", LoadBalancingMethod.SEQUENTIAL_FILLING));
    }
}
