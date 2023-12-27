package ru.dragonestia.loadbalancer.cp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class ServerConfig {

    @Value("${DLB_HOST_URL:http://localhost:8080/}")
    private String serverUrl;

    @Bean
    URI serverUrl() {
        return URI.create(serverUrl);
    }
}
