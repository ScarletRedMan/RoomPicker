package ru.dragonestia.picker.cp.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import ru.dragonestia.picker.api.repository.UserRepository;
import ru.dragonestia.picker.api.repository.response.type.RRoom;
import ru.dragonestia.picker.api.repository.response.type.RUser;
import ru.dragonestia.picker.api.repository.details.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class UserList extends VerticalLayout {

    private final RRoom room;
    private final UserRepository userRepository;
    private final Button buttonRemove;
    private final Grid<RUser> usersGrid;
    private final Span totalUsers = new Span();
    private final Span occupancy = new Span();
    private List<RUser> cachedUsers = new ArrayList<>();

    public UserList(RRoom room, List<RUser> users, UserRepository userRepository) {
        this.room = room;
        this.userRepository = userRepository;

        add(buttonRemove = createButtonRemove());
        add(usersGrid = createUsersGrid());

        update(users);
        updateButtonRemove();
    }

    private Button createButtonRemove() {
        var button = new Button("Unlink users");
        button.setPrefixComponent(new Icon(VaadinIcon.UNLINK));
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        button.addClickListener(event -> {
            var users = usersGrid.getSelectedItems();
            if (users.isEmpty()) return;
            userRepository.unlinkFromRoom(room, users);
            update(userRepository.all(room, UserRepository.ALL_DETAILS));
        });
        return button;
    }

    private Grid<RUser> createUsersGrid() {
        var grid = new Grid<RUser>();
        grid.addColumn(RUser::getId).setHeader("User Identifier").setFooter(totalUsers);
        grid.addColumn(user -> user.getDetail(UserDetails.COUNT_ROOMS)).setTextAlign(ColumnTextAlign.CENTER).setHeader("Linked with rooms")
                .setFooter(occupancy);
        grid.addComponentColumn(this::createManageButton).setHeader("Manage");
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addSelectionListener(event -> updateButtonRemove());
        return grid;
    }

    private Button createManageButton(RUser user) {
        var button = new Button("Details");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate("/users/" + user.getId()));
        });
        return button;
    }

    public void update(List<RUser> users) {
        cachedUsers = users;
        usersGrid.setItems(users);
        totalUsers.setText("Total users: " + users.size());
        occupancy.setText("Occupancy: %s".formatted(getUsingPercentage(room.getSlots(), users.size())));
    }

    private void updateButtonRemove() {
        var users = usersGrid.getSelectedItems();

        if (users.isEmpty()) {
            buttonRemove.setEnabled(false);
            buttonRemove.setText("Unlink users");
            return;
        }

        buttonRemove.setEnabled(true);
        buttonRemove.setText("Unlink users(" + users.size() + ")");
    }

    public static String getUsingPercentage(int slots, int usedSlots) {
        if (slots == RRoom.INFINITE_SLOTS) return "N/A";
        double percent = usedSlots / (double) slots * 100;
        return ((int) percent) + "%";
    }
}
