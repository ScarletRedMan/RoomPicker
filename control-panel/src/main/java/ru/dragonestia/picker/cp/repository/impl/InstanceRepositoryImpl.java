package ru.dragonestia.picker.cp.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dragonestia.picker.cp.repository.InstanceRepository;
import ru.dragonestia.picker.cp.repository.dto.InstanceDTO;
import ru.dragonestia.picker.cp.repository.graphql.AllInstances;
import ru.dragonestia.picker.cp.service.SessionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstanceRepositoryImpl implements InstanceRepository {

    private final SessionService sessionService;

    @Override
    public List<? extends InstanceDTO> all() {
        return sessionService.getSession().getClient().getRestTemplate().executeGraphQL(AllInstances.query()).getAllInstances();
    }
}
