package ru.dragonestia.picker.api.repository.response.type;

import ru.dragonestia.picker.api.repository.details.UserDetails;

import java.util.HashMap;
import java.util.Map;

public class RUser {

    private String id;
    private Map<UserDetails, String> details = new HashMap<>();

    private RUser() {}

    public RUser(String id) {
        this.id = id;
        this.details = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void putDetail(UserDetails detail, String value) {
        details.put(detail, value);
    }

    public String getDetail(UserDetails detail) {
        return details.get(detail);
    }

    public Map<UserDetails, String> getDetails() {
        return details;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof RUser other) {
            return id.equals(other.id);
        }
        return false;
    }
}
