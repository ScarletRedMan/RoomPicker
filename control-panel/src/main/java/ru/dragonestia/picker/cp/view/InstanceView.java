package ru.dragonestia.picker.cp.view;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import ru.dragonestia.picker.api.model.instance.Instance;
import ru.dragonestia.picker.cp.component.RoomList;
import ru.dragonestia.picker.cp.component.NavPath;
import ru.dragonestia.picker.cp.component.RegisterRoom;
import ru.dragonestia.picker.cp.service.SessionService;
import ru.dragonestia.picker.cp.util.RouteParamExtractor;
import ru.dragonestia.picker.cp.view.layout.MainLayout;

@PageTitle("Rooms")
@Route(value = "/instances/:instanceId", layout = MainLayout.class)
public class InstanceView extends SecuredView {

    private Instance instance;
    private RoomList roomList;

    public InstanceView(SessionService sessionService, RouteParamExtractor paramsExtractor) {
        super(sessionService, paramsExtractor);
    }

    @Override
    protected void preRender(RouteParameters routeParams) {
        instance = getParamsExtractor().instance(routeParams);
    }

    @Override
    protected void render() {
        add(NavPath.toInstance(instance.id().getValue()));
        printInstanceDetails(instance);
        add(new Hr());
        add(new RegisterRoom(instance, room -> {
            try {
                getClient().getRoomRepository().createRoom(room.instanceId(), room.id(), room.slots(), room.payload(), room.locked(), room.persist());
                return new RegisterRoom.Response(false,  null);
            } catch (Error error) {
                return new RegisterRoom.Response(true,  error.getMessage());
            } finally {
                roomList.refresh();
            }
        }));
        add(new Hr());
        add(roomList = new RoomList(instance, getClient()));
    }

    private void printInstanceDetails(Instance instance) {
        add(new H2("Instance details"));

        var layout = new VerticalLayout();
        layout.add(new Html("<span>Identifier: <b>" + instance.id() + "</b></span>"));
        layout.add(new Html("<span>Mode: <b>" + instance.method().name() + "</b></span>"));

        add(layout);
    }
}
