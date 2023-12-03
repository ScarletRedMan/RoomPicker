package ru.dragonestia.loadbalancer.web.page;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dragonestia.loadbalancer.web.component.AddUsers;
import ru.dragonestia.loadbalancer.web.component.NavPath;
import ru.dragonestia.loadbalancer.web.model.Bucket;
import ru.dragonestia.loadbalancer.web.model.Node;
import ru.dragonestia.loadbalancer.web.repository.BucketRepository;
import ru.dragonestia.loadbalancer.web.repository.NodeRepository;

@Route("/nodes/:nodeId/buckets/:bucketId")
public class BucketsPage extends VerticalLayout implements BeforeEnterObserver {

    private final NodeRepository nodeRepository;
    private final BucketRepository bucketRepository;
    private Node node;
    private Bucket bucket;
    private AddUsers addUsers;

    @Autowired
    public BucketsPage(NodeRepository nodeRepository, BucketRepository bucketRepository) {
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

        var bucketIdOpt = event.getRouteParameters().get("bucketId");
        if (bucketIdOpt.isEmpty()) {
            getUI().ifPresent(ui -> ui.navigate("/buckets/" + nodeIdOpt.get()));
            return;
        }

        var nodeId = nodeIdOpt.get();
        var bucketId = bucketIdOpt.get();
        add(new NavPath(new NavPath.Point("Nodes", "/nodes"),
                new NavPath.Point(nodeId, "/nodes/" + nodeId),
                new NavPath.Point(bucketId, "/nodes/" + nodeId + "/buckets/" + bucketId)));

        var nodeOpt = nodeRepository.findNode(nodeId);
        if (nodeOpt.isEmpty()) {
            add(new H2("Error 404"));
            add(new Paragraph("Node not found!"));
            Notification.show("Node '" + nodeId + "' does not exist", 3000, Notification.Position.TOP_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        node = nodeOpt.get();

        var bucketOpt = bucketRepository.find(node, bucketId);
        if (bucketOpt.isEmpty()) {
            add(new H2("Error 404"));
            add(new Paragraph("Bucket not found!"));
            Notification.show("Bucket '" + nodeId + "' does not exist", 3000, Notification.Position.TOP_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        bucket = bucketOpt.get();

        init();
    }

    private void init() {
        add(new H2("Bucket details"));
        printBucketDetails();
        add(new Hr());
        add(addUsers = new AddUsers(bucket));
        add(new Hr());
        add(new H2("Users"));
    }

    private void printBucketDetails() {
        add(new Html("<span>Node identifier: <b>" + bucket.getNodeIdentifier() + "</b></span>"));
        add(new Html("<span>Bucket identifier: <b>" + bucket.getIdentifier() + "</b></span>"));
        add(new Html("<span>Slots: <b>" + (bucket.getSlots().isUnlimited()? "Unlimited" : bucket.getSlots().slots()) + "</b></span>"));
        add(new Html("<span>Locked: <b>" + (bucket.isLocked()? "Yes" : "No") + "</b></span>"));

        var payload = new TextArea("Payload(" + bucket.getPayload().length() + ")");
        payload.setValue(bucket.getPayload());
        payload.setReadOnly(true);
        payload.setMinWidth(50, Unit.REM);
        add(payload);
    }
}
