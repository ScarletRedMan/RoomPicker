package ru.dragonestia.picker.cp.view;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.*;
import ru.dragonestia.picker.api.model.entity.EntityId;
import ru.dragonestia.picker.api.model.room.Room;
import ru.dragonestia.picker.cp.component.AddEntities;
import ru.dragonestia.picker.cp.component.NavPath;
import ru.dragonestia.picker.cp.service.SessionService;
import ru.dragonestia.picker.cp.util.Notifications;
import ru.dragonestia.picker.cp.component.EntityList;
import ru.dragonestia.picker.cp.util.RouteParamExtractor;
import ru.dragonestia.picker.cp.view.layout.MainLayout;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

@PageTitle("Room details")
@Route(value = "/instances/:instanceId/rooms/:roomId", layout = MainLayout.class)
public class RoomView extends SecuredView {

    private Room room;
    private EntityList entityList;
    private Button lockRoomButton;
    private VerticalLayout roomInfo;

    public RoomView(SessionService sessionService, RouteParamExtractor paramExtractor) {
        super(sessionService, paramExtractor);
    }

    @Override
    protected void preRender(RouteParameters routeParams) {
        room = getParamsExtractor().room(routeParams);
    }

    @Override
    protected void render() {
        add(NavPath.toRoom(room.instanceId().getValue(), room.id().getValue()));
        add(new H2("Room details"));
        printRoomDetails();
        add(new Hr());
        add(new AddEntities(room, (entities, ignoreLimitation) -> appendEntities(room, entities, ignoreLimitation)));
        add(new Hr());
        add(new H2("Entities"));
        add(entityList = new EntityList(room, getClient()));
    }

    private void updateRoomInfo() {
        roomInfo.removeAll();
        roomInfo.add(new Html("<span>Instance identifier: <b>" + room.instanceId() + "</b></span>"));
        roomInfo.add(new Html("<span>Room identifier: <b>" + room.id() + "</b></span>"));
        roomInfo.add(new Html("<span>Slots: <b>" + (room.slots() == -1? "Unlimited" : room.slots()) + "</b></span>"));
        roomInfo.add(new Html("<span>Locked: <b>" + (room.locked()? "Yes" : "No") + "</b></span>"));
    }

    private void printRoomDetails() {
        add(roomInfo = new VerticalLayout());
        roomInfo.setPadding(false);

        updateRoomInfo();
        add(lockRoomButton = new Button("", event -> changeBucketLockedState()));
        setLockRoomButtonState();

        var payload = new TextArea("Payload(" + room.payload().length() + ")");
        payload.setValue(room.payload());
        payload.setReadOnly(true);
        payload.setMinWidth(50, Unit.REM);
        add(payload);
    }

    private void setLockRoomButtonState() {
        if (room.locked()) {
            lockRoomButton.setText("Unlock");
            lockRoomButton.setPrefixComponent(new Icon(VaadinIcon.UNLOCK));
        } else {
            lockRoomButton.setText("Lock");
            lockRoomButton.setPrefixComponent(new Icon(VaadinIcon.LOCK));
        }
    }

    private void changeBucketLockedState() {
        var newValue = !room.locked();
        getClient().getRoomRepository().lockRoom(room, newValue);

        room = getClient().getRoomRepository().getRoom(room.instanceId(), room.id());

        setLockRoomButtonState();
        updateRoomInfo();

        Notifications.success("Success");
    }

    private void appendEntities(Room room, Collection<EntityId> entities, boolean ignoreLimitation) {
        AtomicBoolean validationFail = new AtomicBoolean(false);

        var newEntities = entities.stream()
                .filter(entity -> {
                    if (entity.getValue().matches("^[aA-zZ\\d-.\\s:/@%?!~$)(+=_|;*]+$")) {
                        return true;
                    }

                    validationFail.set(true);
                    return false;
                }).toList();

        getClient().getEntityRepository().linkEntitiesWithRoom(room, entities, ignoreLimitation);
        entityList.refresh();

        if (validationFail.get()) {
            if (newEntities.isEmpty()) {
                Notifications.error("All entities entered were added because they do not comply with the rule for writing the entity identifier");
            } else {
                Notifications.warn("Not all entities entered were added because they do not comply with the rule for writing the entity identifier");
            }
        } else {
            Notifications.success("Success");
        }
    }
}
