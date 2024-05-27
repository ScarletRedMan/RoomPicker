package ru.dragonestia.picker.cp.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.Autocomplete;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.springframework.lang.Nullable;
import ru.dragonestia.picker.api.exception.InvalidIdentifierException;
import ru.dragonestia.picker.api.model.instance.InstanceId;
import ru.dragonestia.picker.api.model.instance.type.PickingMethod;
import ru.dragonestia.picker.cp.repository.dto.InstanceDTO;
import ru.dragonestia.picker.cp.util.Notifications;

import java.util.function.Function;

public class RegisterInstance extends Details {

    private final Function<InstanceDTO, Response> onSubmit;
    private final TextField identifierField;
    private final RadioButtonGroup<PickingMethod> modeRadio;
    private final Checkbox persistField;

    public RegisterInstance(Function<InstanceDTO, Response> onSubmit) {
        super(new H2("Register instance"));
        this.onSubmit = onSubmit;

        var layout = new VerticalLayout();
        layout.add(identifierField = createInstanceIdentifierField());
        layout.add(modeRadio = createModeRadio());
        layout.add(persistField = createPersistField());
        layout.add(createSubmitButton());

        add(layout);
    }

    private TextField createInstanceIdentifierField() {
        var field = new TextField("Identifier");
        field.setMinWidth(20, Unit.REM);
        field.setPlaceholder("example-instance-id");
        field.setHelperText("The field can contain only lowercase letters, numbers and a dash character");
        field.setPattern("^[a-z\\d-]+$");
        field.setRequired(true);
        field.setAutocomplete(Autocomplete.OFF);
        field.addValueChangeListener(event -> field.setValue(event.getValue().trim()));
        return field;
    }

    private Checkbox createPersistField() {
        return new Checkbox("Persist", false);
    }

    private Button createSubmitButton() {
        var button = new Button("Register");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickListener(event -> onClick());
        return button;
    }

    private RadioButtonGroup<PickingMethod> createModeRadio() {
        var radio = new RadioButtonGroup<PickingMethod>("Mode");
        radio.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        radio.setRenderer(new ComponentRenderer<Component, PickingMethod>(mode -> new Span(mode.name())));
        radio.setItems(PickingMethod.SEQUENTIAL_FILLING,
                PickingMethod.ROUND_ROBIN,
                PickingMethod.LEAST_PICKED);

        radio.setValue(PickingMethod.SEQUENTIAL_FILLING);
        return radio;
    }

    public void clear() {
        identifierField.clear();
        persistField.setValue(false);
    }

    private @Nullable String validateForm(String identifier) {
        if (identifier.isEmpty()) {
            return "Instance id cannot be empty";
        }

        try {
            InstanceId.of(identifier);
        } catch (InvalidIdentifierException ex) {
            return "Invalid identifier";
        }

        return null;
    }

    private void onClick() {
        String instanceIdentifier = identifierField.getValue();

        String error = null;
        if (identifierField.isInvalid() || (error = validateForm(instanceIdentifier)) != null) {
            if (identifierField.isInvalid()) {
                error = "Invalid instance id format";
            }

            Notifications.error(error);
            return;
        }

        var response = onSubmit.apply(new InstanceDTO() {

            @Override
            public String getId() {
                return instanceIdentifier;
            }

            @Override
            public PickingMethod getMethod() {
                return modeRadio.getValue();
            }

            @Override
            public boolean isPersist() {
                return persistField.getValue();
            }

            @Override
            public int getCountRooms() {
                throw new UnsupportedOperationException();
            }
        });

        clear();
        if (response.error()) {
            Notifications.error(response.reason());
            return;
        }

        Notifications.success("Instance was successfully registered");
    }

    public record Response(boolean error, @Nullable String reason) {}
}