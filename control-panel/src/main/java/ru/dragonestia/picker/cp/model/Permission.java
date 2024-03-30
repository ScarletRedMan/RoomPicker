package ru.dragonestia.picker.cp.model;

import com.github.javaparser.quality.NotNull;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

public class Permission implements GrantedAuthority {

    private final String authority;

    public Permission(@NotNull String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    @Getter
    public enum Enum {
        // All from ru.dragonestia.picker.model.Permission (server)
        // Except for USER and ADMIN

        NODE_MANAGEMENT("Create and remove nodes"),
        ;

        private final String description;

        Enum(String description) {
            this.description = description;
        }
    }
}
