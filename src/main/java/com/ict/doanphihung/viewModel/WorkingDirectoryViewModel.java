package com.ict.doanphihung.viewModel;

import com.ict.doanphihung.model.AppDirectory;
import com.ict.doanphihung.utils.liveData.LiveData;
import com.ict.doanphihung.utils.liveData.MutableLiveData;
import org.jetbrains.annotations.Nullable;

public class WorkingDirectoryViewModel {

    @Nullable
    private static WorkingDirectoryViewModel instance;
    private MutableLiveData<AppDirectory> directory;

    public WorkingDirectoryViewModel() {
        directory = new MutableLiveData<>();
    }

    public LiveData<AppDirectory> getDirectory() {
        return directory;
    }

    public void setDirectory(AppDirectory directory) {
        this.directory.setValue(directory);
    }

    public static WorkingDirectoryViewModel getInstance() {
        if (instance == null) {
            instance = new WorkingDirectoryViewModel();
        }
        return instance;
    }
}
