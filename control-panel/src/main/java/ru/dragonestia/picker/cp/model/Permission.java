package ru.dragonestia.picker.cp.model;

import com.github.javaparser.quality.NotNull;
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
}
