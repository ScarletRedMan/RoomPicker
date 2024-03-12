package ru.dragonestia.picker.api.repository.request.user;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.model.user.UserDetails;
import ru.dragonestia.picker.api.repository.type.UserIdentifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SearchUsers {

    private final String searchInput;
    private final Set<UserDetails> details;

    private SearchUsers(String searchInput, Set<UserDetails> details) {
        this.searchInput = searchInput;
        this.details = details;
    }

    public @NotNull String getSearchInput() {
        return searchInput;
    }

    public @NotNull Set<UserDetails> getDetails() {
        return details;
    }

    @Contract("_ -> new")
    public static @NotNull SearchUsers just(@NotNull UserIdentifier searchInput) {
        return SearchUsers.builder()
                .setSearchInput(searchInput)
                .build();
    }

    @Contract("_ -> new")
    public static @NotNull SearchUsers withAllDetails(@NotNull UserIdentifier searchInput) {
        return SearchUsers.builder()
                .setSearchInput(searchInput)
                .setDetails(Arrays.stream(UserDetails.values()).collect(Collectors.toSet()))
                .build();
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String searchInput = null;
        private Set<UserDetails> details = new HashSet<>();

        private Builder() {}

        @Contract("_ -> this")
        public @NotNull Builder setSearchInput(@NotNull UserIdentifier input) {
            searchInput = input.getValue();
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

        public @NotNull SearchUsers build() {
            if (searchInput == null) {
                throw new NullPointerException("SearchInput is null");
            }

            return new SearchUsers(searchInput, Collections.unmodifiableSet(details));
        }
    }
}
