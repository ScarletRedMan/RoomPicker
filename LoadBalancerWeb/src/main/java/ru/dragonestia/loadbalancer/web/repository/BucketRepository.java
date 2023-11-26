package ru.dragonestia.loadbalancer.web.repository;

import ru.dragonestia.loadbalancer.web.model.Node;

import java.util.List;

public interface BucketRepository {

    List<BucketInfo> all(Node node);

    record BucketInfo(String identifier, int slots) {}
}
