package ru.dragonestia.picker.cp.component;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.Setter;
import ru.dragonestia.picker.api.repository.response.type.RNode;

import java.util.List;
import java.util.function.Consumer;

public class NodeList extends VerticalLayout {

    private final Grid<RNode> nodesGrid;
    private final TextField searchField;
    private List<RNode> cachedNodes;
    @Setter private Consumer<String> removeMethod;

    public NodeList(List<RNode> nodes) {
        super();

        cachedNodes = nodes;

        add(new H2("Nodes"));
        add(searchField = createSearchField());
        add(nodesGrid = createGrid());

        update(nodes);
    }

    private TextField createSearchField() {
        var field = new TextField("Search node");
        field.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        field.setClearButtonVisible(true);
        field.setHelperText("Press Enter to search");
        field.addValueChangeListener(event -> applySearch(event.getValue()));
        field.setValueChangeMode(ValueChangeMode.EAGER);
        return field;
    }

    private void applySearch(String input) {
        var temp = input.trim();

        nodesGrid.setItems(cachedNodes.stream()
                .filter(node -> node.getId().startsWith(temp))
                .toList());
    }

    private Grid<RNode> createGrid() {
        var grid = new Grid<>(RNode.class, false);
        grid.addColumn(RNode::getId).setHeader("Identifier");
        grid.addColumn(node -> node.getMode().getName()).setHeader("Mode");
        grid.addComponentColumn(this::createManageButtons).setHeader("Manage");
        return grid;
    }

    private HorizontalLayout createManageButtons(RNode node) {
        var layout = new HorizontalLayout();

        {
            var button = new Button("Details");
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            button.addClickListener(event -> clickDetailsButton(node));
            layout.add(button);
        }

        {
            var button = new Button("Remove");
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
            button.addClickListener(event -> clickRemoveButton(node));
            layout.add(button);
        }

        return layout;
    }

    private void clickDetailsButton(RNode node) {
        getUI().ifPresent(ui -> ui.navigate("/nodes/" + node.getId()));
    }

    private void clickRemoveButton(RNode node) {
        var dialog = new Dialog("Confirm node deletion");
        dialog.add(new Html("<p>Confirm that you want to delete node. Enter <b><u>" + node.getId() + "</u></b> to field below and confirm.</p>"));

        var inputField = new TextField();
        inputField.setWidth("100%");
        dialog.add(inputField);

        { // confirm
            var button = new Button("Confirm");
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
            button.addClickListener(event -> {
                if (!node.getId().equals(inputField.getValue())) {
                    Notifications.error("Invalid input");
                    return;
                }

                removeNode(node);
                Notifications.success("Node <b>" + node.getId() + "</b> was successfully removed!");
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

    public void update(List<RNode> nodes) {
        cachedNodes = nodes;
        applySearch(searchField.getValue());
    }

    private void removeNode(RNode node) {
        if (removeMethod != null) {
            removeMethod.accept(node.getId());
        }
    }
}
