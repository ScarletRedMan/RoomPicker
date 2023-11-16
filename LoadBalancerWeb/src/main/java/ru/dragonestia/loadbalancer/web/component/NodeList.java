package ru.dragonestia.loadbalancer.web.component;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import ru.dragonestia.loadbalancer.web.model.Node;

import java.util.List;

public class NodeList extends VerticalLayout {

    private final Grid<Node> nodesGrid;
    private final TextField searchField;
    private List<Node> cachedNodes;

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
                .filter(node -> node.identifier().startsWith(temp))
                .toList());
    }

    private Grid<Node> createGrid() {
        var grid = new Grid<>(Node.class, false);
        grid.addColumn(Node::identifier).setHeader("Identifier");
        grid.addColumn(node -> node.method().getName()).setHeader("Mode");
        return grid;
    }

    public void update(List<Node> nodes) {
        cachedNodes = nodes;
        applySearch(searchField.getValue());
    }

    private void registerNode(Node node) {
        // TODO: send request for register node and get all nodes

        update(List.of(node));
    }
}
