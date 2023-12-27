package ru.dragonestia.picker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dragonestia.picker.controller.response.BucketUserListResponse;
import ru.dragonestia.picker.controller.response.LinkUsersWithBucketResponse;
import ru.dragonestia.picker.model.Bucket;
import ru.dragonestia.picker.model.Node;
import ru.dragonestia.picker.model.User;
import ru.dragonestia.picker.service.BucketService;
import ru.dragonestia.picker.service.NodeService;
import ru.dragonestia.picker.service.UserService;
import ru.dragonestia.picker.util.NamingValidator;

import java.util.LinkedList;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/nodes/{nodeIdentifier}/buckets/{bucketIdentifier}/users")
public class UserBucketController {

    private final NodeService nodeService;
    private final BucketService bucketService;
    private final UserService userService;

    @GetMapping
    ResponseEntity<BucketUserListResponse> usersInsideBucket(@PathVariable(name = "nodeIdentifier") String nodeId,
                                                             @PathVariable(name = "bucketIdentifier") String bucketId) {

        Bucket bucket;
        try {
            var temp = getNodeAndBucket(nodeId, bucketId);
            bucket = temp.bucket();
        } catch (Error error) {
            return ResponseEntity.notFound().build();
        }

        var users = userService.getBucketUsers(bucket);
        return ResponseEntity.ok(new BucketUserListResponse(bucket.getSlots().getSlots(), users.size(), users));
    }

    @PostMapping
    ResponseEntity<LinkUsersWithBucketResponse> linkUserWithBucket(@PathVariable(name = "nodeIdentifier") String nodeId,
                                                                   @PathVariable(name = "bucketIdentifier") String bucketId,
                                                                   @RequestParam(name = "users") String userIds,
                                                                   @RequestParam(name = "force") boolean force) {

        Bucket bucket;
        try {
            var temp = getNodeAndBucket(nodeId, bucketId);
            bucket = temp.bucket();
        } catch (Error error) {
            return ResponseEntity.status(404).body(new LinkUsersWithBucketResponse(false, error.getMessage()));
        }

        var list = new LinkedList<User>();
        for (var username: userIds.split(",")) {
            if (!NamingValidator.validateUserIdentifier(username)) continue;

            list.add(new User(username));
        }

        try {
            userService.linkUsersWithBucket(bucket, list, force);
        } catch (Error error) {
            return ResponseEntity.status(400).body(new LinkUsersWithBucketResponse(false, error.getMessage()));
        }

        return ResponseEntity.ok(new LinkUsersWithBucketResponse(true, "Success"));
    }

    @DeleteMapping
    ResponseEntity<?> unlinkUsersForBucket(@PathVariable(name = "nodeIdentifier") String nodeId,
                                @PathVariable(name = "bucketIdentifier") String bucketId,
                                @RequestParam(name = "users") String userIdentifiers) {

        Node node;
        Bucket bucket;
        try {
            var temp = getNodeAndBucket(nodeId, bucketId);
            node = temp.node();
            bucket = temp.bucket();
        } catch (Error error) {
            return ResponseEntity.notFound().build();
        }

        return null;
    }

    private record NodeAndBucket(Node node, Bucket bucket) {}

    private NodeAndBucket getNodeAndBucket(String nodeId, String bucketId) {
        if (!NamingValidator.validateNodeIdentifier(nodeId) || !NamingValidator.validateBucketIdentifier(bucketId)) {
            throw new Error();
        }

        var nodeOpt = nodeService.findNode(nodeId);
        if (nodeOpt.isEmpty()) {
            throw new Error();
        }

        var bucketOpt = bucketService.findBucket(Objects.requireNonNull(nodeOpt.get()), bucketId);
        if (bucketOpt.isEmpty()) {
            throw new Error();
        }

        return new NodeAndBucket(nodeOpt.get(), bucketOpt.get());
    }
}
