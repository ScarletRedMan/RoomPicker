package ru.dragonestia.loadbalancer.model;

import lombok.NonNull;
import ru.dragonestia.loadbalancer.model.type.LoadBalancingMethod;

public record Node(@NonNull String identifier, @NonNull LoadBalancingMethod method) {}
