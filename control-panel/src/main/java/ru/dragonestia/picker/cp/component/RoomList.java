package ru.dragonestia.picker.cp.component;

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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Setter;
import ru.dragonestia.picker.cp.model.dto.RoomDTO;

import java.util.List;
import java.util.function.Consumer;

public class RoomList extends VerticalLayout {

    private final String nodeIdentifier;
    private final Grid<RoomDTO> roomsGrid;
    private final TextField searchField;
    private List<RoomDTO> cachedRooms;
    @Setter private Consumer<RoomDTO> removeMethod;

    public RoomList(String nodeIdentifier, List<RoomDTO> buckets) {
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
        return field;
    }

    private void applySearch(String input) {
        var temp = input.trim();

        roomsGrid.setItems(cachedRooms.stream()
                .filter(room -> room.id().startsWith(temp))
                .toList());
    }

    private Grid<RoomDTO> createGrid() {
        var grid = new Grid<>(RoomDTO.class, false);
        grid.addColumn(RoomDTO::id).setHeader("Identifier");
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

    private HorizontalLayout createManageButtons(RoomDTO room) {
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

    private void clickDetailsButton(RoomDTO bucket) {
        getUI().ifPresent(ui -> {
            ui.navigate("/nodes/" + nodeIdentifier +
                    "/rooms/" + bucket.id());
        });
    }

    private void clickRemoveButton(RoomDTO bucket) {
        var dialog = new Dialog("Confirm bucket deletion");
        dialog.add(new Paragraph("Confirm that you want to delete bucket. Enter '" + bucket.id() + "' to field below and confirm."));

        var inputField = new TextField();
        dialog.add(inputField);

        { // confirm
            var button = new Button("Confirm");
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
            button.addClickListener(event -> {
                if (!bucket.id().equals(inputField.getValue())) {
                    Notification.show("Invalid input", 3000, Notification.Position.TOP_END)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                    return;
                }

                removeBucket(bucket);
                Notification.show("Bucket '" + bucket.id() + "' was successfully removed!", 3000, Notification.Position.TOP_END)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
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

    public void update(List<RoomDTO> buckets) {
        cachedRooms = buckets;
        applySearch(searchField.getValue());
    }

    private void removeBucket(RoomDTO bucket) {
        if (removeMethod != null) {
            removeMethod.accept(bucket);
        }
    }
}
