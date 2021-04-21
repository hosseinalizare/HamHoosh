package com.example.koohestantest1.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileSharedViewModel extends ViewModel {
    private MutableLiveData<Boolean> liveRefreshData = new MutableLiveData<>();

    public void shouldRefresh(boolean condition) {
        liveRefreshData.setValue(condition);
    }

    public LiveData<Boolean> getRefreshCondition(){
        return liveRefreshData;
    }
}
