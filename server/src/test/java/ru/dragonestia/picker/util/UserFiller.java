package ru.dragonestia.picker.util;

import org.springframework.boot.test.context.TestComponent;
import ru.dragonestia.picker.api.repository.type.UserIdentifier;
import ru.dragonestia.picker.model.User;

import java.util.*;

@TestComponent
public class UserFiller {

    public Set<User> createRandomUsers(int amount) {
        var set = new HashSet<User>();
        for (int i = 0; i < amount; i++) {
            set.add(new User(UserIdentifier.of(UUID.randomUUID().toString())));
        }
        return set;
    }
}
