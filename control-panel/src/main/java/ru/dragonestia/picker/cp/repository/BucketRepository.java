package ru.dragonestia.picker.cp.repository;

import ru.dragonestia.picker.cp.model.Bucket;
import ru.dragonestia.picker.cp.model.Node;
import ru.dragonestia.picker.cp.model.dto.BucketDTO;

import java.util.List;
import java.util.Optional;

public interface BucketRepository {

    List<BucketDTO> all(Node node);

    void register(Bucket bucket);

    void remove(Bucket bucket);

    void remove(Node node, BucketDTO bucket);

    Optional<Bucket> find(Node node, String identifier);

    void lock(Bucket bucket, boolean value);
}
