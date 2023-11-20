package ru.dragonestia.loadbalancer.web.page;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import ru.dragonestia.loadbalancer.web.component.NavPath;
import ru.dragonestia.loadbalancer.web.model.Bucket;
import ru.dragonestia.loadbalancer.web.model.Node;
import ru.dragonestia.loadbalancer.web.model.type.LoadBalancingMethod;
import ru.dragonestia.loadbalancer.web.model.type.SlotLimit;

@Route("/nodes/:nodeId/buckets/:bucketId")
public class BucketsPage extends VerticalLayout implements BeforeEnterObserver {

    private Bucket bucket;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        var nodeIdOpt = event.getRouteParameters().get("nodeId");
        var bucketIdOpt = event.getRouteParameters().get("bucketId");

        if (nodeIdOpt.isEmpty() || bucketIdOpt.isEmpty()) {
            getUI().ifPresent(ui -> ui.navigate("/nodes"));
            return;
        }
        // TODO: getting bucket
        bucket = Bucket.create(bucketIdOpt.get(), new Node(nodeIdOpt.get(), LoadBalancingMethod.ROUND_ROBIN), SlotLimit.unlimited(), "");

        init();
    }

    private void init() {
        add(new NavPath(new NavPath.Point("Nodes", "/nodes"),
                new NavPath.Point(bucket.getNodeIdentifier(), "/nodes/" + bucket.getNodeIdentifier()),
                new NavPath.Point(bucket.getIdentifier(), "/nodes/" + bucket.getNodeIdentifier() + "/buckets/" + bucket.getIdentifier())));
        add(new H2("Bucket details"));
        printBucketDetails();
        add(new Hr());
        add(new H2("Add users"));
        add(new Hr());
        add(new H2("Users"));
    }

    private void printBucketDetails() {
        add(new Html("<span>Node identifier: <b>" + bucket.getNodeIdentifier() + "</b></span>"));
        add(new Html("<span>Bucket identifier: <b>" + bucket.getIdentifier() + "</b></span>"));
        add(new Html("<span>Slots: <b>" + (bucket.getSlots().isUnlimited()? "Unlimited" : bucket.getSlots().getSlots()) + "</b></span>"));
    }
}
