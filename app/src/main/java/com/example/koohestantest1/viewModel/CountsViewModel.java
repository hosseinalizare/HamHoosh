package com.example.koohestantest1.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koohestantest1.model.Count;
import com.example.koohestantest1.model.repository.CountsRepository;
import com.example.koohestantest1.model.repository.callback.CountsCallBack;

import java.util.List;

public class CountsViewModel extends ViewModel implements CountsCallBack {
    private CountsRepository countsRepository = new CountsRepository(this);
    private MutableLiveData<Count> liveCount = new MutableLiveData<>();
    private MutableLiveData<String> liveCountError = new MutableLiveData<>();

    public void callForCounts( String userId,String token, String companyId) {
        countsRepository.callForCounts(userId, token, companyId);
    }

    public LiveData<Count> getCount() {
        return liveCount;
    }

    public LiveData<String> getError() {
        return liveCountError;
    }

    @Override
    public void onSuccess(Count count) {
        liveCount.setValue(count);
    }

    @Override
    public void onError(String error) {
        liveCountError.setValue(error);
    }
}
