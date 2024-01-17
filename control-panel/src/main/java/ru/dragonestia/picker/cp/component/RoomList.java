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

    public RoomList(String nodeIdentifier, List<RRoom.Short> buckets) {
        this.nodeIdentifier = nodeIdentifier;
        cachedRooms = buckets;

        add(new H2("Rooms"));
        add(searchField = createSearchField());
        add(roomsGrid = createGrid());

        update(buckets);
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
        grid.addColumn(RRoom.Short::id).setHeader("Identifier");

        grid.addComponentColumn(room -> {
            var result = new Span();
            if (room.slots() == -1) {
                result.setText("Unlimited");
                result.getElement().getThemeList().add("badge contrast");
            } else {
                result.setText(Integer.toString(room.slots()));
            }
            return result;
        }).setHeader("Slots").setTextAlign(ColumnTextAlign.CENTER);

        grid.addColumn(this::getUsers).setHeader("Users").setTextAlign(ColumnTextAlign.CENTER).setFooter(totalUsers);

        grid.addColumn(room -> UserList.getUsingPercentage(room.slots(), getUsers(room)))
                .setHeader("Occupancy").setTextAlign(ColumnTextAlign.CENTER);

        grid.addComponentColumn(room -> {
            var result = new Span();
            if (room.locked()) {
                result.setText("Yes");
                result.getElement().getThemeList().add("badge error");
            } else {
                result.setText("No");
            }
            return result;
        }).setHeader("Locked").setTextAlign(ColumnTextAlign.CENTER);

        grid.addComponentColumn(this::createManageButtons).setHeader("Manage");
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

    private void clickDetailsButton(RRoom.Short bucket) {
        getUI().ifPresent(ui -> {
            ui.navigate("/nodes/" + nodeIdentifier +
                    "/rooms/" + bucket.id());
        });
    }

    private void clickRemoveButton(RRoom.Short bucket) {
        var dialog = new Dialog("Confirm bucket deletion");
        dialog.add(new Html("<p>Confirm that you want to delete bucket. Enter <b><u>" + bucket.id() + "</u></b> to field below and confirm.</p>"));

        var inputField = new TextField();
        inputField.setWidth("100%");
        dialog.add(inputField);

        { // confirm
            var button = new Button("Confirm");
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
            button.addClickListener(event -> {
                if (!bucket.id().equals(inputField.getValue())) {
                    Notifications.error("Invalid input");
                    return;
                }

                removeBucket(bucket);
                Notifications.success("Bucket <b>" + bucket.id() + "</b> was successfully removed!");
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

    public void update(List<RRoom.Short> buckets) {
        cachedRooms = buckets;
        applySearch(searchField.getValue());

        int users = 0;
        for (var room: cachedRooms) {
            users += getUsers(room);
        }
        totalUsers.setText("Total users: " + users);
    }

    private void removeBucket(RRoom.Short bucket) {
        if (removeMethod != null) {
            removeMethod.accept(bucket);
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
