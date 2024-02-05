package ru.dragonestia.picker.cp.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import ru.dragonestia.picker.api.repository.response.type.RRoom;
import ru.dragonestia.picker.api.repository.response.type.RUser;
import ru.dragonestia.picker.api.repository.details.UserDetails;

import java.util.ArrayList;
import java.util.List;

public class UserList extends VerticalLayout {

    private final RRoom room;
    private final Grid<RUser> usersGrid;
    private final Span totalUsers = new Span();
    private final Span occupancy = new Span();
    private List<RUser> cachedUsers = new ArrayList<>();

    public UserList(RRoom room, List<RUser> users) {
        this.room = room;

        add(usersGrid = createUsersGrid());

        update(users);
    }

    private Grid<RUser> createUsersGrid() {
        var grid = new Grid<RUser>();
        grid.addColumn(RUser::getId).setHeader("User Identifier").setFooter(totalUsers);
        grid.addColumn(user -> user.getDetail(UserDetails.COUNT_ROOMS)).setTextAlign(ColumnTextAlign.CENTER).setHeader("Linked with rooms")
                .setFooter(occupancy);
        grid.addComponentColumn(this::createManageButton).setHeader("Manage");
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

    public static String getUsingPercentage(int slots, int usedSlots) {
        if (slots == RRoom.INFINITE_SLOTS) return "N/A";
        double percent = usedSlots / (double) slots * 100;
        return ((int) percent) + "%";
    }
}
