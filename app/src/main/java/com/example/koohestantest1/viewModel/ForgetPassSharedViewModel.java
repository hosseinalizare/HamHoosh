package com.example.koohestantest1.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ForgetPassSharedViewModel extends ViewModel {
    private final MutableLiveData<Boolean> onRemovedClicked = new MutableLiveData<>();

    public void setOnRemovedClicked(boolean onRemoved) {
        onRemovedClicked.setValue(onRemoved);
    }

    public LiveData<Boolean> getOnRemoveClickedRes() {
        return onRemovedClicked;
    }
}
