package ru.dragonestia.picker.cp.page;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dragonestia.picker.api.model.Node;
import ru.dragonestia.picker.api.model.Room;
import ru.dragonestia.picker.api.model.User;
import ru.dragonestia.picker.api.repository.NodeRepository;
import ru.dragonestia.picker.api.repository.RoomRepository;
import ru.dragonestia.picker.api.repository.UserRepository;
import ru.dragonestia.picker.cp.component.AddUsers;
import ru.dragonestia.picker.cp.component.NavPath;
import ru.dragonestia.picker.cp.component.UserList;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

@PageTitle("Room details")
@Route("/nodes/:nodeId/rooms/:roomId")
public class RoomDetailsPage extends VerticalLayout implements BeforeEnterObserver {

    private final NodeRepository nodeRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private Node node;
    private Room room;
    private AddUsers addUsers;
    private UserList userList;
    private Button lockRoomButton;
    private VerticalLayout roomInfo;

    @Autowired
    public RoomDetailsPage(NodeRepository nodeRepository, RoomRepository roomRepository, UserRepository userRepository) {
        this.nodeRepository = nodeRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        var nodeIdOpt = event.getRouteParameters().get("nodeId");
        if (nodeIdOpt.isEmpty()) {
            getUI().ifPresent(ui -> ui.navigate("/nodes"));
            return;
        }

        var roomIdOpt = event.getRouteParameters().get("roomId");
        if (roomIdOpt.isEmpty()) {
            getUI().ifPresent(ui -> ui.navigate("/rooms/" + nodeIdOpt.get()));
            return;
        }

        var nodeId = nodeIdOpt.get();
        var roomId = roomIdOpt.get();
        add(new NavPath(new NavPath.Point("Nodes", "/nodes"),
                new NavPath.Point(nodeId, "/nodes/" + nodeId),
                new NavPath.Point(roomId, "/nodes/" + nodeId + "/rooms/" + roomId)));

        var nodeOpt = nodeRepository.find(nodeId);
        if (nodeOpt.isEmpty()) {
            add(new H2("Error 404"));
            add(new Paragraph("Node not found!"));
            Notification.show("Node '" + nodeId + "' does not exist", 3000, Notification.Position.TOP_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        node = nodeOpt.get();

        var bucketOpt = roomRepository.find(node, roomId);
        if (bucketOpt.isEmpty()) {
            add(new H2("Error 404"));
            add(new Paragraph("Room not found!"));
            Notification.show("Room '" + nodeId + "' does not exist", 3000, Notification.Position.TOP_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        room = bucketOpt.get();

        init();
    }

    private void init() {
        add(new H2("Room details"));
        printRoomDetails();
        add(new Hr());
        add(addUsers = new AddUsers(room, (users, ignoreLimitation) -> appendUsers(room, users, ignoreLimitation)));
        add(new Hr());
        add(new H2("Users"));
        add(userList = new UserList(room, userRepository.all(room)));
    }

    private void updateRoomInfo() {
        roomInfo.removeAll();
        roomInfo.add(new Html("<span>Node identifier: <b>" + room.getNodeId() + "</b></span>"));
        roomInfo.add(new Html("<span>Room identifier: <b>" + room.getId() + "</b></span>"));
        roomInfo.add(new Html("<span>Slots: <b>" + (room.isUnlimited()? "Unlimited" : room.getSlots()) + "</b></span>"));
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
        try {
            roomRepository.lock(room, newValue);
        } catch (Error error) {
            Notification.show(error.getMessage(), 3000, Notification.Position.TOP_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        room.setLocked(newValue);
        setLockRoomButtonState();
        updateRoomInfo();

        Notification.show("Success", 3000, Notification.Position.TOP_END)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void appendUsers(Room room, Collection<User> users, boolean ignoreLimitation) {
        AtomicBoolean validationFail = new AtomicBoolean(false);

        var newUsers = users.stream()
                .filter(user -> {
                    if (user.getId().matches("^[aA-zZ\\d-.\\s:/@%?!~$)(+=_|;*]+$")) {
                        return true;
                    }

                    validationFail.set(true);
                    return false;
                }).toList();

        userRepository.linkWithRoom(room, newUsers, ignoreLimitation);
        userList.update(userRepository.all(room));

        if (validationFail.get()) {
            if (newUsers.isEmpty()) {
                Notification.show("All users entered were added because they do not comply with the rule for writing the user identifier", 3000, Notification.Position.TOP_END)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            } else {
                Notification.show("Not all users entered were added because they do not comply with the rule for writing the user identifier", 3000, Notification.Position.TOP_END)
                        .addThemeVariants(NotificationVariant.LUMO_WARNING);
            }
        } else {
            Notification.show("Success", 3000, Notification.Position.TOP_END)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }
    }
}
