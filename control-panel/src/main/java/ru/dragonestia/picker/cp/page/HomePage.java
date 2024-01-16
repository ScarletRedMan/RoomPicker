package ru.dragonestia.picker.cp.page;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import ru.dragonestia.picker.api.exception.NodeNotFoundException;

@Route("/")
public class HomePage extends VerticalLayout implements BeforeEnterObserver {

    public HomePage() {
        super();

        add(new H1("Hello world!"));
        add(new Paragraph("Hello world!"));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        throw new NodeNotFoundException("gdfsg");
    }
}
