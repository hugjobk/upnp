package com.ict.doanphihung.utils.liveData;

import org.jetbrains.annotations.Nullable;

public interface Observer<T> {
    void onStateChanged(@Nullable T value);
}
