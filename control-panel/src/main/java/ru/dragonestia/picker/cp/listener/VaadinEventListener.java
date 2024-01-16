package ru.dragonestia.picker.cp.listener;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.log4j.Log4j2;
import ru.dragonestia.picker.cp.error.ApplicationErrorHandler;

@Log4j2
@SpringComponent
public class VaadinEventListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addSessionInitListener(e -> {
            e.getSession().setErrorHandler(new ApplicationErrorHandler());
        });
    }
}
