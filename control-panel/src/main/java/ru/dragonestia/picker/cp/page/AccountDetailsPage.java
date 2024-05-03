package ru.dragonestia.picker.cp.page;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.model.account.IAccount;
import ru.dragonestia.picker.cp.component.AccountList;
import ru.dragonestia.picker.cp.component.Notifications;
import ru.dragonestia.picker.cp.model.Permission;
import ru.dragonestia.picker.cp.service.SecurityService;
import ru.dragonestia.picker.cp.util.RouteParamsExtractor;

import java.util.ArrayList;

@RequiredArgsConstructor
@RolesAllowed("ADMIN")
@PageTitle("Account details")
@Route(value = "/admin/accounts/:accountId", layout = MainLayout.class)
public class AccountDetailsPage extends VerticalLayout implements BeforeEnterObserver {

    private final SecurityService securityService;
    private final RouteParamsExtractor paramsExtractor;

    private RoomPickerClient client;
    private IAccount account;

    @PostConstruct
    void postConstruct() {
        client = securityService.getAuthenticatedAccount().getClient();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        account = paramsExtractor.extractAccount(event);

        init();
    }

    private void init() {
        add(new H2("Account management"));
        add(new Html("<span>Username: <b>%s</b></span>".formatted(account.getUsername())));

        add(new Hr());
        add(new H3("Change password"));
        createChangePassword();

        add(new Hr());
        add(new H3("Update permissions"));
        createEditPermissions();

        add(new Hr());
        add(new H3("Delete account"));
        add(createDeleteAccountButton());
    }

    private void createChangePassword() {
        var newPassword = new PasswordField("New password");
        newPassword.setMinWidth(25, Unit.PERCENTAGE);
        add(newPassword);

        var confirmPassword = new PasswordField("Confirm new password");
        confirmPassword.setMinWidth(25, Unit.PERCENTAGE);
        add(confirmPassword);

        var submit = new Button("Submit", event -> {
            var pass = newPassword.getValue();
            var confirm = confirmPassword.getValue();

            if (pass.length() < 5 || pass.length() > 32) {
                Notifications.error("Invalid password length. Valid is 5-32");
                return;
            }

            if (!pass.equals(confirm)) {
                Notifications.error("Passwords are not equals");
                return;
            }

            client.getAccountRepository().setPassword(account, pass);
            Notifications.success("Password successfully changed!");
            newPassword.setValue("");
            confirmPassword.setValue("");
        });
        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(submit);
    }

    private void createEditPermissions() {
        var permissionsList = new ArrayList<AccountList.PermissionCheckBox>();
        for (var permission: Permission.Enum.values()) {
            var comp = new AccountList.PermissionCheckBox(permission);
            comp.setValue(account.getPermissions().contains(permission.name()));
            permissionsList.add(comp);
            add(comp);
        }

        var button = new Button("Update permissions", event -> {
            var permissions = permissionsList.stream()
                    .filter(AbstractField::getValue)
                    .map(AccountList.PermissionCheckBox::getOption)
                    .map(Enum::name)
                    .toList();

            client.getAccountRepository().setPermissions(account, permissions);
            Notifications.success("Permissions successfully changed!");
        });
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(button);
    }

    private Button createDeleteAccountButton() {
        var button = new Button("Delete this account", event -> {
            client.getAccountRepository().removeAccount(account);
            Notifications.warn("Account '%s' was deleted.".formatted(account.getUsername()));

            getUI().ifPresent(ui -> {
                ui.navigate("/admin/accounts");
            });
        });
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        return button;
    }
}
