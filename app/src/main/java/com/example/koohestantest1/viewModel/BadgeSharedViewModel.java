package com.example.koohestantest1.viewModel;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koohestantest1.Utils.BadgeCounter;

public class BadgeSharedViewModel extends ViewModel {
    private final MutableLiveData<Integer> liveCount = new MutableLiveData<>();

    public BadgeSharedViewModel() {
        updateCount();
    }

    public void setCount(int count) {
        liveCount.setValue(count);
        BadgeCounter.setCount(count);
    }

    public LiveData<Integer> getLiveCount() {
        return liveCount;
    }

    public void updateCount(){
        liveCount.setValue(BadgeCounter.getCount());
    }
}
