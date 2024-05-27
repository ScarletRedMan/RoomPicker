package ru.dragonestia.picker.cp.component;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.theme.lumo.LumoIcon;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.model.account.Account;
import ru.dragonestia.picker.api.model.account.AccountId;
import ru.dragonestia.picker.api.model.account.Permission;
import ru.dragonestia.picker.cp.util.PermissionDescription;
import ru.dragonestia.picker.cp.util.Notifications;

import java.util.*;

public class AccountList extends VerticalLayout implements RefreshableTable {

    private final RoomPickerClient client;
    private final TextField searchField;
    private final Grid<Account> grid;

    private List<Account> cachedAccounts = new ArrayList<>();

    public AccountList(RoomPickerClient client) {
        this.client = client;

        add(searchField = createSearchField());
        add(grid = createGridAccounts());

        refresh();
    }

    private TextField createSearchField() {
        var field = new TextField("Search by account entityname");
        field.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        field.setClearButtonVisible(true);
        field.setHelperText("Press Enter to search");
        field.addValueChangeListener(event -> applySearch(event.getValue()));
        field.setValueChangeMode(ValueChangeMode.EAGER);
        field.setMinWidth(30, Unit.PERCENTAGE);
        return field;
    }

    private Grid<Account> createGridAccounts() {
        var grid = new Grid<>(Account.class, false);

        grid.addColumn(Account::id).setHeader("Username")
                .setComparator(Comparator.comparing(account -> account.id().getValue())).setSortable(true);

        grid.addComponentColumn(this::createAccountManagementButtons).setFrozenToEnd(true)
                .setTextAlign(ColumnTextAlign.END).setHeader(createToolItems());

        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);

        return grid;
    }

    private HorizontalLayout createAccountManagementButtons(Account account) {
        var layout = new HorizontalLayout(JustifyContentMode.END);

        {
            var button = new Button("Manage", VaadinIcon.COG_O.create());
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            button.addClickListener(event -> {
                getUI().ifPresent(ui -> {
                    ui.navigate("/admin/accounts/" + account.id());
                });
            });
            layout.add(button);
        }

        return layout;
    }

    private HorizontalLayout createToolItems() {
        var layout = new HorizontalLayout(JustifyContentMode.END);

        layout.add(createRegisterAccountButton());
        layout.add(createRefreshButton());

        return layout;
    }

    private Button createRegisterAccountButton() {
        var button = new Button("Register account", VaadinIcon.PLUS.create());
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        button.addClickListener(event -> openAccountRegistrationDialog());
        return button;
    }

    @Override
    public void refresh() {
        var ids = client.getAccountRepository().allAccountsIds();
        cachedAccounts = client.getAccountRepository().getAccounts(ids);
        applySearch(searchField.getValue());
    }

    public void applySearch(String input) {
        var temp = input.trim();

        grid.setItems(cachedAccounts.stream()
                .filter(account -> account.id().getValue().startsWith(temp))
                .toList());
    }

    private void openAccountRegistrationDialog() {
        var dialog = new Dialog();
        dialog.setHeaderTitle("Register new account");

        var closeButton = new Button(LumoIcon.CROSS.create(), e -> dialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.getHeader().add(closeButton);

        var layout = new VerticalLayout();

        var fieldEntityname = new TextField("Username");
        fieldEntityname.setWidth(70, Unit.PERCENTAGE);
        layout.add(fieldEntityname);

        var fieldPassword = new PasswordField("Password");
        fieldPassword.setWidth(70, Unit.PERCENTAGE);
        layout.add(fieldPassword);

        var fieldConfirmPassword = new PasswordField("Confirm password");
        fieldConfirmPassword.setWidth(70, Unit.PERCENTAGE);
        layout.add(fieldConfirmPassword);

        layout.add(new H3("Permissions"));

        var permissionsList = new ArrayList<PermissionCheckBox>();
        for (var permission: Permission.values()) {
            if (permission == Permission.ADMIN) continue;

            var comp = new PermissionCheckBox(permission);
            permissionsList.add(comp);
            layout.add(comp);
        }

        {
            var button = new Button("Register");
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            button.setWidth(100, Unit.PERCENTAGE);
            button.addClickListener(event -> {
                validateAndRegister(dialog, fieldEntityname, fieldPassword, fieldConfirmPassword, permissionsList);
            });
            dialog.getFooter().add(button);
        }

        dialog.add(layout);
        dialog.setMinWidth(50, Unit.PERCENTAGE);
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        dialog.open();
    }

    private void validateAndRegister(Dialog dialog, TextField entitynameField, PasswordField passwordField, PasswordField confirmPasswordField, List<PermissionCheckBox> permissionCheckBoxes) {
        var entityname = entitynameField.getValue().trim();
        var password = passwordField.getValue();
        var confirmPassword = confirmPasswordField.getValue();

        if (entityname.length() < 3 || entityname.length() > 32) {
            Notifications.error("Invalid entityname length. Valid is 3-32");
            return;
        }

        if (password.length() < 5 || password.length() > 32) {
            Notifications.error("Invalid password length. Valid is 5-32");
            return;
        }

        if (!password.equals(confirmPassword)) {
            Notifications.error("Passwords are not equals");
            return;
        }

        var permissions = permissionCheckBoxes.stream()
                .filter(AbstractField::getValue)
                .map(PermissionCheckBox::getOption)
                .toList();

        client.getAccountRepository().createAccount(AccountId.of(entityname), password, permissions);

        dialog.close();
        refresh();
    }

    public static class PermissionCheckBox extends Checkbox {

        private final Permission option;

        public PermissionCheckBox(Permission option) {
            super(PermissionDescription.of(option));
            this.option = option;
        }

        public Permission getOption() {
            return option;
        }
    }
}
