package ru.dragonestia.picker.cp.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.model.entity.EntityId;
import ru.dragonestia.picker.api.model.room.Room;
import ru.dragonestia.picker.cp.repository.dto.EntityDTO;
import ru.dragonestia.picker.cp.repository.graphql.AllEntities;
import ru.dragonestia.picker.cp.util.UsingSlots;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntityList extends VerticalLayout implements RefreshableTable {

    private final Room room;
    private final RoomPickerClient client;
    private final Button buttonRemove;
    private final Grid<EntityDTO> entitiesGrid;
    private final Span totalEntities = new Span();
    private final Span occupancy = new Span();
    private List<EntityDTO> cachedEntities = new ArrayList<>();

    public EntityList(Room room, RoomPickerClient client) {
        this.room = room;
        this.client = client;

        buttonRemove = createButtonRemove();
        add(entitiesGrid = createEntitiesGrid());

        refresh();
        updateButtonRemove();
    }

    private Button createButtonRemove() {
        var button = new Button("Unlink");
        button.setPrefixComponent(new Icon(VaadinIcon.UNLINK));
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        button.addClickListener(event -> {
            var entities = entitiesGrid.getSelectedItems();
            if (entities.isEmpty()) return;
            client.getEntityRepository().unlinkEntitiesFromRoom(room, entities.stream().map(entity -> EntityId.of(entity.getId())).collect(Collectors.toSet()));
            refresh();
        });
        return button;
    }

    private Grid<EntityDTO> createEntitiesGrid() {
        var grid = new Grid<EntityDTO>();

        grid.addColumn(EntityDTO::getId).setHeader("Entity Identifier").setSortable(true).setFooter(totalEntities);

        grid.addColumn(EntityDTO::getCountRooms).setTextAlign(ColumnTextAlign.CENTER)
                .setHeader("Linked with rooms").setComparator((entity1, entity2) -> {
                    var r1 = entity1.getCountRooms();
                    var r2 = entity2.getCountRooms();

                    return Integer.compare(r1, r2);
                }).setSortable(true).setFooter(occupancy);

        grid.addComponentColumn(this::createManageButton).setTextAlign(ColumnTextAlign.END).setFrozenToEnd(true)
                .setTextAlign(ColumnTextAlign.END).setHeader(createManageTableButtons());

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addSelectionListener(event -> updateButtonRemove());
        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        return grid;
    }

    private Button createManageButton(EntityDTO entity) {
        var button = new Button("Details");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate("/entities/" + entity.getId()));
        });
        return button;
    }

    private HorizontalLayout createManageTableButtons() {
        var layout = new HorizontalLayout();
        layout.setJustifyContentMode(JustifyContentMode.END);

        layout.add(buttonRemove);
        layout.add(createRefreshButton());

        return layout;
    }

    private void updateButtonRemove() {
        var entities = entitiesGrid.getSelectedItems();

        if (entities.isEmpty()) {
            buttonRemove.setEnabled(false);
            buttonRemove.setText("Unlink");
            return;
        }

        buttonRemove.setEnabled(true);
        buttonRemove.setText("Unlink(" + entities.size() + ")");
    }

    @Override
    public void refresh() {
        cachedEntities = client.getRestTemplate().executeGraphQL(AllEntities.query(room.instanceId().getValue(), room.id().getValue()))
                .getRoomById().getEntities().stream().map(entity -> (EntityDTO) entity).toList();
        entitiesGrid.setItems(cachedEntities);
        totalEntities.setText("Total entities: " + cachedEntities.size());
        occupancy.setText("Occupancy: %s".formatted(UsingSlots.getUsingPercentage(room.slots(), cachedEntities.size()) + "%"));
    }
}
