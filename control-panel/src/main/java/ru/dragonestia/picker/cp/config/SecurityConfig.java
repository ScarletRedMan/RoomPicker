package ru.dragonestia.picker.cp.config;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.dragonestia.picker.cp.page.LoginPage;
import ru.dragonestia.picker.cp.service.AccountService;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    private AccountService accountService;

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(AntPathRequestMatcher.antMatcher("/static/**")).permitAll();
        });

        http.userDetailsService(accountService);

        super.configure(http);

        http.formLogin(login -> {
            login.successForwardUrl("/instances");
            login.defaultSuccessUrl("/instances");
        });
        setLoginView(http, LoginPage.class);
    }
}
