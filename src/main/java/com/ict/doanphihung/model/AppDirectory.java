package com.ict.doanphihung.model;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AppDirectory {

    @Nullable
    public String path;
    public List<AppDirectory> directories;
    public List<AppFile> files;

    public AppDirectory() {
        directories = new ArrayList<>();
        files = new ArrayList<>();
    }

    public AppDirectory(@Nullable String path) {
        this();
        this.path = path;
    }
}
