package ru.dragonestia.picker.cp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.cp.annotation.ServerURL;
import ru.dragonestia.picker.cp.model.Account;
import ru.dragonestia.picker.cp.model.provider.AccountProvider;

@Configuration
public class RoomPickerConfig {

    @Value("${ROOMPICKER_HOST_URL:http://localhost:8080}")
    private String serverUrl;

    @Value("${ROOMPICKER_ADMIN_USERNAME:admin}")
    private String adminUsername;

    @Value("${ROOMPICKER_ADMIN_PASSWORD:qwerty123}")
    private String adminPassword;

    @ServerURL
    @Bean
    String severUrl() {
        return serverUrl;
    }

    @Bean
    RoomPickerClient adminClient() {
        return new RoomPickerClient(serverUrl, "admin", "qwerty123");
    }

    @Bean
    AccountProvider accountProvider() {
        return response -> new Account(response, new RoomPickerClient(serverUrl, response.getUsername(), response.getPassword()));
    }
}
