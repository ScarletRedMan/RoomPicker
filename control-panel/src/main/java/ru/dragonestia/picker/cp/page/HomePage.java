package ru.dragonestia.picker.cp.page;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

@Route(value = "/", layout = MainLayout.class)
public class HomePage extends VerticalLayout {

    public HomePage() {
        super();

        add(new H1("Hello world!"));
        add(new Paragraph("Hello world!"));
    }
}
