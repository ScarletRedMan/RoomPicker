package ru.dragonestia.picker.cp.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import ru.dragonestia.picker.cp.component.RefreshableTable;
import ru.dragonestia.picker.cp.repository.dto.EntityDTO;
import ru.dragonestia.picker.cp.repository.dto.RoomDTO;
import ru.dragonestia.picker.cp.repository.graphql.EntityRooms;
import ru.dragonestia.picker.cp.service.SessionService;
import ru.dragonestia.picker.cp.util.RouteParamExtractor;
import ru.dragonestia.picker.cp.view.layout.MainLayout;

import java.util.List;

@PageTitle("Entity details")
@Route(value = "/entities/:entityId", layout = MainLayout.class)
public class EntityView extends SecuredView implements RefreshableTable {

    private EntityDTO entity;
    private Grid<RoomDTO> gridRooms;

    public EntityView(SessionService service, RouteParamExtractor paramExtractor) {
        super(service, paramExtractor);
    }

    @Override
    protected void preRender(RouteParameters routeParams) {
        entity = getParamsExtractor().entity(routeParams);
    }

    @Override
    protected void render() {
        add(new H2("Entity '%s'".formatted(entity.getId())));
        add(new H3("Linked with rooms"));
        add(gridRooms = createGrid());

        refresh();
    }

    private Grid<RoomDTO> createGrid() {
        var grid = new Grid<RoomDTO>();

        grid.addColumn(RoomDTO::getId).setHeader("Room identifier").setSortable(true);

        grid.addColumn(RoomDTO::getInstanceId).setHeader("Instance identifier").setSortable(true);

        grid.addColumn(RoomDTO::getCountEntities).setHeader("Entities")
                .setComparator((room1, room2) -> {
                    var r1 = room1.getCountEntities();
                    var r2 = room2.getCountEntities();

                    return Integer.compare(r1, r2);
                }).setSortable(true);

        grid.addComponentColumn(room -> {
            var button = new Button("Details");
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            button.addClickListener(event -> {
                getUI().ifPresent(ui -> ui.navigate("/instances/%s/rooms/%s".formatted(room.getInstanceId(), room.getId())));
            });
            return button;
        }).setTextAlign(ColumnTextAlign.END).setFrozenToEnd(true).setHeader(createRefreshButton());

        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        return grid;
    }

    @Override
    public void refresh() {
        List<RoomDTO> cachedRooms = getClient().getRestTemplate().executeGraphQL(EntityRooms.query(entity.getId())).getEntityById().getRooms().stream().map(entity -> (RoomDTO) entity).toList();
        gridRooms.setItems(cachedRooms);
    }
}
