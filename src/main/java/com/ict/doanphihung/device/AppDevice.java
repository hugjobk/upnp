package com.ict.doanphihung.device;

import com.ict.doanphihung.service.AppService;
import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.*;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDN;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.UUID;

public class AppDevice {

    @Nullable
    private static LocalDevice device;

    @NotNull
    @SuppressWarnings("unchecked")
    public static LocalDevice createDevice() throws ValidationException, IOException {
        DeviceIdentity identity = new DeviceIdentity(new UDN(UUID.randomUUID()));
        DeviceType type = new AppDeviceType();
        DeviceDetails details = new AppDeviceDetails();
        Icon icon = new AppIcon();

        LocalService<AppService> service = new AnnotationLocalServiceBinder().read(AppService.class);
        service.setManager(new DefaultServiceManager<>(service, AppService.class));

        return new LocalDevice(identity, type, details, icon, service);
    }

    @SuppressWarnings("unchecked")
    public static LocalDevice getDevice() throws ValidationException, IOException {
        if (device == null) {
            device = createDevice();
        }
        return device;
    }
}
