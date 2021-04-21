package com.example.koohestantest1.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koohestantest1.model.MyTime;
import com.example.koohestantest1.model.repository.callback.TimeServiceCallBacks;
import com.example.koohestantest1.model.repository.TimeServiceRepository;

import java.util.Date;

public class TimeViewModel extends ViewModel implements TimeServiceCallBacks {
    private final TimeServiceRepository timeServiceRepository;
    private final MutableLiveData<MyTime> currentTimeLiveData;
    private final MutableLiveData<String> errorHandlerLiveData;
    private final MutableLiveData<String> differenceTimeLiveData;

    //Share Time Between view and adapter
    private static MyTime sharedTime;

    public TimeViewModel() {
        currentTimeLiveData = new MutableLiveData<>();
        this.timeServiceRepository = new TimeServiceRepository(this);
        this.errorHandlerLiveData = new MutableLiveData<>();
        differenceTimeLiveData = new MutableLiveData<>();
    }

    public void callForCurrentTime(int timeMode){
        timeServiceRepository.callCurrentTimeAPI(timeMode);
    }

    public void callForTimeWithDifferences(int timeMode , Date start){
        callForCurrentTime(timeMode);
    }
    public LiveData<MyTime> getCurrentTimeLiveData(){
        return currentTimeLiveData;
    }


    public LiveData<String> getErrorMessage(){
        return errorHandlerLiveData;
    }

    /*
        get Data from repository and init livedata
     */
    @Override
    public void onSuccess(MyTime res) {
        currentTimeLiveData.setValue(res);
    }

    @Override
    public void onError(String err) {
        errorHandlerLiveData.setValue(err);
    }

    public static MyTime getSharedTime() {
        return sharedTime;
    }

    public static void setSharedTime(MyTime sharedTime) {
        sharedTime = sharedTime;
    }
}
