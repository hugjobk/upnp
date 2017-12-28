package com.ict.doanphihung.action;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;

public class GetFilesActionInvocation extends ActionInvocation {

    @SuppressWarnings("unchecked")
    public GetFilesActionInvocation(Service service) {
        super(service.getAction("getFiles"));
        getOutput("files");
    }
}
