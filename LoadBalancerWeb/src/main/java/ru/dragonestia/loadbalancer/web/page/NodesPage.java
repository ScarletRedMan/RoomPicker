package ru.dragonestia.loadbalancer.web.page;

import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import ru.dragonestia.loadbalancer.web.component.NavPath;
import ru.dragonestia.loadbalancer.web.component.NodeList;
import ru.dragonestia.loadbalancer.web.component.RegisterNode;
import ru.dragonestia.loadbalancer.web.repository.NodeRepository;

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
            nodeRepository.removeNode(nodeIdentifier);
            nodeList.update(nodeRepository.getNodes());
        });
    }

    protected RegisterNode createRegisterNodeElement() {
        return new RegisterNode(node -> {
            try {
                nodeRepository.registerNode(node);
                return new RegisterNode.Response(false, "");
            } catch (Error ex) {
                return new RegisterNode.Response(true, ex.getMessage());
            } catch (RuntimeException ex) {
                log.throwing(ex);
                return new RegisterNode.Response(true, ex.getMessage());
            } finally {
                nodeList.update(nodeRepository.getNodes());
            }
        });
    }

    protected NodeList createNodeListElement() {
        return new NodeList(nodeRepository.getNodes());
    }
}
