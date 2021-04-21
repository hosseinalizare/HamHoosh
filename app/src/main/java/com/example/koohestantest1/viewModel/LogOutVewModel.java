package com.example.koohestantest1.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.koohestantest1.model.repository.LogOutRepository;
import com.example.koohestantest1.model.repository.callback.LogOutCallBacks;

public class LogOutVewModel extends AndroidViewModel implements LogOutCallBacks {
    private LogOutRepository logOutRepository;
    private MutableLiveData<String> liveDataLogoutResult = new MutableLiveData<>();

    public LogOutVewModel(@NonNull Application application) {
        super(application);
        logOutRepository = new LogOutRepository(application, this);
    }

    public LiveData<String> getLiveDataLogoutResult() {
        return liveDataLogoutResult;
    }

    public void clearDatabase() {
        logOutRepository.deleteAllLocalData();
    }


    @Override
    public void onSuccess() {
        liveDataLogoutResult.postValue("ok");
    }

    @Override
    public void onError(String s) {
        liveDataLogoutResult.postValue(s);
    }
}
