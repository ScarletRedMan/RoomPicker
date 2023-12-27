package ru.dragonestia.picker.cp.repository.impl.response;

import ru.dragonestia.picker.cp.model.dto.BucketDTO;

import java.util.List;

public record BucketListResponse(String node, List<BucketDTO> buckets) {}
