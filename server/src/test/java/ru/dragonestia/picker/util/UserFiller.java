package ru.dragonestia.picker.util;

import org.springframework.boot.test.context.TestComponent;
import ru.dragonestia.picker.api.repository.type.EntityIdentifier;
import ru.dragonestia.picker.model.entity.Entity;

import java.util.*;

@TestComponent
public class UserFiller {

    public Set<Entity> createRandomUsers(int amount) {
        var set = new HashSet<Entity>();
        for (int i = 0; i < amount; i++) {
            set.add(new Entity(EntityIdentifier.of(UUID.randomUUID().toString())));
        }
        return set;
    }
}
