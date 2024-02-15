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
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import ru.dragonestia.picker.api.repository.details.RoomDetails;
import ru.dragonestia.picker.api.repository.response.type.RRoom;

import java.util.List;
import java.util.function.Consumer;

@Log4j2
public class RoomList extends VerticalLayout {

    private final String nodeIdentifier;
    private final Grid<RRoom.Short> roomsGrid;
    private final TextField searchField;
    private List<RRoom.Short> cachedRooms;
    private final Span totalUsers = new Span();
    @Setter private Consumer<RRoom.Short> removeMethod;

    public RoomList(String nodeIdentifier, List<RRoom.Short> rooms) {
        this.nodeIdentifier = nodeIdentifier;
        cachedRooms = rooms;

        add(new H2("Rooms"));
        add(searchField = createSearchField());
        add(roomsGrid = createGrid());

        update(rooms);
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
                .filter(room -> room.id().startsWith(temp))
                .toList());
    }

    private Grid<RRoom.Short> createGrid() {
        var grid = new Grid<>(RRoom.Short.class, false);

        grid.addColumn(RRoom.Short::id).setHeader("Identifier").setSortable(true);

        grid.addComponentColumn(room -> {
            var result = new Span();
            if (room.slots() == -1) {
                result.setText("Unlimited");
                result.getElement().getThemeList().add("badge contrast");
            } else {
                result.setText(Integer.toString(room.slots()));
            }
            return result;
        }).setHeader("Slots").setComparator((room1, room2) -> {
            var r1 = room1.slots() == -1? Integer.MAX_VALUE : room1.slots();
            var r2 = room2.slots() == -1? Integer.MAX_VALUE : room2.slots();

            return Integer.compare(r1, r2);
        }).setSortable(true).setTextAlign(ColumnTextAlign.CENTER);

        grid.addColumn(this::getUsers).setHeader("Users")
                .setComparator((room1, room2) -> Integer.compare(getUsers(room1), getUsers(room2))).setSortable(true)
                .setTextAlign(ColumnTextAlign.CENTER).setFooter(totalUsers);

        grid.addColumn(room -> Math.max(UserList.getUsingPercentage(room.slots(), getUsers(room)), 0) + "%")
                .setComparator((room1, room2) -> {
                    var p1 = UserList.getUsingPercentage(room1.slots(), getUsers(room1));
                    var p2 = UserList.getUsingPercentage(room2.slots(), getUsers(room2));

                    return Integer.compare(p1, p2);
                }).setHeader("Occupancy").setTextAlign(ColumnTextAlign.CENTER);

        grid.addComponentColumn(room -> {
            var result = new Span();
            if (room.locked()) {
                result.setText("Yes");
                result.getElement().getThemeList().add("badge error");
            } else {
                result.setText("No");
            }
            return result;
        }).setComparator((room1, room2) -> Boolean.compare(room1.locked(), room2.locked())).setSortable(true)
                .setHeader("Locked").setTextAlign(ColumnTextAlign.CENTER);

        grid.addComponentColumn(this::createManageButtons).setHeader("Manage");

        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        return grid;
    }

    private HorizontalLayout createManageButtons(RRoom.Short room) {
        var layout = new HorizontalLayout();

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

    private void clickDetailsButton(RRoom.Short room) {
        getUI().ifPresent(ui -> {
            ui.navigate("/nodes/" + nodeIdentifier +
                    "/rooms/" + room.id());
        });
    }

    private void clickRemoveButton(RRoom.Short room) {
        var dialog = new Dialog("Confirm room deletion");
        dialog.add(new Html("<p>Confirm that you want to delete room. Enter <b><u>" + room.id() + "</u></b> to field below and confirm.</p>"));

        var inputField = new TextField();
        inputField.setWidth("100%");
        dialog.add(inputField);

        { // confirm
            var button = new Button("Confirm");
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
            button.addClickListener(event -> {
                if (!room.id().equals(inputField.getValue())) {
                    Notifications.error("Invalid input");
                    return;
                }

                removeRemove(room);
                Notifications.success("Room <b>" + room.id() + "</b> was successfully removed!");
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

    public void update(List<RRoom.Short> rooms) {
        cachedRooms = rooms;
        applySearch(searchField.getValue());

        int users = 0;
        for (var room: cachedRooms) {
            users += getUsers(room);
        }
        totalUsers.setText("Total users: " + users);
    }

    private void removeRemove(RRoom.Short room) {
        if (removeMethod != null) {
            removeMethod.accept(room);
        }
    }

    private int getUsers(RRoom.Short room) {
        try {
            return Integer.parseInt(room.details().getOrDefault(RoomDetails.COUNT_USERS, "0"));
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}
