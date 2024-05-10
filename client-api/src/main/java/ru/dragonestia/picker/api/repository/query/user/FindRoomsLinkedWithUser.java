package ru.dragonestia.picker.api.repository.query.user;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.model.room.RoomDetails;
import ru.dragonestia.picker.api.repository.type.EntityIdentifier;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class FindRoomsLinkedWithUser {

    private final String userId;
    private final Set<RoomDetails> details;

    private FindRoomsLinkedWithUser(String userId, Set<RoomDetails> details) {
        this.userId = userId;
        this.details = details;
    }

    public @NotNull String getUserId() {
        return userId;
    }

    public @NotNull Set<RoomDetails> getDetails() {
        return details;
    }

    @Contract("_ -> new")
    public static @NotNull FindRoomsLinkedWithUser just(@NotNull EntityIdentifier identifier) {
        return FindRoomsLinkedWithUser.builder()
                .setUserId(identifier)
                .build();
    }

    @Contract("_ -> new")
    public static @NotNull FindRoomsLinkedWithUser withAllDetails(@NotNull EntityIdentifier identifier) {
        return FindRoomsLinkedWithUser.builder()
                .setUserId(identifier)
                .setDetails(Arrays.stream(RoomDetails.values()).collect(Collectors.toSet()))
                .build();
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String userId = null;
        private Set<RoomDetails> details = new HashSet<>();

        private Builder() {}

        @Contract("_ -> this")
        public @NotNull Builder setUserId(@NotNull EntityIdentifier identifier) {
            userId = identifier.getValue();
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder setDetails(@NotNull Set<RoomDetails> details) {
            this.details = details;
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder appendDetail(@NotNull RoomDetails detail) {
            details.add(detail);
            return this;
        }

        public @NotNull FindRoomsLinkedWithUser build() {
            if (userId == null) {
                throw new NullPointerException("User id is null");
            }

            return new FindRoomsLinkedWithUser(userId, details);
        }
    }
}
