package com.ict.doanphihung.action;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;

public class SetVolumeActionInvocation extends ActionInvocation {

    @SuppressWarnings("unchecked")
    public SetVolumeActionInvocation(Service service, int volume) {
        super(service.getAction("setVolume"));
        setInput("volume", volume);
    }
}
