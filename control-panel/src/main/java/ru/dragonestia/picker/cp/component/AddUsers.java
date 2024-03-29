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
import ru.dragonestia.picker.api.model.room.IRoom;
import ru.dragonestia.picker.api.model.user.IUser;
import ru.dragonestia.picker.api.model.user.UserDefinition;
import ru.dragonestia.picker.api.repository.type.UserIdentifier;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class AddUsers extends Details {

    private final BiConsumer<Collection<IUser>, Boolean> onCommit;
    private final Checkbox ignoreSlots;
    private final VerticalLayout usersLayout;
    private final AtomicInteger freeUserIdNumber = new AtomicInteger(1);

    public AddUsers(IRoom room, BiConsumer<Collection<IUser>, Boolean> onCommit) {
        super(new H2("Add users"));

        this.onCommit = onCommit;
        usersLayout = new VerticalLayout();

        add(addUserToTransacionButton());
        add(usersLayout);
        usersLayout.add(new UserEntry(false, freeUserIdNumber.getAndIncrement()));
        add(ignoreSlots = new Checkbox("Ignore slot limitation", false));
        add(createAddUsersButton());
    }

    public void clear() {
        freeUserIdNumber.set(1);
        ignoreSlots.setValue(false);
        usersLayout.removeAll();
        usersLayout.add(new UserEntry(false, freeUserIdNumber.getAndIncrement()));
    }

    public List<IUser> readAllUsers() {
        return usersLayout.getChildren()
                .filter(component -> component instanceof UserEntry)
                .map(component -> (UserEntry) component)
                .map(user -> user.getUserIdentifierField().getValue())
                .map(String::trim)
                .filter(user -> !user.isEmpty())
                .map(id -> (IUser) new UserDefinition(UserIdentifier.of(id)))
                .toList();
    }

    private Button addUserToTransacionButton() {
        var button = new Button("Add user to transaction");
        button.addClickListener(event -> {
            usersLayout.add(new UserEntry(true, freeUserIdNumber.getAndIncrement()));
        });
        button.setPrefixComponent(new Icon(VaadinIcon.PLUS));
        return button;
    }

    private Button createAddUsersButton() {
        var button = new Button("Commit", event -> onClick());
        button.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        return button;
    }

    private void onClick() {
        try {
            onCommit.accept(readAllUsers(), ignoreSlots.getValue());
        } catch (Error error) {
            Notifications.error(error.getMessage());
        }

        clear();
    }

    @Getter
    public static class UserEntry extends Div {

        private final TextField userIdentifierField;

        public UserEntry(boolean canBeDeleted, int number) {
            add(userIdentifierField = createUserIdentifierField(canBeDeleted, number));
        }

        private TextField createUserIdentifierField(boolean canBeDeleted, int number) {
            var field = new TextField("User id");
            field.setPlaceholder("example-user-id-" + number);
            if (!canBeDeleted) {
                field.setHelperText("It can be UUID, username, numeric ids, etc");
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
