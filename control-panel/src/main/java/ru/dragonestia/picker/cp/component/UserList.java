package ru.dragonestia.picker.cp.component;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import ru.dragonestia.picker.api.model.Room;
import ru.dragonestia.picker.api.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserList extends VerticalLayout {

    private final Room room;
    private final Grid<User> usersGrid;
    private final Span totalUsers = new Span();
    private final Span occupancy = new Span();
    private List<User> cachedUsers = new ArrayList<>();

    public UserList(Room room, List<User> users) {
        this.room = room;

        add(usersGrid = createUsersGrid());

        update(users);
    }

    private Grid<User> createUsersGrid() {
        var grid = new Grid<User>();
        grid.addColumn(User::getId).setHeader("User Identifier").setFooter(totalUsers);
        grid.addColumn(user -> 0).setTextAlign(ColumnTextAlign.CENTER).setHeader("Linked with rooms") // TODO
                .setFooter(occupancy);
        grid.addComponentColumn(user -> new Span("buttons")).setHeader("Manage"); // TODO
        return grid;
    }

    public void update(List<User> users) {
        cachedUsers = users;
        usersGrid.setItems(users);
        totalUsers.setText("Total users: " + users.size());
        occupancy.setText("Occupancy: %s".formatted(getUsingPercentage(room, users.size())));
    }

    private String getUsingPercentage(Room room, int usedSlots) {
        if (room.isUnlimited()) return "0%";
        double percent = usedSlots / (double) room.getSlots() * 100;
        return ((int) percent) + "%";
    }
}
