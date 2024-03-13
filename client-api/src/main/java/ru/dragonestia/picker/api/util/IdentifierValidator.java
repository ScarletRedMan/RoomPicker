package ru.dragonestia.picker.api.util;

import org.jetbrains.annotations.NotNull;

public class IdentifierValidator {

    private IdentifierValidator() {}

    public static boolean forNode(@NotNull String nodeId) {
        return nodeId.matches("^(?!-)[a-z\\d-]{0,31}[a-z\\d](?!-)$");
    }

    public static boolean forRoom(@NotNull String roomId) {
        return roomId.matches("^(?!-)[a-z\\d-]{0,35}[a-z\\d](?!-)$");
    }

    public static boolean forUser(@NotNull String username) {
        return username.matches("^[aA-zZ\\d-.\\s:@_;]{1,64}$");
    }
}
