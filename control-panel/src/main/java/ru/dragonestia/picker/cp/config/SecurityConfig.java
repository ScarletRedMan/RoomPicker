package ru.dragonestia.picker.cp.config;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.cp.model.provider.AccountProvider;
import ru.dragonestia.picker.cp.page.LoginPage;
import ru.dragonestia.picker.cp.service.AccountService;

@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.toString().equals(encodedPassword);
            }
        };
    }

    @Bean
    UserDetailsService userDetailsService(RoomPickerClient adminClient, AccountProvider accountProvider) {
        return new AccountService(adminClient, accountProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {

        });

        super.configure(http);
        setLoginView(http, LoginPage.class);
    }
}
