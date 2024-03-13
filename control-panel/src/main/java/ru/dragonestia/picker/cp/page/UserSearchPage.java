package ru.dragonestia.picker.cp.page;

import com.vaadin.flow.component.Key;
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
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.model.user.IUser;
import ru.dragonestia.picker.api.model.user.UserDetails;
import ru.dragonestia.picker.api.repository.UserRepository;
import ru.dragonestia.picker.api.repository.query.user.SearchUsers;
import ru.dragonestia.picker.api.repository.type.UserIdentifier;
import ru.dragonestia.picker.cp.component.RefreshableTable;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@PageTitle("Search users")
@Route(value = "/users", layout = MainLayout.class)
public class UserSearchPage extends VerticalLayout implements RefreshableTable {

    private final UserRepository userRepository;
    private final TextField fieldUsername;
    private final Grid<IUser> userGrid;
    private final Span foundUsers;
    private List<IUser> cachedUsers = new LinkedList<>();

    @Autowired
    public UserSearchPage(RoomPickerClient client) {
        this.userRepository = client.getUserRepository();

        foundUsers = new Span();
        add(fieldUsername = createUsernameInputField());
        add(userGrid = createUserGrid());
        justRefresh();
    }

    private TextField createUsernameInputField() {
        var field = new TextField();
        field.setLabel("Username");
        field.setPlaceholder("some-user-identifier");
        field.setRequired(true);
        field.setMinWidth(30, Unit.PERCENTAGE);
        field.setAutofocus(true);

        var button = new Button(new Icon(VaadinIcon.SEARCH));
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.getStyle().set("color", "#FFFFFF");
        button.addClickListener(event -> refresh());
        button.addClickShortcut(Key.ENTER);

        field.setSuffixComponent(button);
        return field;
    }

    private Grid<IUser> createUserGrid() {
        var grid = new Grid<IUser>();

        grid.addColumn(IUser::getIdentifier).setHeader("Identifier").setSortable(true)
                .setFooter(foundUsers);

        grid.addColumn(user -> user.getDetail(UserDetails.COUNT_ROOMS)).setComparator((user1, user2) -> {
            var r1 = Integer.parseInt(Objects.requireNonNull(user1.getDetail(UserDetails.COUNT_ROOMS)));
            var r2 = Integer.parseInt(Objects.requireNonNull(user2.getDetail(UserDetails.COUNT_ROOMS)));

            return Integer.compare(r1, r2);
        }).setTextAlign(ColumnTextAlign.CENTER).setHeader("Linked with rooms");

        grid.addComponentColumn(user -> {
            var button = new Button("Details");
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            button.addClickListener(event -> {
                getUI().ifPresent(ui -> ui.navigate("/users/" + user.getIdentifier()));
            });
            return button;
        }).setTextAlign(ColumnTextAlign.END).setFrozenToEnd(true).setHeader(createRefreshButton());

        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        return grid;
    }

    private void search(String input) {
        System.out.println("Input: " + input);
        if (input.isEmpty()) {
            userGrid.setItems();
        }

        userGrid.setItems(cachedUsers = userRepository.searchUsers(SearchUsers.withAllDetails(UserIdentifier.of(input)))
                .stream().map(user -> (IUser) user).toList());
    }

    @Override
    public void refresh() {
        search(fieldUsername.getValue().trim());
        foundUsers.setText("Found %s users".formatted(cachedUsers.size()));
    }

    public void justRefresh() {
        userGrid.setItems();
        foundUsers.setText("Found %s users".formatted(cachedUsers.size()));
    }
}
