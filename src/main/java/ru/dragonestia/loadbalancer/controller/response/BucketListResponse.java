package ru.dragonestia.loadbalancer.controller.response;

import java.util.List;

public record BucketListResponse(String node, List<BucketInfo> buckets) {

    public record BucketInfo(String identifier, int slots) {}
}
