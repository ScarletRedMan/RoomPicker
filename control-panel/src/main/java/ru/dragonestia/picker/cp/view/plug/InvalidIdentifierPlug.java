package ru.dragonestia.picker.cp.view.plug;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import jakarta.servlet.http.HttpServletResponse;
import ru.dragonestia.picker.api.exception.InvalidIdentifierException;

public class InvalidIdentifierPlug extends ErrorPlug implements HasErrorParameter<InvalidIdentifierException> {

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<InvalidIdentifierException> parameter) {
        var ex = parameter.getException();
        init("Error 400", ex.getMessage());
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
