package ru.dragonestia.picker.cp.view;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.*;
import ru.dragonestia.picker.api.model.account.Account;
import ru.dragonestia.picker.api.model.account.Permission;
import ru.dragonestia.picker.cp.component.AccountList;
import ru.dragonestia.picker.cp.service.SessionService;
import ru.dragonestia.picker.cp.util.Notifications;
import ru.dragonestia.picker.cp.util.RouteParamExtractor;
import ru.dragonestia.picker.cp.view.layout.MainLayout;

import java.util.ArrayList;

@PageTitle("UserDetails details")
@Route(value = "/admin/accounts/:accountId", layout = MainLayout.class)
public class AccountView extends SecuredView{

    private Account account;

    public AccountView(SessionService sessionService, RouteParamExtractor paramsExtractor) {
        super(sessionService, paramsExtractor, Permission.ADMIN);
    }

    @Override
    protected void preRender(RouteParameters routeParams) {
        account = getParamsExtractor().account(routeParams);
    }

    @Override
    protected void render() {
        add(new H2("UserDetails management"));
        add(new Html("<span>Entityname: <b>%s</b></span>".formatted(account.id())));

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

            getClient().getAccountRepository().changePassword(account, pass);
            Notifications.success("Password successfully changed!");
            newPassword.setValue("");
            confirmPassword.setValue("");
        });
        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(submit);
    }

    private void createEditPermissions() {
        var permissionsList = new ArrayList<AccountList.PermissionCheckBox>();
        for (var permission: Permission.values()) {
            if (permission == Permission.ADMIN) continue;

            var comp = new AccountList.PermissionCheckBox(permission);
            comp.setValue(account.permissions().contains(permission));
            permissionsList.add(comp);
            add(comp);
        }

        var button = new Button("Update permissions", event -> {
            var permissions = permissionsList.stream()
                    .filter(AbstractField::getValue)
                    .map(AccountList.PermissionCheckBox::getOption)
                    .toList();

            getClient().getAccountRepository().setPermissions(account, permissions);
            Notifications.success("Permissions successfully changed!");
        });
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(button);
    }

    private Button createDeleteAccountButton() {
        var button = new Button("Delete this account", event -> {
            getClient().getAccountRepository().deleteAccount(account.id());
            Notifications.warn("UserDetails '%s' was deleted.".formatted(account.id()));

            getUI().ifPresent(ui -> {
                ui.navigate("/admin/accounts");
            });
        });
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        return button;
    }
}
