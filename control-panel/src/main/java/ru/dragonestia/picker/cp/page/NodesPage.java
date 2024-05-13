package ru.dragonestia.picker.cp.page;

import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dragonestia.picker.api.exception.ApiException;
import ru.dragonestia.picker.api.repository.InstanceRepository;
import ru.dragonestia.picker.cp.component.NavPath;
import ru.dragonestia.picker.cp.component.NodeList;
import ru.dragonestia.picker.cp.component.RegisterNode;
import ru.dragonestia.picker.cp.service.SecurityService;

@RolesAllowed("USER")
@PageTitle("Nodes")
@RouteAlias(value = "/", layout = MainLayout.class)
@Route(value = "/instances", layout = MainLayout.class)
public class NodesPage extends VerticalLayout {

    private final InstanceRepository instanceRepository;
    private final NodeList nodeList;

    @Autowired
    public NodesPage(SecurityService securityService) {
        this.instanceRepository = securityService.getAuthenticatedAccount().getClient().getNodeRepository();

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
                instanceRepository.saveNode(nodeDefinition);
                return new RegisterNode.Response(false, "");
            } catch (ApiException ex) {
                return new RegisterNode.Response(true, ex.getMessage());
            } finally {
                nodeList.refresh();
            }
        });
    }

    protected NodeList createNodeListElement() {
        return new NodeList(instanceRepository);
    }
}
