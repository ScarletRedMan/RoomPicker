package ru.dragonestia.loadbalancer.web.repository.impl.response;

import ru.dragonestia.loadbalancer.web.model.dto.BucketDTO;

import java.util.List;

public record BucketListResponse(String node, List<BucketDTO> buckets) {}
