package com.ict.doanphihung.model;

import org.jetbrains.annotations.Nullable;

public class AppFile {

    @Nullable
    public String path;

    public AppFile() {
    }

    public AppFile(@Nullable String path) {
        this.path = path;
    }
}
