package ru.dragonestia.picker.controller.response;

import java.util.List;

public record BucketListResponse(String node, List<BucketDTO> buckets) {

    public record BucketDTO(String identifier, int slots, boolean locked) {}
}
