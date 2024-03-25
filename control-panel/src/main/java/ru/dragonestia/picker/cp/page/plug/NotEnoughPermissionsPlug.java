package ru.dragonestia.picker.cp.page.plug;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.User;
import ru.dragonestia.picker.api.impl.exception.NotEnoughPermissions;

public class NotEnoughPermissionsPlug extends ErrorPlug implements HasErrorParameter<NotEnoughPermissions> {

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotEnoughPermissions> parameter) {
        return render(this);
    }

    public static int render(ErrorPlug plug) {
        plug.add(new H1("Access denied"));
        plug.add(new Paragraph("You do not have sufficient permissions to access this page"));
        return HttpServletResponse.SC_FORBIDDEN;
    }
}
