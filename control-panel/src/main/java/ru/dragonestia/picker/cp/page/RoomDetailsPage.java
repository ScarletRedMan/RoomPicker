package ru.dragonestia.picker.cp.page;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.model.node.INode;
import ru.dragonestia.picker.api.model.room.IRoom;
import ru.dragonestia.picker.api.model.room.ResponseRoom;
import ru.dragonestia.picker.api.model.user.IUser;
import ru.dragonestia.picker.api.repository.query.user.LinkUsersWithRoom;
import ru.dragonestia.picker.cp.component.AddUsers;
import ru.dragonestia.picker.cp.component.NavPath;
import ru.dragonestia.picker.cp.component.Notifications;
import ru.dragonestia.picker.cp.component.UserList;
import ru.dragonestia.picker.cp.util.RouteParamsExtractor;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@PageTitle("Room details")
@Route(value = "/nodes/:nodeId/rooms/:roomId", layout = MainLayout.class)
public class RoomDetailsPage extends VerticalLayout implements BeforeEnterObserver {

    private final RoomPickerClient client;
    private final RouteParamsExtractor paramsExtractor;

    private INode node;
    private ResponseRoom room;
    private AddUsers addUsers;
    private UserList userList;
    private Button lockRoomButton;
    private VerticalLayout roomInfo;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        node = paramsExtractor.extractNode(event);
        room = (ResponseRoom) paramsExtractor.extractRoom(event, node);

        init();
    }

    private void init() {
        add(NavPath.toRoom(node.getIdentifier(), room.getIdentifier()));
        add(new H2("Room details"));
        printRoomDetails();
        add(new Hr());
        add(addUsers = new AddUsers(room, (users, ignoreLimitation) -> appendUsers(room, users, ignoreLimitation)));
        add(new Hr());
        add(new H2("Users"));
        add(userList = new UserList(room, client.getUserRepository()));
    }

    private void updateRoomInfo() {
        roomInfo.removeAll();
        roomInfo.add(new Html("<span>Node identifier: <b>" + room.getNodeIdentifier() + "</b></span>"));
        roomInfo.add(new Html("<span>Room identifier: <b>" + room.getIdentifier() + "</b></span>"));
        roomInfo.add(new Html("<span>Slots: <b>" + (room.hasUnlimitedSlots()? "Unlimited" : room.getMaxSlots()) + "</b></span>"));
        roomInfo.add(new Html("<span>Locked: <b>" + (room.isLocked()? "Yes" : "No") + "</b></span>"));
    }

    private void printRoomDetails() {
        add(roomInfo = new VerticalLayout());
        roomInfo.setPadding(false);

        updateRoomInfo();
        add(lockRoomButton = new Button("", event -> changeBucketLockedState()));
        setLockRoomButtonState();

        var payload = new TextArea("Payload(" + room.getPayload().length() + ")");
        payload.setValue(room.getPayload());
        payload.setReadOnly(true);
        payload.setMinWidth(50, Unit.REM);
        add(payload);
    }

    private void setLockRoomButtonState() {
        if (room.isLocked()) {
            lockRoomButton.setText("Unlock");
            lockRoomButton.setPrefixComponent(new Icon(VaadinIcon.UNLOCK));
        } else {
            lockRoomButton.setText("Lock");
            lockRoomButton.setPrefixComponent(new Icon(VaadinIcon.LOCK));
        }
    }

    private void changeBucketLockedState() {
        var newValue = !room.isLocked();
        client.getRoomRepository().lockRoom(room.getPath(), newValue);

        room.setLocked(newValue);
        setLockRoomButtonState();
        updateRoomInfo();

        Notifications.success("Success");
    }

    private void appendUsers(IRoom room, Collection<IUser> users, boolean ignoreLimitation) {
        AtomicBoolean validationFail = new AtomicBoolean(false);

        var newUsers = users.stream()
                .filter(user -> {
                    if (user.getIdentifier().matches("^[aA-zZ\\d-.\\s:/@%?!~$)(+=_|;*]+$")) {
                        return true;
                    }

                    validationFail.set(true);
                    return false;
                }).toList();

        client.getUserRepository().linkUsersWithRoom(LinkUsersWithRoom.builder()
                .setNodeId(room.getNodeIdentifierObject())
                .setRoomId(room.getIdentifierObject())
                .setUsers(users.stream().map(IUser::getIdentifierObject).collect(Collectors.toSet()))
                .setIgnoreSlotLimitation(ignoreLimitation)
                .build());
        userList.refresh();

        if (validationFail.get()) {
            if (newUsers.isEmpty()) {
                Notifications.error("All users entered were added because they do not comply with the rule for writing the user identifier");
            } else {
                Notifications.warn("Not all users entered were added because they do not comply with the rule for writing the user identifier");
            }
        } else {
            Notifications.success("Success");
        }
    }
}
