package com.ict.doanphihung.utils.liveData;

public class MutableLiveData<T> extends LiveData<T> {
    public void setValue(T value) {
        this.value = value;
        broadCast();
    }
}
