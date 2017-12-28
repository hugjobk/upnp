package com.ict.doanphihung.device;

import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.ManufacturerDetails;

public class AppDeviceDetails extends DeviceDetails {

    public static final String FRIENDLY_NAME = "Media Server";
    public static final String MANUFACTURER = "Doan Phi Hung";

    public AppDeviceDetails() {
        super(FRIENDLY_NAME, new ManufacturerDetails(MANUFACTURER), new AppModelDetails());
    }
}
