package ru.dragonestia.picker.api.impl.util;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EnumUtils {

    private EnumUtils() {}

    public static @NotNull String enumSetToString(@NotNull Set<? extends Enum<?>> set) {
        return String.join(",", set.stream().map(Enum::toString).toList());
    }
}
