package com.ict.doanphihung.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ict.doanphihung.model.AppDirectory;
import com.ict.doanphihung.model.AppFile;
import org.fourthline.cling.binding.annotations.*;
import org.fourthline.cling.model.types.InvalidValueException;
import org.jetbrains.annotations.Nullable;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@UpnpService(
        serviceId = @UpnpServiceId("AppService"),
        serviceType = @UpnpServiceType(value = "AppService")
)
public class AppService {

    @UpnpStateVariable(defaultValue = "0")
    private int volume;

    @UpnpStateVariable()
    private String path;

    @UpnpStateVariable()
    private String files;

    @UpnpStateVariable
    private String deleted;

    @Nullable
    @UpnpStateVariable
    private String copied;

    @UpnpStateVariable
    private String pasted;

    @UpnpAction(name = "setVolume")
    public void setVolume(@UpnpInputArgument(name = "volume") int volume) {
        if (volume < 0 || volume > 100) {
            throw new InvalidValueException("Volume must be between 0 and 100");
        }
        try {
            Clip clip = AudioSystem.getClip();
            FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            this.volume = volume;
        } catch (LineUnavailableException e) {
            throw new InvalidValueException("Cannot change volume");
        }
    }

    @UpnpAction(name = "getVolume", out = @UpnpOutputArgument(name = "volume"))
    public int getVolume() {
        return volume;
    }

    @UpnpAction(name = "setPath")
    public void setPath(@UpnpInputArgument(name = "path") String path) {
        if (path.equals("..")) {
            File file = new File(this.path);
            if (file.getAbsolutePath().equals(System.getProperty("user.home"))) {
                return;
            }
            this.path = file.getParentFile().getAbsolutePath();
        } else {
            File file = new File(path);
            if (!file.exists()) {
                throw new InvalidValueException("Invalid path");
            }
            if (file.isDirectory()) {
                this.path = path;
            } else {
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    throw new InvalidValueException("Cannot open this file");
                }
            }
        }
    }

    @UpnpAction(name = "getPath", out = @UpnpOutputArgument(name = "path"))
    public String getPath() {
        return path;
    }

    @UpnpAction(name = "delete")
    public void setDeleted(@UpnpInputArgument(name = "deleted") String deleted) {
        File file = new File(deleted);
        if (!file.exists()) {
            throw new InvalidValueException("No file exists at: " + deleted);
        }
        file.delete();
    }

    @UpnpAction(name = "copy")
    public void setCopied(@UpnpInputArgument(name = "copied") String copied) {
        File file = new File(copied);
        if (!file.exists() || !file.isFile()) {
            throw new InvalidValueException("No file exists at " + copied);
        }
        this.copied = copied;
    }

    @UpnpAction(name = "paste")
    public void setPasted(@UpnpInputArgument(name = "pasted") String pasted) {
        if (copied == null) {
            throw new InvalidValueException("No file in clipboard");
        }
        File c = new File(copied);
        File dir = new File(pasted);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new InvalidValueException("No directory exists at " + pasted);
        }
        File p = new File(pasted + "/" + c.getName());
        try {
            Files.copy(c.toPath(), p.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new InvalidValueException("Copy failed");
        }
    }

    @UpnpAction(name = "getFiles", out = @UpnpOutputArgument(name = "files"))
    public String getFiles() {
        AppDirectory directory = new AppDirectory(path != null ? path : System.getProperty("user.home"));
        findAll(directory);
        Gson gson = new GsonBuilder().create();
        return gson.toJson(directory);
    }

    public void findAll(AppDirectory directory) {
        if (directory.path == null) {
            return;
        }
        File current = new File(directory.path);
        File[] children = current.listFiles();
        if (children == null) {
            return;
        }
        for (File child : children) {
            if (!child.isHidden()) {
                if (child.isDirectory()) {
                    directory.directories.add(new AppDirectory(child.getAbsolutePath()));
                } else {
                    directory.files.add(new AppFile(child.getAbsolutePath()));
                }
            }
        }
    }
}
