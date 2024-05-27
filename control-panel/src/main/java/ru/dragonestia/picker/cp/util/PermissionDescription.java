package ru.dragonestia.picker.cp.util;

import com.github.javaparser.quality.NotNull;
import ru.dragonestia.picker.api.model.account.Permission;

import java.util.HashMap;
import java.util.Map;

public class PermissionDescription {

    private final static Map<Permission, String> map;

    static {
        map = new HashMap<>();
        map.put(Permission.NODE_MANAGEMENT, "Create and remove instances");
    }

    private PermissionDescription() {}

    public static String of(@NotNull Permission permission) {
        return map.getOrDefault(permission, permission.name());
    }
}
