package ru.dragonestia.picker.cp.page;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dragonestia.picker.api.model.Node;
import ru.dragonestia.picker.api.model.Room;
import ru.dragonestia.picker.api.repository.NodeRepository;
import ru.dragonestia.picker.api.repository.RoomRepository;
import ru.dragonestia.picker.cp.component.Notifications;
import ru.dragonestia.picker.cp.component.RoomList;
import ru.dragonestia.picker.cp.component.NavPath;
import ru.dragonestia.picker.cp.component.RegisterRoom;

import java.util.List;

@Getter
@PageTitle("Rooms")
@Route("/nodes/:nodeId")
public class NodeDetailsPage extends VerticalLayout implements BeforeEnterObserver {

    private final NodeRepository nodeRepository;
    private final RoomRepository roomRepository;
    private Node node;
    private RegisterRoom registerRoom;
    private RoomList roomList;

    public NodeDetailsPage(@Autowired NodeRepository nodeRepository,
                           @Autowired RoomRepository roomRepository) {

        this.nodeRepository = nodeRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        var nodeIdOpt = event.getRouteParameters().get("nodeId");
        if (nodeIdOpt.isEmpty()) {
            getUI().ifPresent(ui -> ui.navigate("/nodes"));
            return;
        }
        var nodeId = nodeIdOpt.get();
        add(new NavPath(new NavPath.Point("Nodes", "/nodes"), new NavPath.Point(nodeId, "/nodes/" + nodeId)));

        var nodeOpt = nodeRepository.find(nodeId);
        if (nodeOpt.isEmpty()) {
            add(new H2("Error 404"));
            add(new Paragraph("Node not found"));
            Notifications.error("Node <b>'" + nodeId + "'</b> does not exist");
            return;
        }
        node = nodeOpt.get();

        initComponents(node, roomRepository.all(node));
    }

    private void initComponents(Node node, List<Room.Short> rooms) {
        printNodeDetails(node);
        add(new Hr());
        add(registerRoom = new RegisterRoom(node, (room) -> {
            try {
                roomRepository.register(room);
                return new RegisterRoom.Response(false,  null);
            } catch (Error error) {
                return new RegisterRoom.Response(true,  error.getMessage());
            } finally {
                roomList.update(roomRepository.all(node));
            }
        }));
        add(new Hr());
        add(roomList = new RoomList(node.getId(), rooms));
        roomList.setRemoveMethod(room -> {
            roomRepository.remove(node, room);
            roomList.update(roomRepository.all(node));
        });
    }

    private void printNodeDetails(Node node) {
        add(new H2("Node details"));

        var layout = new VerticalLayout();
        layout.add(new Html("<span>Identifier: <b>" + node.getId() + "</b></span>"));
        layout.add(new Html("<span>Mode: <b>" + node.getMode().getName() + "</b></span>"));

        add(layout);
    }
}
