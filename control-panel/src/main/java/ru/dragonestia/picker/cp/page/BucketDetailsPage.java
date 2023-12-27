package ru.dragonestia.picker.cp.page;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dragonestia.picker.cp.component.AddUsers;
import ru.dragonestia.picker.cp.component.NavPath;
import ru.dragonestia.picker.cp.component.UserList;
import ru.dragonestia.picker.cp.model.Bucket;
import ru.dragonestia.picker.cp.model.Node;
import ru.dragonestia.picker.cp.repository.BucketRepository;
import ru.dragonestia.picker.cp.repository.NodeRepository;
import ru.dragonestia.picker.cp.repository.UserRepository;

@Route("/nodes/:nodeId/buckets/:bucketId")
public class BucketDetailsPage extends VerticalLayout implements BeforeEnterObserver {

    private final NodeRepository nodeRepository;
    private final BucketRepository bucketRepository;
    private final UserRepository userRepository;
    private Node node;
    private Bucket bucket;
    private AddUsers addUsers;
    private UserList userList;
    private Button lockBucketButton;
    private VerticalLayout bucketInfo;

    @Autowired
    public BucketDetailsPage(NodeRepository nodeRepository, BucketRepository bucketRepository, UserRepository userRepository) {
        this.nodeRepository = nodeRepository;
        this.bucketRepository = bucketRepository;
        this.userRepository = userRepository;
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
        add(userList = new UserList(bucket, userRepository.all(bucket)));
    }

    private void updateBucketInfo() {
        bucketInfo.removeAll();
        bucketInfo.add(new Html("<span>Node identifier: <b>" + bucket.getNodeIdentifier() + "</b></span>"));
        bucketInfo.add(new Html("<span>Bucket identifier: <b>" + bucket.getIdentifier() + "</b></span>"));
        bucketInfo.add(new Html("<span>Slots: <b>" + (bucket.getSlots().isUnlimited()? "Unlimited" : bucket.getSlots().slots()) + "</b></span>"));
        bucketInfo.add(new Html("<span>Locked: <b>" + (bucket.isLocked()? "Yes" : "No") + "</b></span>"));
    }

    private void printBucketDetails() {
        add(bucketInfo = new VerticalLayout());
        bucketInfo.setPadding(false);

        updateBucketInfo();
        add(lockBucketButton = new Button("", event -> changeBucketLockedState()));
        setLockBucketButtonState();

        var payload = new TextArea("Payload(" + bucket.getPayload().length() + ")");
        payload.setValue(bucket.getPayload());
        payload.setReadOnly(true);
        payload.setMinWidth(50, Unit.REM);
        add(payload);
    }

    private void setLockBucketButtonState() {
        if (bucket.isLocked()) {
            lockBucketButton.setText("Unlock");
            lockBucketButton.setPrefixComponent(new Icon(VaadinIcon.UNLOCK));
        } else {
            lockBucketButton.setText("Lock");
            lockBucketButton.setPrefixComponent(new Icon(VaadinIcon.LOCK));
        }
    }

    private void changeBucketLockedState() {
        var newValue = !bucket.isLocked();
        try {
            bucketRepository.lock(bucket, newValue);
        } catch (Error error) {
            Notification.show(error.getMessage(), 3000, Notification.Position.TOP_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        bucket.setLocked(newValue);
        setLockBucketButtonState();
        updateBucketInfo();

        Notification.show("Success", 3000, Notification.Position.TOP_END)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}
