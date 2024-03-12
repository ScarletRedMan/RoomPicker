package ru.dragonestia.picker.api.repository.request.user;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.model.room.RoomDetails;
import ru.dragonestia.picker.api.repository.type.UserIdentifier;

import java.util.HashSet;
import java.util.Set;

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

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String userId = null;
        private Set<RoomDetails> details = new HashSet<>();

        private Builder() {}

        @Contract("_ -> this")
        public @NotNull Builder setUserId(@NotNull UserIdentifier identifier) {
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
