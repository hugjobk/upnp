package com.ict.doanphihung.viewModel;

import com.ict.doanphihung.utils.liveData.LiveData;
import com.ict.doanphihung.utils.liveData.MutableLiveData;
import org.fourthline.cling.model.meta.Device;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DeviceListViewModel {

    private MutableLiveData<List<Device>> devices;
    @Nullable
    private static DeviceListViewModel instance;

    public DeviceListViewModel() {
        devices = new MutableLiveData<>();
    }

    public static DeviceListViewModel getInstance() {
        if (instance == null) {
            instance = new DeviceListViewModel();
        }
        return instance;
    }

    public LiveData<List<Device>> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices.setValue(devices);
    }

    public void addDevice(Device device) {
        List<Device> devices = this.devices.getValue();
        if (devices == null) {
            devices = new ArrayList<>();
        }
        devices.add(device);
        this.devices.setValue(devices);
    }

    public void removeDevice(Device device) {
        List<Device> devices = this.devices.getValue();
        if (devices == null) {
            return;
        }
        devices.remove(device);
        this.devices.setValue(devices);
    }
}
