package ru.dragonestia.picker.aspect;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.repository.UserRepository;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@Aspect
@RequiredArgsConstructor
@Log4j2
public class UserMetricsAspect {

    private final UserRepository userRepository;
    private final MeterRegistry meterRegistry;

    private final AtomicInteger totalUsers = new AtomicInteger(0);

    @PostConstruct
    void init() {
        meterRegistry.gauge("roompicker_total_users", totalUsers);
    }

    @After("execution(void ru.dragonestia.picker.repository.UserRepository.linkWithRoom(..))")
    void onLinkUsers() {
        totalUsers.set(userRepository.countAllUsers());
    }

    @After("execution(void ru.dragonestia.picker.repository.UserRepository.unlinkWithRoom(..))")
    void onUnlinkUsers() {
        totalUsers.set(userRepository.countAllUsers());
    }

    @Scheduled(fixedDelay = 3_000)
    void updateUserMetrics() {
        // TODO: metrics for userRepository.countUsersForNodes()
    }
}
