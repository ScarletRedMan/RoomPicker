package ru.dragonestia.loadbalancer.web.model.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoadBalancingMethod {
    SEQUENTIAL_FILLING("Sequential filling"),
    ROUND_ROBIN("Round Robin"),
    LEAST_PICKED("Least Picked");

    private final String name;
}
