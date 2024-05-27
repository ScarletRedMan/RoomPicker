package ru.dragonestia.picker.cp.repository.dto;

import ru.dragonestia.picker.api.model.instance.type.PickingMethod;

public interface InstanceDTO {

    String getId();

    PickingMethod getMethod();

    boolean isPersist();

    int getCountRooms();
}
