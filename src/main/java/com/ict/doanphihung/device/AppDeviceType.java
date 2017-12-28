package com.ict.doanphihung.device;

import org.fourthline.cling.model.types.UDADeviceType;

public class AppDeviceType extends UDADeviceType {

    public static final String TYPE = "MyMediaServer";
    public static final int VERSION = 1;

    public AppDeviceType() {
        super(TYPE, VERSION);
    }
}
