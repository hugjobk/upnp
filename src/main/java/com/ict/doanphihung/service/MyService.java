package com.ict.doanphihung.service;

import com.ict.doanphihung.device.AppDeviceType;
import com.ict.doanphihung.viewModel.DeviceListViewModel;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.message.header.DeviceTypeHeader;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;

import java.util.ArrayList;

public class MyService implements Runnable {

    @Override
    public void run() {
        UpnpService upnpService = new UpnpServiceImpl();
        DeviceListViewModel deviceListViewModel = DeviceListViewModel.getInstance();
        DeviceType deviceType = new AppDeviceType();

        Runtime.getRuntime().addShutdownHook(new Thread(upnpService::shutdown));

        deviceListViewModel.setDevices(new ArrayList<>(upnpService.getRegistry().getDevices(deviceType)));
        upnpService.getRegistry().addListener(new DefaultRegistryListener() {
            @Override
            public void deviceAdded(Registry registry, Device device) {
                super.deviceAdded(registry, device);
                if (device.getType().getType().equals(AppDeviceType.TYPE)) {
                    deviceListViewModel.addDevice(device);
                }
            }

            @Override
            public void deviceRemoved(Registry registry, Device device) {
                super.deviceRemoved(registry, device);
                if (device.getType().getType().equals(AppDeviceType.TYPE)) {
                    deviceListViewModel.removeDevice(device);
                }
            }
        });
        upnpService.getControlPoint().search(new DeviceTypeHeader(deviceType));
    }
}
