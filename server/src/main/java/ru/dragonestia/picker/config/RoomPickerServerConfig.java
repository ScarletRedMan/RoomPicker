package ru.dragonestia.picker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoomPickerServerConfig {

    @Value("${ROOMPICKER_ADMIN_USERNAME:admin}")
    private String adminUsername;

    @Value("${ROOMPICKER_ADMIN_PASSWORD:qwerty123}")
    private String adminPassword;

    @Bean
    AdminCredentials adminCredentials() {
        return new AdminCredentials(adminUsername, adminPassword);
    }

    public record AdminCredentials(String username, String password) {}
}
