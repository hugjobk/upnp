package com.ict.doanphihung.utils.liveData;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LiveData<T> {

    protected List<Observer<T>> observers;
    @Nullable
    protected T value;

    public LiveData() {
        observers = new ArrayList<>();
    }

    @Nullable
    public T getValue() {
        return value;
    }

    protected void broadCast() {
        for (Observer<T> observer : observers) {
            observer.onStateChanged(value);
        }
    }

    public void observe(Observer<T> observer) {
        observers.add(observer);
    }

    public void observeAndFire(Observer<T> observer) {
        observe(observer);
        observer.onStateChanged(value);
    }
}
