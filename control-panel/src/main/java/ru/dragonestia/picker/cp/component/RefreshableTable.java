package ru.dragonestia.picker.cp.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;

public interface RefreshableTable {

    default Button createRefreshButton() {
        var button = new Button("Refresh", new Icon(VaadinIcon.REFRESH));
        button.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        button.addClickListener(event -> {
            refresh();

            var notification = new Notification("Refreshed!");
            notification.setDuration(1000);
            notification.setPosition(Notification.Position.BOTTOM_END);
            notification.open();
        });
        return button;
    }

    void refresh();
}
