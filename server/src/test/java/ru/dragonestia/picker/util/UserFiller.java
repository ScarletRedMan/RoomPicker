package ru.dragonestia.picker.util;

import org.springframework.boot.test.context.TestComponent;
import ru.dragonestia.picker.model.entity.EntityId;

import java.util.*;

@TestComponent
public class UserFiller {

    public Set<EntityId> createRandomUsers(int amount) {
        var set = new HashSet<EntityId>();
        for (int i = 0; i < amount; i++) {
            set.add(EntityId.of(UUID.randomUUID().toString()));
        }
        return set;
    }
}
