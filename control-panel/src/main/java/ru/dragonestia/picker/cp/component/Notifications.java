package ru.dragonestia.picker.cp.component;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Notifications {

    public Notification success(String text) {
        var notification = create(VaadinIcon.CHECK_CIRCLE, text);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.open();
        return notification;
    }

    public Notification warn(String text) {
        var notification = create(VaadinIcon.WARNING, text);
        notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
        notification.open();
        return notification;
    }

    public Notification error(String text) {
        var notification = create(VaadinIcon.WARNING, text);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.open();
        return notification;
    }

    private Notification create(VaadinIcon icon, String text) {
        var layout = new HorizontalLayout();
        layout.add(new Icon(icon));
        layout.add(new Html("<span>" + text + "</span>"));

        var notification = new Notification();
        notification.setDuration(5000);
        notification.setPosition(Notification.Position.TOP_END);

        var closeButton = new Button(VaadinIcon.CLOSE_SMALL.create(), event -> notification.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        layout.add(closeButton);

        notification.add(layout);
        return notification;
    }
}
