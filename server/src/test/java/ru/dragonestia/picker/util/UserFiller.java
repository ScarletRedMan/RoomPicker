package ru.dragonestia.picker.util;

import org.springframework.boot.test.context.TestComponent;
import ru.dragonestia.picker.api.repository.type.UserIdentifier;
import ru.dragonestia.picker.model.User;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@TestComponent
public class UserFiller {

    public List<User> createRandomUsers(int amount) {
        var list = new LinkedList<User>();
        for (int i = 0; i < amount; i++) {
            list.add(new User(UserIdentifier.of(UUID.randomUUID().toString())));
        }
        return list;
    }
}
