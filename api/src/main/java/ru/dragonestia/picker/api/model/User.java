package ru.dragonestia.picker.api.model;

public class User {

    private String id;

    private User() {}

    public User(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof User other) {
            return id.equals(other.id);
        }
        return false;
    }
}
