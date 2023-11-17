package ru.dragonestia.loadbalancer.web.page;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.Getter;
import ru.dragonestia.loadbalancer.web.component.BucketList;
import ru.dragonestia.loadbalancer.web.component.NavPath;
import ru.dragonestia.loadbalancer.web.component.RegisterBucket;
import ru.dragonestia.loadbalancer.web.model.Bucket;
import ru.dragonestia.loadbalancer.web.model.Node;
import ru.dragonestia.loadbalancer.web.model.type.LoadBalancingMethod;
import ru.dragonestia.loadbalancer.web.model.type.SlotLimit;

import java.util.List;

@Getter
@PageTitle("Buckets")
@Route("/nodes/:nodeId")
public class NodeDetailsPage extends VerticalLayout implements BeforeEnterObserver {

    private Node node;
    private RegisterBucket registerBucket;
    private BucketList bucketList;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        var nodeIdOpt = event.getRouteParameters().get("nodeId");
        if (nodeIdOpt.isEmpty()) {
            getUI().ifPresent(ui -> ui.navigate("/nodes"));
            return;
        }
        node = new Node(nodeIdOpt.get(), LoadBalancingMethod.ROUND_ROBIN); // TODO: getting node

        // TODO: getting buckets
        initComponents(node, List.of(
                Bucket.create("test-1", node, SlotLimit.unlimited(), "Hello world!"),
                Bucket.create("test-2", node, SlotLimit.of(12), "Hello world!"),
                Bucket.create("test-3", node, SlotLimit.unlimited(), "Hello world!"),
                Bucket.create("test-4", node, SlotLimit.of(32), "Hello world!"),
                Bucket.create("test-5", node, SlotLimit.of(54), "Hello world!")));
    }

    private void initComponents(Node node, List<Bucket> buckets) {
        add(new NavPath(new NavPath.Point("Nodes", "/nodes"),
                new NavPath.Point(node.identifier(), "/nodes/" + node.identifier())));

        printNodeDetails(node);
        add(new Hr());
        add(registerBucket = new RegisterBucket(node, (bucket) -> {
            // TODO: register bucket and getting all buckets
            return new RegisterBucket.Response(false, "");
        }));
        add(new Hr());
        add(bucketList = new BucketList(buckets));
    }

    private void printNodeDetails(Node node) {
        add(new H2("Node details"));

        var layout = new VerticalLayout();
        layout.add(new Html("<span>Identifier: <b>" + node.identifier() + "</b></span>"));
        layout.add(new Html("<span>Mode: <b>" + node.method().getName() + "</b></span>"));

        add(layout);
    }
}
