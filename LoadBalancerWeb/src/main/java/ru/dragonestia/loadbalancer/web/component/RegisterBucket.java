package ru.dragonestia.loadbalancer.web.component;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.Autocomplete;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.lang.Nullable;
import ru.dragonestia.loadbalancer.web.model.Bucket;
import ru.dragonestia.loadbalancer.web.model.Node;
import ru.dragonestia.loadbalancer.web.model.type.SlotLimit;

import java.util.function.Function;

public class RegisterBucket extends Details {

    private final Node node;
    private final Function<Bucket, Response> onSubmit;
    private final TextField identifierField;
    private final TextArea payloadField;

    public RegisterBucket(Node node, Function<Bucket, Response> onSubmit) {
        super(new H2("Register bucket"));
        this.node = node;
        this.onSubmit = onSubmit;

        var layout = new VerticalLayout();
        layout.add(createNodeIdentifierField());
        layout.add(identifierField = createBucketIdentifierField());
        layout.add(payloadField = createPayloadField());
        layout.add(createSubmitButton());

        add(layout);
    }

    private TextField createNodeIdentifierField() {
        var field = new TextField("Node identifier");
        field.setMinWidth(20, Unit.REM);
        field.setValue(node.identifier());
        field.setReadOnly(true);
        return field;
    }

    private TextField createBucketIdentifierField() {
        var field = new TextField("Identifier");
        field.setMinWidth(20, Unit.REM);
        field.setPlaceholder("example-bucket-id");
        field.setHelperText("The field can contain only lowercase letters, numbers and a dash character");
        field.setRequired(true);
        field.setPattern("^[a-z\\d-]+$");
        field.setAutocomplete(Autocomplete.OFF);
        field.addValueChangeListener(event -> field.setValue(event.getValue().trim()));
        return field;
    }

    private TextArea createPayloadField() {
        var field = new TextArea("Payload");
        field.setMinWidth(20, Unit.REM);
        field.setPlaceholder("{\"value\": \"Hello world!\"}");
        field.setHelperText("Any useful data here");
        return field;
    }

    private Button createSubmitButton() {
        var button = new Button("Register");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickListener(event -> onClick());
        return button;
    }

    public void clear() {
        identifierField.clear();
        payloadField.clear();
    }

    private @Nullable String validateForm(String identifier) {
        if (identifier.isEmpty()) {
            return "Node identifier cannot be empty";
        }

        return null;
    }

    private void onClick() {
        var nodeIdentifier = identifierField.getValue();

        String error = null;
        if (identifierField.isInvalid() || (error = validateForm(nodeIdentifier)) != null) {
            if (identifierField.isInvalid()) {
                error = "Invalid bucket identifier format";
            }

            Notification.show(error, 3000, Notification.Position.TOP_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        var bucket = Bucket.create(nodeIdentifier, node, SlotLimit.unlimited(), payloadField.getValue());
        var response = onSubmit.apply(bucket);
        clear();
        if (response.error()) {
            Notification.show(response.reason(), 3000, Notification.Position.TOP_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        Notification.show("Bucket was successfully registered", 3000, Notification.Position.TOP_END)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    public record Response(boolean error, @Nullable String reason) {}
}
