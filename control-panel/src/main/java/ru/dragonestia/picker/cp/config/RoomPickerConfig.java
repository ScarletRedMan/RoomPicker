package ru.dragonestia.picker.cp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.dragonestia.picker.cp.annotation.ServerURL;

@Configuration
public class RoomPickerConfig {

    @Value("${ROOMPICKER_HOST_URL:http://localhost:8080}")
    private String serverUrl;

    @ServerURL
    @Bean
    String severUrl() {
        return serverUrl;
    }
}
