package ru.dragonestia.loadbalancer.controller.response;

import ru.dragonestia.loadbalancer.model.Bucket;

import java.util.List;

public record BucketListResponse(String node, List<Bucket> buckets) {}
