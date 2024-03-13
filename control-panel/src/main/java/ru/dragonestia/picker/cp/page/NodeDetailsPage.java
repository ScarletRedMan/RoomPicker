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
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.model.node.INode;
import ru.dragonestia.picker.cp.component.RoomList;
import ru.dragonestia.picker.cp.component.NavPath;
import ru.dragonestia.picker.cp.component.RegisterRoom;
import ru.dragonestia.picker.cp.util.RouteParamsExtractor;

@Getter
@RequiredArgsConstructor
@PageTitle("Rooms")
@Route(value = "/nodes/:nodeId", layout = MainLayout.class)
public class NodeDetailsPage extends VerticalLayout implements BeforeEnterObserver {

    private final RoomPickerClient client;
    private final RouteParamsExtractor paramsExtractor;

    private INode node;
    private RegisterRoom registerRoom;
    private RoomList roomList;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        node = paramsExtractor.extractNode(event);

        initComponents(node);
    }

    private void initComponents(INode node) {
        add(NavPath.toNode(node.getIdentifier()));
        printNodeDetails(node);
        add(new Hr());
        add(registerRoom = new RegisterRoom(node, roomDefinition -> {
            try {
                client.getRoomRepository().saveRoom(roomDefinition);
                return new RegisterRoom.Response(false,  null);
            } catch (Error error) {
                return new RegisterRoom.Response(true,  error.getMessage());
            } finally {
                roomList.refresh();
            }
        }));
        add(new Hr());
        add(roomList = new RoomList(node, client.getRoomRepository()));
    }

    private void printNodeDetails(INode node) {
        add(new H2("Node details"));

        var layout = new VerticalLayout();
        layout.add(new Html("<span>Identifier: <b>" + node.getIdentifier() + "</b></span>"));
        layout.add(new Html("<span>Mode: <b>" + node.getPickingMethod().name() + "</b></span>"));

        add(layout);
    }
}
