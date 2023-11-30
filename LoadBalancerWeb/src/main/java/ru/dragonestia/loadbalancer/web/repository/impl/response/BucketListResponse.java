package ru.dragonestia.loadbalancer.web.repository.impl.response;

import ru.dragonestia.loadbalancer.web.model.Bucket;

import java.util.List;

public record BucketListResponse(String node, List<Bucket> buckets) {}
