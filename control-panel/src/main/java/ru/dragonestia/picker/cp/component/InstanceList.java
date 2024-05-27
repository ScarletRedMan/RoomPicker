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
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.model.instance.InstanceId;
import ru.dragonestia.picker.cp.repository.dto.InstanceDTO;
import ru.dragonestia.picker.cp.repository.graphql.AllInstances;
import ru.dragonestia.picker.cp.util.Notifications;

import java.util.Comparator;
import java.util.List;

public class InstanceList extends VerticalLayout implements RefreshableTable {

    private final RoomPickerClient client;
    private final Grid<InstanceDTO> instancesGrid;
    private final TextField searchField;
    private List<InstanceDTO> cachedInstances;

    public InstanceList(RoomPickerClient client) {
        this.client = client;

        add(new H2("Instances"));
        add(searchField = createSearchField());
        add(instancesGrid = createGrid());

        refresh();
    }

    private TextField createSearchField() {
        var field = new TextField("Search instance");
        field.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        field.setClearButtonVisible(true);
        field.setHelperText("Press Enter to search");
        field.addValueChangeListener(event -> applySearch(event.getValue()));
        field.setValueChangeMode(ValueChangeMode.EAGER);
        return field;
    }

    private void applySearch(String input) {
        var temp = input.trim();

        var instances = cachedInstances.stream()
                .filter(instance -> instance.getId().startsWith(temp))
                .map(instance -> (InstanceDTO) instance)
                .toList();

        instancesGrid.setItems(instances);
    }

    private Grid<InstanceDTO> createGrid() {
        var grid = new Grid<>(InstanceDTO.class, false);

        grid.addComponentColumn(instance -> {
            if (instance.isPersist()) {
                return new Span(instance.getId());
            }

            var result = new Span(instance.getId());
            result.add(grayBadge("(temp)"));
            return result;
        }).setHeader("Identifier").setComparator(Comparator.comparing(InstanceDTO::getId)).setSortable(true);

        grid.addColumn(instance -> instance.getMethod().name()).setHeader("Mode").setSortable(true);

        grid.addComponentColumn(this::createManageButtons).setFrozenToEnd(true)
                .setTextAlign(ColumnTextAlign.END).setHeader(createRefreshButton());

        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        return grid;
    }

    private HorizontalLayout createManageButtons(InstanceDTO instance) {
        var layout = new HorizontalLayout(JustifyContentMode.END);

        {
            var button = new Button("Details");
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            button.addClickListener(event -> clickDetailsButton(instance));
            layout.add(button);
        }

        {
            var button = new Button("Remove");
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
            button.addClickListener(event -> clickRemoveButton(instance));
            layout.add(button);
        }

        return layout;
    }

    private void clickDetailsButton(InstanceDTO instance) {
        getUI().ifPresent(ui -> ui.navigate("/instances/" + instance.getId()));
    }

    private void clickRemoveButton(InstanceDTO instance) {
        var dialog = new Dialog("Confirm instance deletion");
        dialog.add(new Html("<p>Confirm that you want to delete instance. Enter <b><u>" + instance.getId() + "</u></b> to field below and confirm.</p>"));

        var inputField = new TextField();
        inputField.setWidth("100%");
        dialog.add(inputField);

        { // confirm
            var button = new Button("Confirm");
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
            button.addClickListener(event -> {
                if (!instance.getId().equals(inputField.getValue())) {
                    Notifications.error("Invalid input");
                    return;
                }

                removeInstance(instance);
                Notifications.success("Instance <b>" + instance.getId() + "</b> was successfully removed!");
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

    private void removeInstance(InstanceDTO instance) {
        client.getInstanceRepository().deleteInstance(InstanceId.of(instance.getId()));
        refresh();
    }

    @Override
    public void refresh() {
        cachedInstances = client.getRestTemplate().executeGraphQL(AllInstances.query()).getAllInstances().stream()
                .map(instance -> (InstanceDTO) instance)
                .toList();
        applySearch(searchField.getValue());
    }

    private Span grayBadge(String text) {
        var span = new Span(text);
        span.getElement().getThemeList().add("badge contrast");
        return span;
    }
}
