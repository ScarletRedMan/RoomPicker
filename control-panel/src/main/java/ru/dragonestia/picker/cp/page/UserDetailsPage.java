package ru.dragonestia.picker.cp.page;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.model.room.RoomDetails;
import ru.dragonestia.picker.api.model.room.ShortResponseRoom;
import ru.dragonestia.picker.api.model.user.IUser;
import ru.dragonestia.picker.api.repository.query.user.FindRoomsLinkedWithUser;
import ru.dragonestia.picker.cp.component.RefreshableTable;
import ru.dragonestia.picker.cp.service.SecurityService;
import ru.dragonestia.picker.cp.util.RouteParamsExtractor;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RolesAllowed("USER")
@PageTitle("User details")
@Route(value = "/users/:userId", layout = MainLayout.class)
public class UserDetailsPage extends VerticalLayout implements BeforeEnterObserver, RefreshableTable {

    private final SecurityService securityService;
    private final RouteParamsExtractor paramsExtractor;

    private RoomPickerClient client;
    private IUser user;
    private Grid<ShortResponseRoom> gridRooms;

    @PostConstruct
    void postConstruct() {
        client = securityService.getAuthenticatedAccount().getClient();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        user = paramsExtractor.extractUser(event);

        init();
    }

    private void init() {
        add(new H2("User '%s'".formatted(user.getIdentifier())));
        add(new H3("Linked with rooms"));
        add(gridRooms = createGrid());

        refresh();
    }

    private Grid<ShortResponseRoom> createGrid() {
        var grid = new Grid<ShortResponseRoom>();

        grid.addColumn(ShortResponseRoom::getIdentifier).setHeader("Room identifier").setSortable(true);

        grid.addColumn(ShortResponseRoom::getNodeIdentifier).setHeader("Node identifier").setSortable(true);

        grid.addColumn(room -> room.getDetail(RoomDetails.COUNT_USERS)).setHeader("Users")
                .setComparator((room1, room2) -> {
                    var r1 = Integer.parseInt(Objects.requireNonNull(room1.getDetail(RoomDetails.COUNT_USERS)));
                    var r2 = Integer.parseInt(Objects.requireNonNull(room2.getDetail(RoomDetails.COUNT_USERS)));

                    return Integer.compare(r1, r2);
                }).setSortable(true);

        grid.addComponentColumn(room -> {
            var button = new Button("Details");
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            button.addClickListener(event -> {
                getUI().ifPresent(ui -> ui.navigate("/nodes/%s/rooms/%s".formatted(room.getNodeIdentifier(), room.getIdentifier())));
            });
            return button;
        }).setTextAlign(ColumnTextAlign.END).setFrozenToEnd(true).setHeader(createRefreshButton());

        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        return grid;
    }

    @Override
    public void refresh() {
        List<ShortResponseRoom> cachedRooms = client.getUserRepository()
                .findRoomsLinkedWithUser(FindRoomsLinkedWithUser.withAllDetails(user.getIdentifierObject()));
        gridRooms.setItems(cachedRooms);
    }
}
