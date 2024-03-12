package ru.dragonestia.picker.api.model.user;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@Schema(title = "User")
public class ResponseUser implements IUser {

    @Schema(description = "User identifier", example = "test-user")
    private String id;

    @Schema(description = "Additional data requested (Key-Value)")
    private Map<UserDetails, String> details;

    @Internal
    public ResponseUser() {}

    public ResponseUser(@NotNull String id) {
        this.id = id;
        this.details = new HashMap<>();
    }

    @Override
    public @NotNull String getIdentifier() {
        return id;
    }

    @Override
    public @Nullable String getDetail(@NotNull UserDetails detail) {
        return details.get(detail);
    }

    public void putDetail(@NotNull UserDetails detail, @NotNull String value) {
        details.put(detail, value);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof ResponseUser other) {
            return id.equals(other.id);
        }
        return false;
    }

    @Override
    public String toString() {
        return "[ResponseUser id='%s]".formatted(id);
    }
}
