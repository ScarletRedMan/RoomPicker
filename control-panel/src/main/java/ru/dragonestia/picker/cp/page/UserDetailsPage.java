package ru.dragonestia.picker.cp.page;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;
import ru.dragonestia.picker.api.repository.RoomRepository;
import ru.dragonestia.picker.api.repository.UserRepository;
import ru.dragonestia.picker.api.repository.details.RoomDetails;
import ru.dragonestia.picker.api.repository.response.type.RRoom;
import ru.dragonestia.picker.api.repository.response.type.RUser;
import ru.dragonestia.picker.cp.util.RouteParamsExtractor;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@PageTitle("User details")
@Route(value = "/users/:userId", layout = MainLayout.class)
public class UserDetailsPage extends VerticalLayout implements BeforeEnterObserver {

    private final UserRepository userRepository;
    private final RouteParamsExtractor paramsExtractor;
    private RUser user;
    private Grid<RRoom.Short> gridRooms;
    private List<RRoom.Short> cachedRooms = new LinkedList<>();

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        user = paramsExtractor.extractUserId(event);

        init();
    }

    private void init() {
        add(new H2("User '%s'".formatted(user.getId())));
        add(new H3("Linked with rooms"));
        add(gridRooms = createGrid());

        update(userRepository.getLinkedRoomsWithUsers(user, RoomRepository.ALL_DETAILS));
    }

    private Grid<RRoom.Short> createGrid() {
        var grid = new Grid<RRoom.Short>();

        grid.addColumn(RRoom.Short::id).setHeader("Room identifier");

        grid.addColumn(RRoom.Short::nodeId).setHeader("Node identifier");

        grid.addColumn(room -> room.details().get(RoomDetails.COUNT_USERS)).setHeader("Users");

        grid.addComponentColumn(room -> {
            var button = new Button("Details");
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            button.addClickListener(event -> {
                getUI().ifPresent(ui -> ui.navigate("/nodes/%s/rooms/%s".formatted(room.nodeId(), room.id())));
            });
            return button;
        }).setHeader("Other");
        return grid;
    }

    public void update(List<RRoom.Short> rooms) {
        gridRooms.setItems(cachedRooms = rooms);
    }
}
