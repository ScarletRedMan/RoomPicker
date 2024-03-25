package ru.dragonestia.picker.cp.page;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AnonymousAllowed
@Route("/login")
public class LoginPage extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm formLogin;

    public LoginPage() {
        setAlignItems(Alignment.CENTER);

        add(new Html("<h1><u>RoomPicker!</u></h1>"));
        add(formLogin = createFormLogin());
    }

    private LoginForm createFormLogin() {
        var form = new LoginForm();
        form.setAction("login");
        form.setForgotPasswordButtonVisible(false);

        var i18n = LoginI18n.createDefault();
        i18n.getForm().setTitle(null);
        i18n.getForm().setUsername("Account username");
        i18n.getForm().setSubmit("Login");
        form.setI18n(i18n);
        return form;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(event.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {

            formLogin.setError(true);
        }
    }
}
