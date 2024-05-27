package ru.dragonestia.picker.cp.listener;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.spring.annotation.SpringComponent;
import ru.dragonestia.picker.cp.error.ApplicationErrorHandler;

@SpringComponent
public class VaadinEventListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addSessionInitListener(e -> {
            e.getSession().setErrorHandler(new ApplicationErrorHandler());
        });
    }
}
