package ru.dragonestia.loadbalancer.cp.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.Autocomplete;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.springframework.lang.Nullable;
import ru.dragonestia.loadbalancer.cp.model.Node;
import ru.dragonestia.loadbalancer.cp.model.type.LoadBalancingMethod;

import java.util.function.Function;

public class RegisterNode extends Details {

    private final Function<Node, Response> onSubmit;
    private final TextField identifierField;
    private final RadioButtonGroup<LoadBalancingMethod> modeRadio;

    public RegisterNode(Function<Node, Response> onSubmit) {
        super(new H2("Register node"));
        this.onSubmit = onSubmit;

        var layout = new VerticalLayout();
        layout.add(identifierField = createNodeIdentifierField());
        layout.add(modeRadio = createModeRadio());
        layout.add(createSubmitButton());

        add(layout);
    }

    private TextField createNodeIdentifierField() {
        var field = new TextField("Identifier");
        field.setMinWidth(20, Unit.REM);
        field.setPlaceholder("example-node-id");
        field.setHelperText("The field can contain only lowercase letters, numbers and a dash character");
        field.setPattern("^[a-z\\d-]+$");
        field.setRequired(true);
        field.setAutocomplete(Autocomplete.OFF);
        field.addValueChangeListener(event -> field.setValue(event.getValue().trim()));
        return field;
    }

    private Button createSubmitButton() {
        var button = new Button("Register");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickListener(event -> onClick());
        return button;
    }

    private RadioButtonGroup<LoadBalancingMethod> createModeRadio() {
        var radio = new RadioButtonGroup<LoadBalancingMethod>("Mode");
        radio.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        radio.setRenderer(new ComponentRenderer<Component, LoadBalancingMethod>(mode -> new Span(mode.getName())));
        radio.setItems(LoadBalancingMethod.SEQUENTIAL_FILLING,
                LoadBalancingMethod.ROUND_ROBIN,
                LoadBalancingMethod.LEAST_PICKED);

        radio.setValue(LoadBalancingMethod.SEQUENTIAL_FILLING);
        return radio;
    }

    public void clear() {
        identifierField.clear();
    }

    private @Nullable String validateForm(String identifier) {
        if (identifier.isEmpty()) {
            return "Node identifier cannot be empty";
        }

        return null;
    }

    private void onClick() {
        String nodeIdentifier = identifierField.getValue();

        String error = null;
        if (identifierField.isInvalid() || (error = validateForm(nodeIdentifier)) != null) {
            if (identifierField.isInvalid()) {
                error = "Invalid node identifier format";
            }

            Notification.show(error, 3000, Notification.Position.TOP_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        var node = new Node(nodeIdentifier, modeRadio.getValue());
        var response = onSubmit.apply(node);
        clear();
        if (response.error()) {
            Notification.show(response.reason(), 3000, Notification.Position.TOP_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        Notification.show("Node was successfully registered", 3000, Notification.Position.TOP_END)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    public record Response(boolean error, @Nullable String reason) {}
}