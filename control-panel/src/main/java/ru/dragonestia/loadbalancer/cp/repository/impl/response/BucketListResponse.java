package ru.dragonestia.loadbalancer.cp.repository.impl.response;

import ru.dragonestia.loadbalancer.cp.model.dto.BucketDTO;

import java.util.List;

public record BucketListResponse(String node, List<BucketDTO> buckets) {}
