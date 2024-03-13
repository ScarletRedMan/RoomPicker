package ru.dragonestia.picker.cp.page;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dragonestia.picker.api.impl.RoomPickerClient;

@Route("/login")
public class LoginPage extends VerticalLayout {

    private final RoomPickerClient client;
    private final TextField fieldLogin;
    private final PasswordField fieldPassword;

    @Autowired
    public LoginPage(RoomPickerClient client) {
        this.client = client;

        setAlignItems(Alignment.CENTER);

        add(new Html("<h1><u>RoomPicker!</u></h1>"));
        add(fieldLogin = createLoginField());
        add(fieldPassword = createPasswordField());
        add(createLoginButton());
    }

    private TextField createLoginField() {
        var field = new TextField("Account login");
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
        var button = new Button("Login");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.setMinWidth(20, Unit.PERCENTAGE);
        return button;
    }
}
