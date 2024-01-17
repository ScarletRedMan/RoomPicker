package ru.dragonestia.picker.api.repository.response.type.type;

public enum PickingMode {
    SEQUENTIAL_FILLING("Sequential filling"),
    ROUND_ROBIN("Round Robin"),
    LEAST_PICKED("Least Picked");

    private final String name;

    PickingMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
