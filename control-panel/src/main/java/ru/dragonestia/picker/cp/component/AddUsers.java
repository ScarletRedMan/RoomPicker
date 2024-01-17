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
import ru.dragonestia.picker.api.repository.response.type.RRoom;
import ru.dragonestia.picker.api.repository.response.type.RUser;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

public class AddUsers extends Details {

    private final RRoom room;
    private final BiConsumer<Collection<RUser>, Boolean> onCommit;
    private final Checkbox ignoreSlots;
    private final VerticalLayout usersLayout;

    public AddUsers(RRoom room, BiConsumer<Collection<RUser>, Boolean> onCommit) {
        super(new H2("Add users"));

        this.room = room;
        this.onCommit = onCommit;
        usersLayout = new VerticalLayout();

        add(addUserToTransacionButton());
        add(usersLayout);
        usersLayout.add(new UserEntry(false));
        add(ignoreSlots = new Checkbox("Ignore slot limitation", false));
        add(createAddUsersButton());
    }

    public void clear() {
        ignoreSlots.setValue(false);
        usersLayout.removeAll();
        usersLayout.add(new UserEntry(false));
    }

    public List<RUser> readAllUsers() {
        return usersLayout.getChildren()
                .filter(component -> component instanceof UserEntry)
                .map(component -> (UserEntry) component)
                .map(user -> user.getUserIdentifierField().getValue())
                .map(String::trim)
                .filter(user -> !user.isEmpty())
                .map(RUser::new)
                .toList();
    }

    private Button addUserToTransacionButton() {
        var button = new Button("Add user to transaction");
        button.addClickListener(event -> {
            usersLayout.add(new UserEntry(true));
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

        public UserEntry(boolean canBeDeleted) {
            add(userIdentifierField = createUserIdentifierField(canBeDeleted));
        }

        private TextField createUserIdentifierField(boolean canBeDeleted) {
            var field = new TextField("User id");
            field.setPlaceholder("example-user-id");
            field.setHelperText("It can be UUID, username, numeric ids, etc");
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
