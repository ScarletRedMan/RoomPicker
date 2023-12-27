package ru.dragonestia.picker.cp.page;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dragonestia.picker.cp.component.RoomList;
import ru.dragonestia.picker.cp.component.NavPath;
import ru.dragonestia.picker.cp.component.RegisterRoom;
import ru.dragonestia.picker.cp.model.Node;
import ru.dragonestia.picker.cp.model.dto.RoomDTO;
import ru.dragonestia.picker.cp.repository.RoomRepository;
import ru.dragonestia.picker.cp.repository.NodeRepository;

import java.util.List;

@Getter
@PageTitle("Buckets")
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
            Notification.show("Node '" + nodeId + "' does not exist", 3000, Notification.Position.TOP_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        node = nodeOpt.get();

        initComponents(node, roomRepository.all(node));
    }

    private void initComponents(Node node, List<RoomDTO> rooms) {
        printNodeDetails(node);
        add(new Hr());
        add(registerRoom = new RegisterRoom(node, (bucket) -> {
            try {
                roomRepository.register(bucket);
                return new RegisterRoom.Response(false,  null);
            } catch (Error error) {
                return new RegisterRoom.Response(true,  error.getMessage());
            } finally {
                roomList.update(roomRepository.all(node));
            }
        }));
        add(new Hr());
        add(roomList = new RoomList(node.id(), rooms));
        roomList.setRemoveMethod(bucket -> {
            roomRepository.remove(node, bucket);
            roomList.update(roomRepository.all(node));
        });
    }

    private void printNodeDetails(Node node) {
        add(new H2("Node details"));

        var layout = new VerticalLayout();
        layout.add(new Html("<span>Identifier: <b>" + node.id() + "</b></span>"));
        layout.add(new Html("<span>Mode: <b>" + node.mode().getName() + "</b></span>"));

        add(layout);
    }
}
