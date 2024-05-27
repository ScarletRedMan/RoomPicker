package ru.dragonestia.picker.cp.component;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.extern.log4j.Log4j2;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.model.instance.Instance;
import ru.dragonestia.picker.api.model.instance.InstanceId;
import ru.dragonestia.picker.api.model.room.RoomId;
import ru.dragonestia.picker.cp.repository.dto.RoomDTO;
import ru.dragonestia.picker.cp.repository.graphql.AllRooms;
import ru.dragonestia.picker.cp.util.Notifications;
import ru.dragonestia.picker.cp.util.UsingSlots;

import java.util.Comparator;
import java.util.List;

@Log4j2
public class RoomList extends VerticalLayout implements RefreshableTable {

    private final Instance instance;
    private final RoomPickerClient client;
    private final Grid<RoomDTO> roomsGrid;
    private final TextField searchField;
    private List<RoomDTO> cachedRooms;
    private final Span totalEntities = new Span();

    public RoomList(Instance instance, RoomPickerClient client) {
        this.instance = instance;
        this.client = client;

        add(new H2("Rooms"));
        add(searchField = createSearchField());
        add(roomsGrid = createGrid());

        refresh();
    }

    private TextField createSearchField() {
        var field = new TextField("Search room");
        field.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        field.setClearButtonVisible(true);
        field.setHelperText("Press Enter to search");
        field.addValueChangeListener(event -> applySearch(event.getValue()));
        field.setValueChangeMode(ValueChangeMode.EAGER);
        return field;
    }

    private void applySearch(String input) {
        var temp = input.trim();

        roomsGrid.setItems(cachedRooms.stream()
                .filter(room -> room.getId().startsWith(temp))
                .toList());
    }

    private Grid<RoomDTO> createGrid() {
        var grid = new Grid<>(RoomDTO.class, false);

        grid.addColumn(RoomDTO::getId).setHeader("Identifier").setSortable(true);

        grid.addComponentColumn(room -> {
            var result = new Span();
            if (room.getSlots() == -1) {
                result.setText("Unlimited");
                result.getElement().getThemeList().add("badge contrast");
            } else {
                result.setText(Integer.toString(room.getSlots()));
            }
            return result;
        }).setHeader("Slots").setComparator((room1, room2) -> {
            var r1 = room1.getSlots() == 1? Integer.MAX_VALUE : room1.getSlots();
            var r2 = room2.getSlots() == 1? Integer.MAX_VALUE : room2.getSlots();

            return Integer.compare(r1, r2);
        }).setSortable(true).setTextAlign(ColumnTextAlign.CENTER);

        grid.addColumn(RoomDTO::getCountEntities).setHeader("Entities")
                .setComparator(Comparator.comparingInt(RoomDTO::getCountEntities)).setSortable(true)
                .setTextAlign(ColumnTextAlign.CENTER).setFooter(totalEntities);

        grid.addColumn(room -> Math.max(UsingSlots.getUsingPercentage(room.getSlots(), room.getCountEntities()), 0) + "%")
                .setComparator((room1, room2) -> {
                    var p1 = UsingSlots.getUsingPercentage(room1.getSlots(), room1.getCountEntities());
                    var p2 = UsingSlots.getUsingPercentage(room2.getSlots(), room2.getCountEntities());

                    return Integer.compare(p1, p2);
                }).setHeader("Occupancy").setTextAlign(ColumnTextAlign.CENTER);

        grid.addComponentColumn(room -> {
            var result = new Span();
            if (room.isLocked()) {
                result.setText("Yes");
                result.getElement().getThemeList().add("badge error");
            } else {
                result.setText("No");
            }
            return result;
        }).setComparator((room1, room2) -> Boolean.compare(room1.isLocked(), room2.isLocked())).setSortable(true)
                .setHeader("Locked").setTextAlign(ColumnTextAlign.CENTER);

        grid.addComponentColumn(this::createManageButtons).setFrozenToEnd(true)
                .setTextAlign(ColumnTextAlign.END).setHeader(createRefreshButton());

        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        return grid;
    }

    private HorizontalLayout createManageButtons(RoomDTO room) {
        var layout = new HorizontalLayout(JustifyContentMode.END);

        {
            var button = new Button("Details");
            button.addClickListener(event -> clickDetailsButton(room));
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            layout.add(button);
        }

        {
            var button = new Button("Remove");
            button.addClickListener(event -> clickRemoveButton(room));
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
            layout.add(button);
        }

        return layout;
    }

    private void clickDetailsButton(RoomDTO room) {
        getUI().ifPresent(ui -> {
            ui.navigate("/instances/%s/rooms/%s".formatted(instance.id(), room.getId()));
        });
    }

    private void clickRemoveButton(RoomDTO room) {
        var dialog = new Dialog("Confirm room deletion");
        dialog.add(new Html("<p>Confirm that you want to delete room. Enter <b><u>" + room.getId() + "</u></b> to field below and confirm.</p>"));

        var inputField = new TextField();
        inputField.setWidth("100%");
        dialog.add(inputField);

        { // confirm
            var button = new Button("Confirm");
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
            button.addClickListener(event -> {
                if (!room.getId().equals(inputField.getValue())) {
                    Notifications.error("Invalid input");
                    return;
                }

                removeRoom(room);
                Notifications.success("Room <b>" + room.getId() + "</b> was successfully removed!");
                dialog.close();
            });

            dialog.getFooter().add(button);
        }

        { // cancel
            var button = new Button("Cancel", event -> dialog.close());
            button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            dialog.getFooter().add(button);
        }

        dialog.open();
    }

    public void removeRoom(RoomDTO room) {
        client.getRoomRepository().deleteRoom(InstanceId.of(room.getInstanceId()), RoomId.of(room.getId()));
        refresh();
    }

    @Override
    public void refresh() {
        cachedRooms = client.getRestTemplate().executeGraphQL(AllRooms.query(instance.id().getValue())).getAllRooms()
                .stream()
                .map(room -> (RoomDTO) room)
                .toList();
        applySearch(searchField.getValue());

        int entities = 0;
        for (var room: cachedRooms) {
            entities += room.getCountEntities();
        }
        totalEntities.setText("Total entities: " + entities);
    }
}
