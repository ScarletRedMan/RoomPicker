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
import ru.dragonestia.picker.api.model.node.INode;
import ru.dragonestia.picker.api.model.room.RoomDetails;
import ru.dragonestia.picker.api.model.room.ShortResponseRoom;
import ru.dragonestia.picker.api.repository.RoomRepository;
import ru.dragonestia.picker.api.repository.query.room.GetAllRooms;

import java.util.List;

@Log4j2
public class RoomList extends VerticalLayout implements RefreshableTable {

    private final INode node;
    private final RoomRepository roomRepository;
    private final Grid<ShortResponseRoom> roomsGrid;
    private final TextField searchField;
    private List<ShortResponseRoom> cachedRooms;
    private final Span totalUsers = new Span();

    public RoomList(INode node, RoomRepository roomRepository) {
        this.node = node;
        this.roomRepository = roomRepository;

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
                .filter(room -> room.getIdentifier().startsWith(temp))
                .toList());
    }

    private Grid<ShortResponseRoom> createGrid() {
        var grid = new Grid<>(ShortResponseRoom.class, false);

        grid.addColumn(ShortResponseRoom::getIdentifier).setHeader("Identifier").setSortable(true);

        grid.addComponentColumn(room -> {
            var result = new Span();
            if (room.getMaxSlots() == -1) {
                result.setText("Unlimited");
                result.getElement().getThemeList().add("badge contrast");
            } else {
                result.setText(Integer.toString(room.getMaxSlots()));
            }
            return result;
        }).setHeader("Slots").setComparator((room1, room2) -> {
            var r1 = room1.hasUnlimitedSlots()? Integer.MAX_VALUE : room1.getMaxSlots();
            var r2 = room2.hasUnlimitedSlots()? Integer.MAX_VALUE : room2.getMaxSlots();

            return Integer.compare(r1, r2);
        }).setSortable(true).setTextAlign(ColumnTextAlign.CENTER);

        grid.addColumn(this::getUsers).setHeader("Users")
                .setComparator((room1, room2) -> Integer.compare(getUsers(room1), getUsers(room2))).setSortable(true)
                .setTextAlign(ColumnTextAlign.CENTER).setFooter(totalUsers);

        grid.addColumn(room -> Math.max(UserList.getUsingPercentage(room.getMaxSlots(), getUsers(room)), 0) + "%")
                .setComparator((room1, room2) -> {
                    var p1 = UserList.getUsingPercentage(room1.getMaxSlots(), getUsers(room1));
                    var p2 = UserList.getUsingPercentage(room2.getMaxSlots(), getUsers(room2));

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

    private HorizontalLayout createManageButtons(ShortResponseRoom room) {
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

    private void clickDetailsButton(ShortResponseRoom room) {
        getUI().ifPresent(ui -> {
            ui.navigate("/nodes/%s/rooms/%s".formatted(node.getIdentifier(), room.getIdentifier()));
        });
    }

    private void clickRemoveButton(ShortResponseRoom room) {
        var dialog = new Dialog("Confirm room deletion");
        dialog.add(new Html("<p>Confirm that you want to delete room. Enter <b><u>" + room.getIdentifier() + "</u></b> to field below and confirm.</p>"));

        var inputField = new TextField();
        inputField.setWidth("100%");
        dialog.add(inputField);

        { // confirm
            var button = new Button("Confirm");
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
            button.addClickListener(event -> {
                if (!room.getIdentifier().equals(inputField.getValue())) {
                    Notifications.error("Invalid input");
                    return;
                }

                removeRoom(room);
                Notifications.success("Room <b>" + room.getIdentifier() + "</b> was successfully removed!");
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

    public void removeRoom(ShortResponseRoom room) {
        roomRepository.removeRoom(room);
        refresh();
    }

    private int getUsers(ShortResponseRoom room) {
        var users = room.getDetail(RoomDetails.COUNT_USERS);
        if (users == null) return 0;
        try {

            return Integer.parseInt(users);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    @Override
    public void refresh() {
        cachedRooms = roomRepository.allRooms(GetAllRooms.withAllDetails(node.getIdentifierObject()));
        applySearch(searchField.getValue());

        int users = 0;
        for (var room: cachedRooms) {
            users += getUsers(room);
        }
        totalUsers.setText("Total users: " + users);
    }
}
