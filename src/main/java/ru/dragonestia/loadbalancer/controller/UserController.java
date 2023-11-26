package ru.dragonestia.loadbalancer.controller;

import org.springframework.web.bind.annotation.*;
import ru.dragonestia.loadbalancer.model.Bucket;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping
    Collection<Bucket> linkedBucketsForUser(@RequestParam(name = "user") String userIdentifier) {
        return null;
    }

    @PostMapping
    String linkUserWithBucket(@RequestParam(name = "user") String userIdentifier,
                              @RequestParam(name = "bucket") String bucketIdentifier,
                              @RequestParam(name = "force") boolean force) {
        return null;
    }

    @DeleteMapping
    String unlinkUsersForBucket(@RequestParam(name = "users") String userIdentifiers,
                                @RequestParam(name = "bucket") String bucketIdentifier) {
        return null;
    }
}
