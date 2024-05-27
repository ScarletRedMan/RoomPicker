package ru.dragonestia.picker.cp.view;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.dragonestia.picker.api.impl.RoomPickerClient;
import ru.dragonestia.picker.api.impl.exception.AuthException;
import ru.dragonestia.picker.cp.annotation.ServerURL;
import ru.dragonestia.picker.cp.model.AccountSession;
import ru.dragonestia.picker.cp.service.SessionService;
import ru.dragonestia.picker.cp.util.Notifications;

@Route("/login")
@PageTitle("Log in")
public class LoginView extends VerticalLayout {

    private final SessionService sessionService;
    private final String serverUrl;
    private final TextField fieldLogin;
    private final PasswordField fieldPassword;

    public LoginView(SessionService sessionService, @ServerURL String serverURL) {
        this.sessionService = sessionService;
        this.serverUrl = serverURL;

        if (sessionService.getSession() != null) {
            fieldLogin = null;
            fieldPassword = null;

            add(new Button("Logout", event -> getUI().ifPresent(sessionService::logout)));
            return;
        }

        setAlignItems(Alignment.CENTER);

        add(new Html("<h1><u>RoomPicker!</u></h1>"));
        add(fieldLogin = createLoginField());
        add(fieldPassword = createPasswordField());
        add(createLoginButton());
    }

    private TextField createLoginField() {
        var field = new TextField("Username");
        field.setRequired(true);
        field.setMinWidth(20, Unit.PERCENTAGE);
        return field;
    }

    private PasswordField createPasswordField() {
        var field = new PasswordField("Password");
        field.setRequired(true);
        field.setMinWidth(20, Unit.PERCENTAGE);
        return field;
    }

    private Button createLoginButton() {
        var button = new Button("Login", event -> tryLogin());
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.setMinWidth(20, Unit.PERCENTAGE);
        return button;
    }

    private void tryLogin() {
        var client = new RoomPickerClient(serverUrl, fieldLogin.getValue(), fieldPassword.getValue());
        try {
            var account = client.getAccount();
            var session = new AccountSession(account, fieldPassword.getValue(), client);
            getUI().ifPresent(ui -> {
                sessionService.login(session, ui);
            });
        } catch (AssertionError | AuthException ex) {
            Notifications.error("Invalid username or password");
        }
    }
}
