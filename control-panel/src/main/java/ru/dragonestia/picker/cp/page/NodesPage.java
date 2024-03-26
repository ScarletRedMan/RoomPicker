package ru.dragonestia.picker.cp.page;

import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dragonestia.picker.api.exception.ApiException;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.repository.NodeRepository;
import ru.dragonestia.picker.cp.component.NavPath;
import ru.dragonestia.picker.cp.component.NodeList;
import ru.dragonestia.picker.cp.component.RegisterNode;
import ru.dragonestia.picker.cp.service.SecurityService;

@PermitAll
@PageTitle("Nodes")
@RouteAlias(value = "/", layout = MainLayout.class)
@Route(value = "/nodes", layout = MainLayout.class)
public class NodesPage extends VerticalLayout {

    private final NodeRepository nodeRepository;
    private final NodeList nodeList;

    @Autowired
    public NodesPage(RoomPickerClient client, SecurityService securityService) {
        this.nodeRepository = client.getNodeRepository();

        add(NavPath.rootNodes());

        if (securityService.hasRole("NODE_MANAGEMENT")) {
            add(createRegisterNodeElement());
        }

        add(new Hr());
        add(nodeList = createNodeListElement());
    }

    protected RegisterNode createRegisterNodeElement() {
        return new RegisterNode(nodeDefinition -> {
            try {
                nodeRepository.saveNode(nodeDefinition);
                return new RegisterNode.Response(false, "");
            } catch (ApiException ex) {
                return new RegisterNode.Response(true, ex.getMessage());
            } finally {
                nodeList.refresh();
            }
        });
    }

    protected NodeList createNodeListElement() {
        return new NodeList(nodeRepository);
    }
}
