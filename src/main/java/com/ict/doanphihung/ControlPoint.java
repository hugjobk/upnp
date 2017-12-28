package com.ict.doanphihung;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ict.doanphihung.action.*;
import com.ict.doanphihung.model.AppDirectory;
import com.ict.doanphihung.model.AppFile;
import com.ict.doanphihung.service.MyService;
import com.ict.doanphihung.utils.liveData.Observer;
import com.ict.doanphihung.viewModel.DeviceListViewModel;
import com.ict.doanphihung.viewModel.WorkingDirectoryViewModel;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.action.ActionArgumentValue;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UDAServiceId;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ControlPoint {

    private JPanel panelMain;
    private JList listDevice;
    private JSlider sliderVolume;
    private JList listDirectory;
    private JButton backButton;
    private UpnpService upnpService;
    private DeviceListViewModel deviceListViewModel;
    private WorkingDirectoryViewModel workingDirectoryViewModel;
    private Observer<List<Device>> deviceListObserver;
    private Observer<AppDirectory> workingDirectoryObserver;
    @Nullable
    private Service service;

    @SuppressWarnings("unchecked")

    private ControlPoint() {
        new Thread(new MyService()).run();
        deviceListViewModel = DeviceListViewModel.getInstance();
        workingDirectoryViewModel = WorkingDirectoryViewModel.getInstance();
        upnpService = new UpnpServiceImpl();

        deviceListObserver = devices -> {
            if (devices == null) {
                return;
            }
            DefaultListModel model = new DefaultListModel();
            for (Device device : devices) {
                model.addElement(device.getDetails().getFriendlyName());
            }
            listDevice.setModel(model);
        };

        workingDirectoryObserver = directory -> {
            if (directory == null) {
                return;
            }
            DefaultListModel model = new DefaultListModel();
            for (AppDirectory d : directory.directories) {
                model.addElement(d.path);
            }
            for (AppFile f : directory.files) {
                model.addElement(f.path);
            }
            listDirectory.setModel(model);
        };

        listDevice.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                List<Device> devices = deviceListViewModel.getDevices().getValue();
                if (devices == null) {
                    return;
                }
                Device selected = devices.get(listDevice.getSelectedIndex());
                service = selected.findService(new UDAServiceId("AppService"));
                if (service != null) {
                    upnpService.getControlPoint().execute(new GetFilesActionCallback(service));
                }
            }
        });

        listDirectory.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (SwingUtilities.isLeftMouseButton(e) && service != null) {
                    String path = (String) listDirectory.getSelectedValue();
                    File file = new File(path);
                    List<String> list;
                    if (file.isDirectory()) {
                        String[] array = {"Open", "Paste", "Delete"};
                        list = new ArrayList<>(Arrays.asList(array));
                    } else {
                        String[] array = {"Open", "Copy", "Delete"};
                        list = new ArrayList<>(Arrays.asList(array));
                    }
                    String[] options = list.toArray(new String[list.size()]);
                    int choice = JOptionPane.showOptionDialog(
                            null,
                            "Select an action?",
                            "Options",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            options,
                            options[0]
                    );
                    switch (options[choice]) {
                        case "Open":
                            upnpService.getControlPoint().execute(new SetPathActionCallback(service, path));
                            break;
                        case "Copy":
                            upnpService.getControlPoint().execute(new CopyActionCallback(service, path));
                            break;
                        case "Paste":
                            upnpService.getControlPoint().execute(new PasteActionCallback(service, path));
                            break;
                        case "Delete":
                            upnpService.getControlPoint().execute(new DeleteActionCallback(service, path));
                            break;
                    }
                }
            }
        });

        sliderVolume.addChangeListener(e -> {
            if (service != null && !sliderVolume.getValueIsAdjusting()) {
                int volume = sliderVolume.getValue() * 100 / sliderVolume.getMaximum();
                upnpService.getControlPoint().execute(new SetVolumeActionCallback(service, volume));
            }
        });

        backButton.addActionListener(e -> {
            if (service != null) {
                upnpService.getControlPoint().execute(new SetPathActionCallback(service, ".."));
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        ControlPoint controlPoint = new ControlPoint();
        frame.setContentPane(controlPoint.panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        controlPoint.deviceListViewModel.getDevices().observeAndFire(controlPoint.deviceListObserver);
        controlPoint.workingDirectoryViewModel.getDirectory().observeAndFire(controlPoint.workingDirectoryObserver);
    }

    private class GetFilesActionCallback extends ActionCallback {

        protected GetFilesActionCallback(Service service) {
            super(new GetFilesActionInvocation(service));
        }

        @Override
        public void success(ActionInvocation invocation) {
            ActionArgumentValue[] values = invocation.getOutput();
            if (values.length > 0) {
                String value = (String) values[0].getValue();
                Gson gson = new GsonBuilder().create();
                AppDirectory directory = gson.fromJson(value, AppDirectory.class);
                workingDirectoryViewModel.setDirectory(directory);
            }
        }

        @Override
        public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
            JOptionPane.showMessageDialog(null, defaultMsg);
        }
    }

    private class SetPathActionCallback extends ActionCallback {

        private Service service;
        private String path;

        protected SetPathActionCallback(Service service, String path) {
            super(new SetPathActionInvocation(service, path));
            this.service = service;
            this.path = path;
        }

        @Override
        public void success(ActionInvocation invocation) {
            File file = new File(path);
            if (file.isDirectory() || path.equals("..")) {
                upnpService.getControlPoint().execute(new GetFilesActionCallback(service));
            }
        }

        @Override
        public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
            JOptionPane.showMessageDialog(null, defaultMsg);
        }
    }

    private class SetVolumeActionCallback extends ActionCallback {

        protected SetVolumeActionCallback(Service service, int volume) {
            super(new SetVolumeActionInvocation(service, volume));
        }

        @Override
        public void success(ActionInvocation invocation) {
            JOptionPane.showMessageDialog(null, "Changed Volume Succeed");
        }

        @Override
        public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
            JOptionPane.showMessageDialog(null, defaultMsg);
        }
    }

    private class CopyActionCallback extends ActionCallback {

        private String copied;

        protected CopyActionCallback(Service service, String copied) {
            super(new CopyActionInvocation(service, copied));
            this.copied = copied;
        }

        @Override
        public void success(ActionInvocation invocation) {
            JOptionPane.showMessageDialog(null, "Copy " + copied + "to clipboard");
        }

        @Override
        public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
            JOptionPane.showMessageDialog(null, defaultMsg);
        }
    }

    private class PasteActionCallback extends ActionCallback {

        protected PasteActionCallback(Service service, String pasted) {
            super(new PasteActionInvocation(service, pasted));
        }

        @Override
        public void success(ActionInvocation invocation) {
            JOptionPane.showMessageDialog(null, "Paste succeed");
        }

        @Override
        public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
            JOptionPane.showMessageDialog(null, defaultMsg);
        }
    }

    private class DeleteActionCallback extends ActionCallback {

        private Service service;

        protected DeleteActionCallback(Service service, String path) {
            super(new DeleteActionInvocation(service, path));
            this.service = service;
        }

        @Override
        public void success(ActionInvocation invocation) {
            upnpService.getControlPoint().execute(new GetFilesActionCallback(service));
        }

        @Override
        public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
            JOptionPane.showMessageDialog(null, defaultMsg);
        }
    }
}
