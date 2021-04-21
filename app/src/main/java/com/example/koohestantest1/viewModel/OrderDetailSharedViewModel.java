package com.example.koohestantest1.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koohestantest1.model.repository.callback.UpdateOrderCallBack;
import com.example.koohestantest1.model.repository.UpdateOrderRepository;

import com.example.koohestantest1.ViewModels.UpdateOrderClass;
import com.example.koohestantest1.classDirectory.GetResualt;

public class OrderDetailSharedViewModel extends ViewModel implements UpdateOrderCallBack {
    private MutableLiveData<Integer> canceledStateLiveData = new MutableLiveData<>();
    private MutableLiveData<GetResualt> cancelOrderResLive = new MutableLiveData<>();
    private MutableLiveData<String> cancelOrderErrorLive = new MutableLiveData<>();

    private UpdateOrderRepository updateOrderRepository = new UpdateOrderRepository(this);
    public void cancelOrder(){

    }

    public void callForUpdate(UpdateOrderClass updateOrderClass){
        updateOrderRepository.callForUpdating(updateOrderClass);
    }
    public LiveData<GetResualt> getCancelOrderRes(){
        return cancelOrderResLive;
    }

    public LiveData<Integer> getCanceledStatus(){
        return canceledStateLiveData;
    }

    public void setCanceledStatus(int canceledStatus){
        canceledStateLiveData.setValue(canceledStatus);
    }

    @Override
    public void onSuccess(GetResualt resualt) {
        cancelOrderResLive.setValue(resualt);
    }

    @Override
    public void onError(String err) {
        cancelOrderErrorLive.setValue(err);
    }
}
