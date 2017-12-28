package com.ict.doanphihung.action;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;

public class SetPathActionInvocation extends ActionInvocation {

    @SuppressWarnings("unchecked")
    public SetPathActionInvocation(Service service, String path) {
        super(service.getAction("setPath"));
        setInput("path", path);
    }
}
