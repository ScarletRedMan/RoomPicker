package ru.dragonestia.picker.cp.page;

import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dragonestia.picker.api.exception.ApiException;
import ru.dragonestia.picker.api.repository.NodeRepository;
import ru.dragonestia.picker.cp.component.NavPath;
import ru.dragonestia.picker.cp.component.NodeList;
import ru.dragonestia.picker.cp.component.RegisterNode;

@Log4j2
@Getter
@PageTitle("Nodes")
@Route("/nodes")
public class NodesPage extends VerticalLayout {

    private final NodeRepository nodeRepository;
    private final RegisterNode registerNode;
    private final NodeList nodeList;

    public NodesPage(@Autowired NodeRepository nodeRepository) {
        super();
        this.nodeRepository = nodeRepository;

        add(new NavPath(new NavPath.Point("Nodes", "/nodes")));
        add(registerNode = createRegisterNodeElement());
        add(new Hr());
        add(nodeList = createNodeListElement());
        nodeList.setRemoveMethod(nodeIdentifier -> {
            nodeRepository.remove(nodeIdentifier);
            nodeList.update(nodeRepository.all());
        });
    }

    protected RegisterNode createRegisterNodeElement() {
        return new RegisterNode(node -> {
            try {
                nodeRepository.register(node);
                return new RegisterNode.Response(false, "");
            } catch (ApiException ex) {
                return new RegisterNode.Response(true, ex.getMessage());
            } finally {
                nodeList.update(nodeRepository.all());
            }
        });
    }

    protected NodeList createNodeListElement() {
        return new NodeList(nodeRepository.all());
    }
}
