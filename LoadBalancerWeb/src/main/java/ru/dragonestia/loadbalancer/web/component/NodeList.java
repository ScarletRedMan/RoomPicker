package ru.dragonestia.loadbalancer.web.component;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import ru.dragonestia.loadbalancer.web.model.Node;

import java.util.List;

public class NodeList extends VerticalLayout {

    private final Grid<Node> nodesGrid;

    public NodeList(List<Node> nodes) {
        super();

        add(new H2("Nodes"));
        add(nodesGrid = createGrid());

        update(nodes);
    }

    private Grid<Node> createGrid() {
        var grid = new Grid<>(Node.class, false);
        grid.addColumn(Node::identifier).setHeader("Identifier");
        grid.addColumn(node -> node.method().getName()).setHeader("Mode");
        return grid;
    }

    public void update(List<Node> nodes) {
        nodesGrid.setItems(nodes);
    }

    private void registerNode(Node node) {
        // TODO: send request for register node and get all nodes

        update(List.of(node));
    }

    public interface OnRegister {

        List<Node> register(Node node);
    }
}
