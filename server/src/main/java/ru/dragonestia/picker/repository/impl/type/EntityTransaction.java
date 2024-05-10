package ru.dragonestia.picker.repository.impl.type;

import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.model.room.Room;
import ru.dragonestia.picker.model.entity.Entity;

import java.util.Collection;
import java.util.function.Consumer;

public record EntityTransaction(@NotNull Room room, Collection<Entity> target) {

    public interface Listener extends Consumer<EntityTransaction> {}
}
