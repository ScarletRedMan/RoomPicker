package ru.dragonestia.picker.cp.error;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;
import lombok.extern.log4j.Log4j2;
import ru.dragonestia.picker.api.exception.ApiException;
import ru.dragonestia.picker.cp.component.Notifications;

import java.security.InvalidParameterException;

@Log4j2
public class ApplicationErrorHandler implements ErrorHandler {

    @Override
    public void error(ErrorEvent errorEvent) {
        if (UI.getCurrent() == null) {
            log.throwing(errorEvent.getThrowable());
            return;
        }

        if (errorEvent.getThrowable() instanceof ApiException ex) {
            execute(() -> {
                Notifications.error(ex.getMessage());
            });
            return;
        }

        if (errorEvent.getThrowable() instanceof InvalidParameterException ex) {
            execute(() -> {
                Notifications.error(ex.getMessage());
            });
            return;
        }

        execute(() -> {
            Notifications.error("Internal server error");
        });
        log.throwing(errorEvent.getThrowable());
    }

    private void execute(Command command) {
        UI.getCurrent().access(command);
    }
}
