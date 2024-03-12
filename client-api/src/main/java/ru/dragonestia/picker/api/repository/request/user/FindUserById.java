package ru.dragonestia.picker.api.repository.request.user;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.model.user.UserDetails;
import ru.dragonestia.picker.api.repository.type.UserIdentifier;

import java.util.*;
import java.util.stream.Collectors;

public class FindUserById {

    private final String userId;
    private final Set<UserDetails> details;

    private FindUserById(String userId, Set<UserDetails> details) {
        this.userId = userId;
        this.details = details;
    }

    public @NotNull String getUserId() {
        return userId;
    }

    public @NotNull Set<UserDetails> getDetails() {
        return details;
    }

    @Contract("_ -> new")
    public static @NotNull FindUserById just(@NotNull UserIdentifier userId) {
        return builder()
                .setUserId(userId)
                .build();
    }

    @Contract("_ -> new")
    public static @NotNull FindUserById withAllDetails(@NotNull UserIdentifier userId) {
        return builder()
                .setUserId(userId)
                .setDetails(Arrays.stream(UserDetails.values()).collect(Collectors.toSet()))
                .build();
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String userId = null;
        private Set<UserDetails> details = new HashSet<>();

        private Builder() {}

        @Contract("_ -> this")
        public @NotNull Builder setUserId(@NotNull UserIdentifier identifier) {
            userId = identifier.getValue();
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder setDetails(@NotNull Set<UserDetails> details) {
            this.details = details;
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder appendDetail(@NotNull UserDetails detail) {
            details.add(detail);
            return this;
        }

        public @NotNull FindUserById build() {
            if (userId == null) {
                throw new NullPointerException("User id is null");
            }

            return new FindUserById(userId, Collections.unmodifiableSet(details));
        }
    }
}
