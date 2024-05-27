package ru.dragonestia.picker.cp.component;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import ru.dragonestia.picker.api.model.entity.EntityId;
import ru.dragonestia.picker.api.model.room.Room;
import ru.dragonestia.picker.cp.util.Notifications;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class AddEntities extends Details {

    private final BiConsumer<Collection<EntityId>, Boolean> onCommit;
    private final Checkbox ignoreSlots;
    private final VerticalLayout entitiesLayout;
    private final AtomicInteger freeEntityIdNumber = new AtomicInteger(1);

    public AddEntities(Room room, BiConsumer<Collection<EntityId>, Boolean> onCommit) {
        super(new H2("Add entities"));

        this.onCommit = onCommit;
        entitiesLayout = new VerticalLayout();

        add(addEntityToTransacionButton());
        add(entitiesLayout);
        entitiesLayout.add(new EntityEntry(false, freeEntityIdNumber.getAndIncrement()));
        add(ignoreSlots = new Checkbox("Ignore slot limitation", false));
        add(createAddEntitiesButton());
    }

    public void clear() {
        freeEntityIdNumber.set(1);
        ignoreSlots.setValue(false);
        entitiesLayout.removeAll();
        entitiesLayout.add(new EntityEntry(false, freeEntityIdNumber.getAndIncrement()));
    }

    public List<EntityId> readAllEntities() {
        return entitiesLayout.getChildren()
                .filter(component -> component instanceof EntityEntry)
                .map(component -> (EntityEntry) component)
                .map(entity -> entity.getEntityIdentifierField().getValue())
                .map(String::trim)
                .filter(entity -> !entity.isEmpty())
                .map(EntityId::of)
                .toList();
    }

    private Button addEntityToTransacionButton() {
        var button = new Button("Add entity to transaction");
        button.addClickListener(event -> {
            entitiesLayout.add(new EntityEntry(true, freeEntityIdNumber.getAndIncrement()));
        });
        button.setPrefixComponent(new Icon(VaadinIcon.PLUS));
        return button;
    }

    private Button createAddEntitiesButton() {
        var button = new Button("Commit", event -> onClick());
        button.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        return button;
    }

    private void onClick() {
        try {
            onCommit.accept(readAllEntities(), ignoreSlots.getValue());
        } catch (Error error) {
            Notifications.error(error.getMessage());
        }

        clear();
    }

    @Getter
    public static class EntityEntry extends Div {

        private final TextField entityIdentifierField;

        public EntityEntry(boolean canBeDeleted, int number) {
            add(entityIdentifierField = createEntityIdentifierField(canBeDeleted, number));
        }

        private TextField createEntityIdentifierField(boolean canBeDeleted, int number) {
            var field = new TextField("Entity id");
            field.setPlaceholder("example-entity-id-" + number);
            if (!canBeDeleted) {
                field.setHelperText("It can be UUID, entityname, numeric ids, etc");
            }
            field.setMinWidth(20, Unit.REM);

            if (canBeDeleted) {
                var removeButton = new Button(new Icon(VaadinIcon.TRASH), event -> {
                    removeFromParent();
                });
                removeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

                field.setSuffixComponent(removeButton);
            }

            return field;
        }
    }
}
