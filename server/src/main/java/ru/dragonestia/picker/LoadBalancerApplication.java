package ru.dragonestia.picker;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(
        info = @Info(
                title = "RoomPicker Server",
                version = "0.0.1",
                description = "Game room picker system and load balancer",
                license = @License(name = "GPL-3.0", url = "https://github.com/ScarletRedMan/RoomPicker/blob/master/LICENSE"),
                contact = @Contact(url = "https://github.com/ScarletRedMan", name = "Andrey Terentev", email = "terentev.andrey.2002@gmail.com")
        ),
        servers = {
                @Server(
                        description = "Local server",
                        url = "http://localhost:8080"
                )
        }
)
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableScheduling
public class LoadBalancerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoadBalancerApplication.class, args);
    }
}
