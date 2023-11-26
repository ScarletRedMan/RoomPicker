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
import ru.dragonestia.loadbalancer.web.repository.BucketRepository;

import java.util.List;

public class BucketList extends VerticalLayout {

    private final String nodeIdentifier;
    private final Grid<BucketRepository.BucketInfo> bucketsGrid;
    private final TextField searchField;
    private List<BucketRepository.BucketInfo> cachedBuckets;

    public BucketList(String nodeIdentifier, List<BucketRepository.BucketInfo> buckets) {
        this.nodeIdentifier = nodeIdentifier;
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
                .filter(bucket -> bucket.identifier().startsWith(temp))
                .toList());
    }

    private Grid<BucketRepository.BucketInfo> createGrid() {
        var grid = new Grid<>(BucketRepository.BucketInfo.class, false);
        grid.addColumn(BucketRepository.BucketInfo::identifier).setHeader("Identifier");
        grid.addComponentColumn(bucket -> {
            var result = new Span();
            if (bucket.slots() == -1) {
                result.setText("Unlimited");
                result.getElement().getThemeList().add("badge contrast");
            } else {
                result.setText(Integer.toString(bucket.slots()));
            }
            return result;
        }).setHeader("Slots").setTextAlign(ColumnTextAlign.CENTER);
        grid.addComponentColumn(this::createManageButtons).setHeader("Manage");
        return grid;
    }

    private HorizontalLayout createManageButtons(BucketRepository.BucketInfo bucket) {
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

    private void clickDetailsButton(BucketRepository.BucketInfo bucket) {
        getUI().ifPresent(ui -> {
            ui.navigate("/nodes/" + nodeIdentifier +
                    "/buckets/" + bucket.identifier());
        });
    }

    private void clickRemoveButton(BucketRepository.BucketInfo bucket) {
        var dialog = new Dialog("Confirm bucket deletion");
        dialog.add(new Paragraph("Confirm that you want to delete bucket. Enter '" + bucket.identifier() + "' to field below and confirm."));

        var inputField = new TextField();
        dialog.add(inputField);

        { // confirm
            var button = new Button("Confirm");
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
            button.addClickListener(event -> {
                if (!bucket.identifier().equals(inputField.getValue())) {
                    Notification.show("Invalid input", 3000, Notification.Position.TOP_END)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                    return;
                }

                removeBucket(bucket);
                Notification.show("Bucket '" + bucket.identifier() + "' was successfully removed!", 3000, Notification.Position.TOP_END)
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

    public void update(List<BucketRepository.BucketInfo> buckets) {
        cachedBuckets = buckets;
        applySearch(searchField.getValue());
    }

    private void removeBucket(BucketRepository.BucketInfo bucket) {
        // TODO: send remove request and getting list
    }
}
