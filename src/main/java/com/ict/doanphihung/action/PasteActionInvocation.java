package com.ict.doanphihung.action;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;

public class PasteActionInvocation extends ActionInvocation {

    @SuppressWarnings("unchecked")
    public PasteActionInvocation(Service service, String pasted) {
        super(service.getAction("paste"));
        setInput("pasted", pasted);
    }
}
