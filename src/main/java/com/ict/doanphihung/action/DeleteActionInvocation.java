package com.ict.doanphihung.action;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;

public class DeleteActionInvocation extends ActionInvocation {

    @SuppressWarnings("unchecked")
    public DeleteActionInvocation(Service service, String deleted) {
        super(service.getAction("delete"));
        setInput("deleted", deleted);
    }
}
