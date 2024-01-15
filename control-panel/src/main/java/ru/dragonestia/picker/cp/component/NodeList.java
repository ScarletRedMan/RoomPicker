package ru.dragonestia.picker.cp.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Setter;
import ru.dragonestia.picker.api.model.Node;

import java.util.List;
import java.util.function.Consumer;

public class NodeList extends VerticalLayout {

    private final Grid<Node> nodesGrid;
    private final TextField searchField;
    private List<Node> cachedNodes;
    @Setter private Consumer<String> removeMethod;

    public NodeList(List<Node> nodes) {
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
        return field;
    }

    private void applySearch(String input) {
        var temp = input.trim();

        nodesGrid.setItems(cachedNodes.stream()
                .filter(node -> node.getId().startsWith(temp))
                .toList());
    }

    private Grid<Node> createGrid() {
        var grid = new Grid<>(Node.class, false);
        grid.addColumn(Node::getId).setHeader("Identifier");
        grid.addColumn(node -> node.getMode().getName()).setHeader("Mode");
        grid.addComponentColumn(this::createManageButtons).setHeader("Manage");
        return grid;
    }

    private HorizontalLayout createManageButtons(Node node) {
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

    private void clickDetailsButton(Node node) {
        getUI().ifPresent(ui -> ui.navigate("/nodes/" + node.getId()));
    }

    private void clickRemoveButton(Node node) {
        var dialog = new Dialog("Confirm node deletion");
        dialog.add(new Paragraph("Confirm that you want to delete node. Enter '" + node.getId() + "' to field below and confirm."));

        var inputField = new TextField();
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
                Notifications.success("Node '" + node.getId() + "' was successfully removed!");
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

    public void update(List<Node> nodes) {
        cachedNodes = nodes;
        applySearch(searchField.getValue());
    }

    private void removeNode(Node node) {
        if (removeMethod != null) {
            removeMethod.accept(node.getId());
        }
    }
}
