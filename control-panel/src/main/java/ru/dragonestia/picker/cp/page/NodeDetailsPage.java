package ru.dragonestia.picker.cp.page;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.dragonestia.picker.api.repository.details.RoomDetails;
import ru.dragonestia.picker.api.repository.response.type.RNode;
import ru.dragonestia.picker.api.repository.response.type.RRoom;
import ru.dragonestia.picker.api.repository.NodeRepository;
import ru.dragonestia.picker.api.repository.RoomRepository;
import ru.dragonestia.picker.cp.component.RoomList;
import ru.dragonestia.picker.cp.component.NavPath;
import ru.dragonestia.picker.cp.component.RegisterRoom;
import ru.dragonestia.picker.cp.util.RouteParamsExtractor;

import java.util.List;
import java.util.Set;

@Getter
@RequiredArgsConstructor
@PageTitle("Rooms")
@Route(value = "/nodes/:nodeId", layout = MainLayout.class)
public class NodeDetailsPage extends VerticalLayout implements BeforeEnterObserver {

    private final NodeRepository nodeRepository;
    private final RoomRepository roomRepository;
    private final RouteParamsExtractor paramsExtractor;

    private RNode node;
    private RegisterRoom registerRoom;
    private RoomList roomList;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        node = paramsExtractor.extractNodeId(event);

        initComponents(node, roomRepository.all(node, RoomRepository.ALL_DETAILS));
    }

    private void initComponents(RNode node, List<RRoom.Short> rooms) {
        add(NavPath.toNode(node.getId()));
        printNodeDetails(node);
        add(new Hr());
        add(registerRoom = new RegisterRoom(node, (room) -> {
            try {
                roomRepository.register(room);
                return new RegisterRoom.Response(false,  null);
            } catch (Error error) {
                return new RegisterRoom.Response(true,  error.getMessage());
            } finally {
                roomList.update(roomRepository.all(node, RoomRepository.ALL_DETAILS));
            }
        }));
        add(new Hr());
        add(roomList = new RoomList(node.getId(), rooms));
        roomList.setRemoveMethod(room -> {
            roomRepository.remove(node, room);
            roomList.update(roomRepository.all(node, RoomRepository.ALL_DETAILS));
        });
    }

    private void printNodeDetails(RNode node) {
        add(new H2("Node details"));

        var layout = new VerticalLayout();
        layout.add(new Html("<span>Identifier: <b>" + node.getId() + "</b></span>"));
        layout.add(new Html("<span>Mode: <b>" + node.getMode().getName() + "</b></span>"));

        add(layout);
    }
}
