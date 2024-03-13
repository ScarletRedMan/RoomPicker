package ru.dragonestia.picker.cp.component;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.Autocomplete;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.lang.Nullable;
import ru.dragonestia.picker.api.model.node.INode;
import ru.dragonestia.picker.api.model.room.IRoom;
import ru.dragonestia.picker.api.model.room.RoomDefinition;
import ru.dragonestia.picker.api.repository.type.RoomIdentifier;

import java.util.function.Function;

public class RegisterRoom extends Details {

    private final INode node;
    private final Function<RoomDefinition, Response> onSubmit;
    private final TextField identifierField;
    private final TextArea payloadField;
    private final Checkbox lockedField;
    private final Checkbox persistField;

    public RegisterRoom(INode node, Function<RoomDefinition, Response> onSubmit) {
        super(new H2("Register room"));
        this.node = node;
        this.onSubmit = onSubmit;

        var layout = new VerticalLayout();
        layout.add(createNodeIdentifierField());
        layout.add(identifierField = createRoomIdentifierField());
        layout.add(payloadField = createPayloadField());
        layout.add(lockedField = createLockedField());
        layout.add(persistField = createPersistField());
        layout.add(createSubmitButton());

        add(layout);
    }

    private TextField createNodeIdentifierField() {
        var field = new TextField("Node identifier");
        field.setMinWidth(20, Unit.REM);
        field.setValue(node.getIdentifier());
        field.setReadOnly(true);
        return field;
    }

    private TextField createRoomIdentifierField() {
        var field = new TextField("Identifier");
        field.setMinWidth(20, Unit.REM);
        field.setPlaceholder("example-room-id");
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

    private Checkbox createLockedField() {
        var field = new Checkbox("Locked");
        field.setValue(false);
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

    public void clear() {
        identifierField.clear();
        payloadField.clear();
        lockedField.setValue(false);
        persistField.setValue(false);
    }

    private @Nullable String validateForm(String identifier) {
        if (identifier.isEmpty()) {
            return "Node identifier cannot be empty";
        }

        return null;
    }

    private void onClick() {
        var roomId = identifierField.getValue();

        String error = null;
        if (identifierField.isInvalid() || (error = validateForm(roomId)) != null) {
            if (identifierField.isInvalid()) {
                error = "Invalid room id format";
            }

            Notifications.error(error);
            return;
        }

        var room = new RoomDefinition(node.getIdentifierObject(), RoomIdentifier.of(roomId))
                .setMaxSlots(IRoom.UNLIMITED_SLOTS)
                .setPayload(payloadField.getValue())
                .setPersist(persistField.getValue());

        room.setLocked(lockedField.getValue());
        var response = onSubmit.apply(room);
        clear();
        if (response.error()) {
            Notifications.error(response.reason());
            return;
        }

        Notifications.success("Room was successfully registered");
    }

    public record Response(boolean error, @Nullable String reason) {}
}
