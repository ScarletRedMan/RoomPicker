package ru.dragonestia.picker.api.repository.request.room;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.dragonestia.picker.api.repository.type.RoomPath;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RemoveRoomsByIds {

    private final Set<RoomPath> paths;

    private RemoveRoomsByIds(Set<RoomPath> paths) {
        this.paths = paths;
    }

    public @NotNull Set<RoomPath> getPaths() {
        return paths;
    }

    public static @NotNull RemoveRoomsByIds just(@NotNull RoomPath path) {
        return RemoveRoomsByIds.builder().appendPath(path).build();
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Set<RoomPath> paths = new HashSet<>();

        private Builder() {}

        @Contract("_ -> this")
        public @NotNull Builder setPaths(@NotNull Set<RoomPath> paths) {
            this.paths = paths;
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder appendPath(@NotNull RoomPath path) {
            paths.add(path);
            return this;
        }

        public @NotNull RemoveRoomsByIds build() {
            return new RemoveRoomsByIds(Collections.unmodifiableSet(paths));
        }
    }
}
