package ru.dragonestia.picker.cp.page;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dragonestia.picker.api.repository.UserRepository;
import ru.dragonestia.picker.api.repository.details.UserDetails;
import ru.dragonestia.picker.api.repository.response.type.RUser;
import ru.dragonestia.picker.cp.component.Notifications;

import java.util.LinkedList;
import java.util.List;

@PageTitle("Search users")
@Route(value = "/users", layout = MainLayout.class)
public class UserSearchPage extends VerticalLayout {

    private final UserRepository userRepository;
    private final TextField fieldUsername;
    private final Grid<RUser> userGrid;
    private List<RUser> cachedUsers = new LinkedList<>();

    @Autowired
    public UserSearchPage(UserRepository userRepository) {
        this.userRepository = userRepository;

        add(fieldUsername = createUsernameInputField());
        add(userGrid = createUserGrid());
    }

    private TextField createUsernameInputField() {
        var field = new TextField();
        field.setLabel("Username");
        field.setPlaceholder("some-user-identifier");
        field.setRequired(true);
        field.setMinWidth(30, Unit.PERCENTAGE);

        var button = new Button(new Icon(VaadinIcon.SEARCH));
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.getStyle().set("color", "#FFFFFF");
        button.addClickListener(event -> search(fieldUsername.getValue().trim()));

        field.setSuffixComponent(button);
        return field;
    }

    private Grid<RUser> createUserGrid() {
        var grid = new Grid<RUser>();

        grid.addColumn(RUser::getId).setHeader("Identifier")
                .setFooter("Found %s users".formatted(cachedUsers.size()));

        grid.addColumn(user -> user.getDetail(UserDetails.COUNT_ROOMS)).setTextAlign(ColumnTextAlign.CENTER).setHeader("Linked with rooms");

        grid.addComponentColumn(user -> new Span("buttons")).setHeader("Manage"); // TODO

        return grid;
    }

    private void search(String input) {
        userGrid.setItems(cachedUsers = userRepository.search(input, UserRepository.ALL_DETAILS));
    }
}
