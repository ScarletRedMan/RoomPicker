package ru.dragonestia.picker.cp.util;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.RouteParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dragonestia.picker.api.exception.DoesNotExistsException;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.model.account.Account;
import ru.dragonestia.picker.api.model.account.AccountId;
import ru.dragonestia.picker.api.model.instance.Instance;
import ru.dragonestia.picker.api.model.instance.InstanceId;
import ru.dragonestia.picker.api.model.room.Room;
import ru.dragonestia.picker.api.model.room.RoomId;
import ru.dragonestia.picker.cp.repository.dto.EntityDTO;
import ru.dragonestia.picker.cp.repository.graphql.EntityData;
import ru.dragonestia.picker.cp.service.SessionService;

@Component
@RequiredArgsConstructor
public class RouteParamExtractor {

    private final SessionService sessionService;

    private RoomPickerClient client() {
        return sessionService.getSession().getClient();
    }

    public Instance instance(RouteParameters params) throws DoesNotExistsException {
        var id = params.get("instanceId").map(InstanceId::of).orElseThrow();
        return client().getInstanceRepository().getInstance(id);
    }

    public Room room(RouteParameters params) throws DoesNotExistsException {
        var instanceId = params.get("instanceId").map(InstanceId::of).orElseThrow();
        var roomId = params.get("roomId").map(RoomId::of).orElseThrow();
        return client().getRoomRepository().getRoom(instanceId, roomId);
    }

    public EntityDTO entity(RouteParameters params) throws DoesNotExistsException {
        var id = params.get("entityId").orElseThrow();
        return client().getRestTemplate().executeGraphQL(EntityData.query(id)).getEntityById();
    }

    public Account account(RouteParameters params) throws DoesNotExistsException {
        var id = params.get("accountId").map(AccountId::of).orElseThrow();
        return client().getAccountRepository().getAccount(id);
    }
}
