package ru.dragonestia.loadbalancer.web.page;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.Getter;
import ru.dragonestia.loadbalancer.web.component.NodeList;
import ru.dragonestia.loadbalancer.web.component.RegisterNode;

import java.util.List;

@Getter
@PageTitle("Nodes")
@Route("/nodes")
public class NodesPage extends VerticalLayout {

    private final RegisterNode registerNode;
    private final NodeList nodeList;

    public NodesPage() {
        super();

        add(registerNode = createRegisterNodeElement());
        add(nodeList = createNodeListElement());
    }

    protected RegisterNode createRegisterNodeElement() {
        return new RegisterNode(node -> {
            nodeList.update(List.of(node)); // remove it later
            return new RegisterNode.Response(false, "Some error"); // TODO
        });
    }

    protected NodeList createNodeListElement() {
        // TODO: getting nodes list
        return new NodeList(List.of());
    }
}
