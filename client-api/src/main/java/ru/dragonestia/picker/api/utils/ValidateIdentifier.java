package ru.dragonestia.picker.api.utils;

public class ValidateIdentifier {

    private ValidateIdentifier() {}

    public static boolean forNode(String nodeId) {
        return nodeId.matches("^(?!-)[a-z\\d-]{0,31}[a-z\\d](?!-)$");
    }

    public static boolean forRoom(String roomId) {
        return roomId.matches("^(?!-)[a-z\\d-]{0,31}[a-z\\d](?!-)$");
    }

    public static boolean forUser(String username) {
        return username.matches("^[aA-zZ\\d-.\\s:@_;]{1,64}$");
    }
}
