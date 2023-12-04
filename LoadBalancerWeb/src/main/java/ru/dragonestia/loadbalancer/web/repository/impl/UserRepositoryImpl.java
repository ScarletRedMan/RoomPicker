package ru.dragonestia.loadbalancer.web.repository.impl;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.client.HttpClientErrorException;
import ru.dragonestia.loadbalancer.web.model.Bucket;
import ru.dragonestia.loadbalancer.web.model.User;
import ru.dragonestia.loadbalancer.web.repository.UserRepository;
import ru.dragonestia.loadbalancer.web.repository.impl.response.BucketUserListResponse;

import java.net.URI;
import java.util.Collection;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@SpringComponent
public class UserRepositoryImpl implements UserRepository {

    private final RestUtil rest;

    @Override
    public void linkWithBucket(Bucket bucket, Collection<User> users) {
        // TODO
    }

    @Override
    public void unlinkFromBucket(Bucket bucket, Collection<User> users) {
        // TODO
    }

    @Override
    public List<User> all(Bucket bucket) {
        try {
            var response = rest.get(URI.create("/nodes/%s/buckets/%s/users".formatted(bucket.getNodeIdentifier(), bucket.getIdentifier())),
                    BucketUserListResponse.class,
                    params -> {});

            return response.users();
        } catch (Exception ex) {
            log.throwing(ex);
            throw new Error("Internal error");
        }
    }
}
