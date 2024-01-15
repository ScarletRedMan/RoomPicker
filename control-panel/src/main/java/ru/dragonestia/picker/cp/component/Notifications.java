package ru.dragonestia.picker.cp.component;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Notifications {

    public Notification success(String text) {
        var notification = create(text);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.open();
        return notification;
    }

    public Notification warn(String text) {
        var notification = create(text);
        notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
        notification.open();
        return notification;
    }

    public Notification error(String text) {
        var notification = create(text);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.open();
        return notification;
    }

    private Notification create(String text) {
        return new Notification(text, 3000, Notification.Position.TOP_END);
    }
}
