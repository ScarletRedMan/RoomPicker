package ru.dragonestia.picker.controller.graphql.entity;

import jakarta.validation.constraints.NotNull;
import ru.dragonestia.picker.model.User;

public class EntityUser {

    private final User user;

    public EntityUser(@NotNull User user) {
        this.user = user;
    }

    public @NotNull String getId() {
        return user.getIdentifier();
    }
}
