package ru.dragonestia.picker.cp.component;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import ru.dragonestia.picker.cp.model.Bucket;
import ru.dragonestia.picker.cp.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserList extends VerticalLayout {

    private final Bucket bucket;
    private final Grid<User> usersGrid;
    private final Span totalUsers = new Span();
    private final Span occupancy = new Span();
    private List<User> cachedUsers = new ArrayList<>();

    public UserList(Bucket bucket, List<User> users) {
        this.bucket = bucket;

        add(usersGrid = createUsersGrid());

        update(users);
    }

    private Grid<User> createUsersGrid() {
        var grid = new Grid<User>();
        grid.addColumn(User::identifier).setHeader("User Identifier").setFooter(totalUsers);
        grid.addColumn(user -> 0).setTextAlign(ColumnTextAlign.CENTER).setHeader("Linked with buckets") // TODO
                .setFooter(occupancy);
        grid.addComponentColumn(user -> new Span("buttons")).setHeader("Manage"); // TODO
        return grid;
    }

    public void update(List<User> users) {
        cachedUsers = users;
        usersGrid.setItems(users);
        totalUsers.setText("Total users: " + users.size());
        occupancy.setText("Occupancy: %s".formatted(bucket.getUsingPercentage(users.size())));
    }
}
