package ru.dragonestia.picker.cp.view.plug;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import jakarta.servlet.http.HttpServletResponse;
import ru.dragonestia.picker.api.exception.DoesNotExistsException;

public class NotFoundPlug extends ErrorPlug implements HasErrorParameter<DoesNotExistsException> {

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<DoesNotExistsException> parameter) {
        var ex = parameter.getException();
        init("Error 404", ex.getMessage());
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
