package ru.dragonestia.picker.cp.repository;

import ru.dragonestia.picker.cp.repository.dto.InstanceDTO;

import java.util.List;

public interface InstanceRepository {

    List<? extends InstanceDTO> all();
}
