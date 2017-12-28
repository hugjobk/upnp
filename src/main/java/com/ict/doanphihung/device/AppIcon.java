package com.ict.doanphihung.device;

import org.fourthline.cling.model.meta.Icon;

import java.io.IOException;

public class AppIcon extends Icon {

    private static final String MIME_TYPE = "image/png";
    private static final int WIDTH = 48;
    private static final int HEIGHT = 48;
    private static final int DEPTH = 8;
    private static final java.net.URL URL = AppIcon.class.getResource("/icon.png");

    public AppIcon() throws IOException {
        super(MIME_TYPE, WIDTH, HEIGHT, DEPTH, URL);
    }
}
