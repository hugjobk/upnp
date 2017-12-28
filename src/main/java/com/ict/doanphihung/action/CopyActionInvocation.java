package com.ict.doanphihung.action;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;

public class CopyActionInvocation extends ActionInvocation {

    @SuppressWarnings("unchecked")
    public CopyActionInvocation(Service service, String copied) {
        super(service.getAction("copy"));
        setInput("copied", copied);
    }
}
