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
import ru.dragonestia.picker.api.model.room.IRoom;
import ru.dragonestia.picker.api.model.user.IUser;
import ru.dragonestia.picker.api.model.user.UserDetails;
import ru.dragonestia.picker.api.repository.UserRepository;
import ru.dragonestia.picker.api.repository.query.user.GetAllUsersFromRoom;
import ru.dragonestia.picker.api.repository.query.user.UnlinkUsersFromRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserList extends VerticalLayout implements RefreshableTable {

    private final IRoom room;
    private final UserRepository userRepository;
    private final Button buttonRemove;
    private final Grid<IUser> usersGrid;
    private final Span totalUsers = new Span();
    private final Span occupancy = new Span();
    private List<IUser> cachedUsers = new ArrayList<>();

    public UserList(IRoom room, UserRepository userRepository) {
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
            userRepository.unlinkUsersFromRoom(UnlinkUsersFromRoom.builder()
                    .setNodeId(room.getNodeIdentifierObject())
                    .setRoomId(room.getIdentifierObject())
                    .setUsers(users.stream().map(IUser::getIdentifierObject).collect(Collectors.toSet()))
                    .build());
            refresh();
        });
        return button;
    }

    private Grid<IUser> createUsersGrid() {
        var grid = new Grid<IUser>();

        grid.addColumn(IUser::getIdentifier).setHeader("User Identifier").setSortable(true).setFooter(totalUsers);

        grid.addColumn(user -> user.getDetail(UserDetails.COUNT_ROOMS)).setTextAlign(ColumnTextAlign.CENTER)
                .setHeader("Linked with rooms").setComparator((user1, user2) -> {
                    var r1 = Integer.parseInt(Objects.requireNonNull(user1.getDetail(UserDetails.COUNT_ROOMS)));
                    var r2 = Integer.parseInt(Objects.requireNonNull(user2.getDetail(UserDetails.COUNT_ROOMS)));

                    return Integer.compare(r1, r2);
                }).setSortable(true).setFooter(occupancy);

        grid.addComponentColumn(this::createManageButton).setTextAlign(ColumnTextAlign.END).setFrozenToEnd(true)
                .setTextAlign(ColumnTextAlign.END).setHeader(createManageTableButtons());

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addSelectionListener(event -> updateButtonRemove());
        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        return grid;
    }

    private Button createManageButton(IUser user) {
        var button = new Button("Details");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate("/users/" + user.getIdentifier()));
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
        if (slots == IRoom.UNLIMITED_SLOTS) return -1;
        double percent = usedSlots / (double) slots * 100;
        return (int) percent;
    }

    @Override
    public void refresh() {
        cachedUsers = userRepository.getAllUsersFormRoom(GetAllUsersFromRoom.withAllDetails(room.getNodeIdentifierObject(), room.getIdentifierObject()))
                .stream().map(user -> (IUser) user).toList();
        usersGrid.setItems(cachedUsers);
        totalUsers.setText("Total users: " + cachedUsers.size());
        occupancy.setText("Occupancy: %s".formatted(getUsingPercentage(room.getMaxSlots(), cachedUsers.size()) + "%"));
    }
}
