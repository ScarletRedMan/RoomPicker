package ru.dragonestia.loadbalancer.web.repository.impl.response;

import ru.dragonestia.loadbalancer.web.repository.BucketRepository;

import java.util.List;

public record BucketListResponse(String node, List<BucketRepository.BucketInfo> buckets) {}
