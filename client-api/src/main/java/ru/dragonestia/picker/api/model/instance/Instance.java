package ru.dragonestia.picker.api.model.instance;

import ru.dragonestia.picker.api.model.instance.type.PickingMethod;

public record Instance(InstanceId id, PickingMethod method, boolean persist) {}
