package ru.dragonestia.picker.cp.model.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PickingMode {
    SEQUENTIAL_FILLING("Sequential filling"),
    ROUND_ROBIN("Round Robin"),
    LEAST_PICKED("Least Picked");

    private final String name;
}
