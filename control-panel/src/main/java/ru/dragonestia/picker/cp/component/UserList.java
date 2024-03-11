package ru.dragonestia.picker.cp.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import ru.dragonestia.picker.api.model.user.UserDetails;
import ru.dragonestia.picker.api.repository.UserRepository;
import ru.dragonestia.picker.api.repository.response.type.RRoom;
import ru.dragonestia.picker.api.repository.response.type.RUser;

import java.util.ArrayList;
import java.util.List;

public class UserList extends VerticalLayout implements RefreshableTable {

    private final RRoom room;
    private final UserRepository userRepository;
    private final Button buttonRemove;
    private final Grid<RUser> usersGrid;
    private final Span totalUsers = new Span();
    private final Span occupancy = new Span();
    private List<RUser> cachedUsers = new ArrayList<>();

    public UserList(RRoom room, UserRepository userRepository) {
        this.room = room;
        this.userRepository = userRepository;

        buttonRemove = createButtonRemove();
        add(usersGrid = createUsersGrid());

        refresh();
        updateButtonRemove();
    }

    private Button createButtonRemove() {
        var button = new Button("Unlink");
        button.setPrefixComponent(new Icon(VaadinIcon.UNLINK));
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        button.addClickListener(event -> {
            var users = usersGrid.getSelectedItems();
            if (users.isEmpty()) return;
            userRepository.unlinkFromRoom(room, users);
            refresh();
        });
        return button;
    }

    private Grid<RUser> createUsersGrid() {
        var grid = new Grid<RUser>();

        grid.addColumn(RUser::getId).setHeader("User Identifier").setSortable(true).setFooter(totalUsers);

        grid.addColumn(user -> user.getDetail(UserDetails.COUNT_ROOMS)).setTextAlign(ColumnTextAlign.CENTER)
                .setHeader("Linked with rooms").setComparator((user1, user2) -> {
                    var r1 = Integer.parseInt(user1.getDetail(UserDetails.COUNT_ROOMS));
                    var r2 = Integer.parseInt(user2.getDetail(UserDetails.COUNT_ROOMS));

                    return Integer.compare(r1, r2);
                }).setSortable(true).setFooter(occupancy);

        grid.addComponentColumn(this::createManageButton).setTextAlign(ColumnTextAlign.END).setFrozenToEnd(true)
                .setTextAlign(ColumnTextAlign.END).setHeader(createManageTableButtons());

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addSelectionListener(event -> updateButtonRemove());
        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
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

    private HorizontalLayout createManageTableButtons() {
        var layout = new HorizontalLayout();
        layout.setJustifyContentMode(JustifyContentMode.END);

        layout.add(buttonRemove);
        layout.add(createRefreshButton());

        return layout;
    }

    private void updateButtonRemove() {
        var users = usersGrid.getSelectedItems();

        if (users.isEmpty()) {
            buttonRemove.setEnabled(false);
            buttonRemove.setText("Unlink");
            return;
        }

        buttonRemove.setEnabled(true);
        buttonRemove.setText("Unlink(" + users.size() + ")");
    }

    public static int getUsingPercentage(int slots, int usedSlots) {
        if (slots == RRoom.INFINITE_SLOTS) return -1;
        double percent = usedSlots / (double) slots * 100;
        return (int) percent;
    }

    @Override
    public void refresh() {
        cachedUsers = userRepository.all(room, UserRepository.ALL_DETAILS);
        usersGrid.setItems(cachedUsers);
        totalUsers.setText("Total users: " + cachedUsers.size());
        occupancy.setText("Occupancy: %s".formatted(getUsingPercentage(room.getSlots(), cachedUsers.size()) + "%"));
    }
}
