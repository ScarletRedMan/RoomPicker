package ru.dragonestia.loadbalancer.web.page;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dragonestia.loadbalancer.web.component.BucketList;
import ru.dragonestia.loadbalancer.web.component.NavPath;
import ru.dragonestia.loadbalancer.web.component.RegisterBucket;
import ru.dragonestia.loadbalancer.web.model.Bucket;
import ru.dragonestia.loadbalancer.web.model.Node;
import ru.dragonestia.loadbalancer.web.model.type.LoadBalancingMethod;
import ru.dragonestia.loadbalancer.web.model.type.SlotLimit;
import ru.dragonestia.loadbalancer.web.repository.BucketRepository;
import ru.dragonestia.loadbalancer.web.repository.NodeRepository;

import java.util.List;

@Getter
@PageTitle("Buckets")
@Route("/nodes/:nodeId")
public class NodeDetailsPage extends VerticalLayout implements BeforeEnterObserver {

    private final NodeRepository nodeRepository;
    private final BucketRepository bucketRepository;
    private Node node;
    private RegisterBucket registerBucket;
    private BucketList bucketList;

    public NodeDetailsPage(@Autowired NodeRepository nodeRepository,
                           @Autowired BucketRepository bucketRepository) {

        this.nodeRepository = nodeRepository;
        this.bucketRepository = bucketRepository;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        var nodeIdOpt = event.getRouteParameters().get("nodeId");
        if (nodeIdOpt.isEmpty()) {
            getUI().ifPresent(ui -> ui.navigate("/nodes"));
            return;
        }
        var nodeId = nodeIdOpt.get();
        add(new NavPath(new NavPath.Point("Nodes", "/nodes"), new NavPath.Point(nodeId, "/nodes/" + nodeId)));

        var nodeOpt = nodeRepository.findNode(nodeId);
        if (nodeOpt.isEmpty()) {
            add(new H2("Error 404"));
            add(new Paragraph("Node not found"));
            Notification.show("Node '" + nodeId + "' does not exist", 3000, Notification.Position.TOP_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        node = nodeOpt.get();

        initComponents(node, bucketRepository.all(node));
    }

    private void initComponents(Node node, List<Bucket> buckets) {
        printNodeDetails(node);
        add(new Hr());
        add(registerBucket = new RegisterBucket(node, (bucket) -> {
            try {
                bucketRepository.register(bucket);
                return new RegisterBucket.Response(false,  null);
            } catch (Error error) {
                return new RegisterBucket.Response(true,  error.getMessage());
            } finally {
                bucketList.update(bucketRepository.all(node));
            }
        }));
        add(new Hr());
        add(bucketList = new BucketList(node.identifier(), buckets));
        bucketList.setRemoveMethod(bucket -> {
            bucketRepository.remove(bucket);
            bucketList.update(bucketRepository.all(node));
        });
    }

    private void printNodeDetails(Node node) {
        add(new H2("Node details"));

        var layout = new VerticalLayout();
        layout.add(new Html("<span>Identifier: <b>" + node.identifier() + "</b></span>"));
        layout.add(new Html("<span>Mode: <b>" + node.method().getName() + "</b></span>"));

        add(layout);
    }
}
