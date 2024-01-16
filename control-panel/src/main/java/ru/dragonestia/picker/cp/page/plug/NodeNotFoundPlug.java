package ru.dragonestia.picker.cp.page.plug;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import jakarta.servlet.http.HttpServletResponse;
import ru.dragonestia.picker.api.exception.NodeNotFoundException;
import ru.dragonestia.picker.cp.component.NavPath;

public class NodeNotFoundPlug extends ErrorPlug implements HasErrorParameter<NodeNotFoundException> {

    @Override
    public int setErrorParameter(BeforeEnterEvent beforeEnterEvent, ErrorParameter<NodeNotFoundException> errorParameter) {
        var ex = errorParameter.getException();
        var nodeId = ex.getNodeId();

        init(NavPath.toNode(nodeId), "Error 404", ex.getMessage());
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
