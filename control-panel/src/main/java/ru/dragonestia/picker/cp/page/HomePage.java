package ru.dragonestia.picker.cp.page;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;

import java.awt.*;

@Route(value = "/", layout = MainLayout.class)
public class HomePage extends VerticalLayout {

    public HomePage() {
        super();

        var field = new TextField("Some field");
        field.setRequired(true);
        add(field);

        var button = new Button("Click me", e -> {
            Notification.show(field.isInvalid() + "");
        });

        add(button);
    }
}
