package ru.dragonestia.picker.cp.component;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.Setter;
import ru.dragonestia.picker.api.model.Room;

import java.util.List;
import java.util.function.Consumer;

public class RoomList extends VerticalLayout {

    private final String nodeIdentifier;
    private final Grid<Room.Short> roomsGrid;
    private final TextField searchField;
    private List<Room.Short> cachedRooms;
    @Setter private Consumer<Room.Short> removeMethod;

    public RoomList(String nodeIdentifier, List<Room.Short> buckets) {
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

    private Grid<Room.Short> createGrid() {
        var grid = new Grid<>(Room.Short.class, false);
        grid.addColumn(Room.Short::id).setHeader("Identifier");
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

    private HorizontalLayout createManageButtons(Room.Short room) {
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

    private void clickDetailsButton(Room.Short bucket) {
        getUI().ifPresent(ui -> {
            ui.navigate("/nodes/" + nodeIdentifier +
                    "/rooms/" + bucket.id());
        });
    }

    private void clickRemoveButton(Room.Short bucket) {
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

    public void update(List<Room.Short> buckets) {
        cachedRooms = buckets;
        applySearch(searchField.getValue());
    }

    private void removeBucket(Room.Short bucket) {
        if (removeMethod != null) {
            removeMethod.accept(bucket);
        }
    }
}
