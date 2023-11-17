package ru.dragonestia.loadbalancer.web.component;

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
import ru.dragonestia.loadbalancer.web.model.Bucket;

import java.util.List;

public class BucketList extends VerticalLayout {

    private final Grid<Bucket> bucketsGrid;
    private final TextField searchField;
    private List<Bucket> cachedBuckets;

    public BucketList(List<Bucket> buckets) {
        cachedBuckets = buckets;

        add(new H2("Buckets"));
        add(searchField = createSearchField());
        add(bucketsGrid = createGrid());

        update(buckets);
    }

    private TextField createSearchField() {
        var field = new TextField("Search bucket");
        field.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        field.setClearButtonVisible(true);
        field.setHelperText("Press Enter to search");
        field.addValueChangeListener(event -> applySearch(event.getValue()));
        return field;
    }

    private void applySearch(String input) {
        var temp = input.trim();

        bucketsGrid.setItems(cachedBuckets.stream()
                .filter(bucket -> bucket.getIdentifier().startsWith(temp))
                .toList());
    }

    private Grid<Bucket> createGrid() {
        var grid = new Grid<>(Bucket.class, false);
        grid.addColumn(Bucket::getIdentifier).setHeader("Identifier");
        grid.addComponentColumn(bucket -> {
            var result = new Span();
            if (bucket.getSlots().isUnlimited()) {
                result.setText("Unlimited");
                result.getElement().getThemeList().add("badge contrast");
            } else {
                result.setText(Integer.toString(bucket.getSlots().getSlots()));
            }
            return result;
        }).setHeader("Slots").setTextAlign(ColumnTextAlign.CENTER);
        grid.addComponentColumn(this::createManageButtons).setHeader("Manage");
        return grid;
    }

    private HorizontalLayout createManageButtons(Bucket bucket) {
        var layout = new HorizontalLayout();

        {
            var button = new Button("Details");
            button.addClickListener(event -> clickDetailsButton(bucket));
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            layout.add(button);
        }

        {
            var button = new Button("Remove");
            button.addClickListener(event -> clickRemoveButton(bucket));
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
            layout.add(button);
        }

        return layout;
    }

    private void clickDetailsButton(Bucket bucket) {
        getUI().ifPresent(ui -> {
            ui.navigate("/nodes/" + bucket.getNodeIdentifier() +
                    "/" + bucket.getIdentifier());
        });
    }

    private void clickRemoveButton(Bucket bucket) {
        var dialog = new Dialog("Confirm bucket deletion");
        dialog.add(new Paragraph("Confirm that you want to delete bucket. Enter '" + bucket.getIdentifier() + "' to field below and confirm."));

        var inputField = new TextField();
        dialog.add(inputField);

        { // confirm
            var button = new Button("Confirm");
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
            button.addClickListener(event -> {
                if (!bucket.getIdentifier().equals(inputField.getValue())) {
                    Notification.show("Invalid input", 3000, Notification.Position.TOP_END)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                    return;
                }

                removeBucket(bucket);
                Notification.show("Bucket '" + bucket.getIdentifier() + "' was successfully removed!", 3000, Notification.Position.TOP_END)
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

    public void update(List<Bucket> buckets) {
        cachedBuckets = buckets;
        applySearch(searchField.getValue());
    }

    private void removeBucket(Bucket bucket) {
        // TODO: send remove request and getting list
    }
}
