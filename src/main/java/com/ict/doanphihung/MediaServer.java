package com.ict.doanphihung;

import com.ict.doanphihung.device.AppDevice;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.LocalDevice;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;

public class MediaServer {

    private JButton quitButton;
    private JPanel panel1;
    @Nullable
    private LocalDevice device;

    private MediaServer() {
        UpnpService upnpService = new UpnpServiceImpl();
        try {
            device = AppDevice.getDevice();
            upnpService.getRegistry().addDevice(device);
        } catch (ValidationException | IOException e) {
            e.printStackTrace();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (device != null) {
                upnpService.getControlPoint().getRegistry().removeDevice(device);
            }
            upnpService.shutdown();
        }));
        quitButton.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        MediaServer server = new MediaServer();
        JFrame frame = new JFrame();
        frame.setContentPane(server.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
