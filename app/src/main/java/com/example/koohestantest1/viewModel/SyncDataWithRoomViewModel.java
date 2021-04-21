package com.example.koohestantest1.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.koohestantest1.model.repository.LocalCartRepository;

import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.SendOrderClass;

public class SyncDataWithRoomViewModel extends AndroidViewModel {
    private LocalCartRepository localCartRepository;
    private String TAG = SyncDataWithRoomViewModel.class.getSimpleName();
    private BaseCodeClass baseCodeClass = new BaseCodeClass();

    public SyncDataWithRoomViewModel(@NonNull Application application) {
        super(application);
        localCartRepository = new LocalCartRepository(application);
    }

    //It's for syncing local data(Cart Items) with Room
    public void updateCartInfo(SendOrderClass sendOrderClass) {
        localCartRepository.updateCartInformation(sendOrderClass);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
//        updateCartInfo(BaseCodeClass.sendOrderClass);
        Log.d(TAG, "onCleared: ");
        //sync data with room when this VM removed by system
        // it is called when Main2Activity is removed from memory(when user exits from app)
        //Main2Activity is singleTop and always is in memory except when app lifecycle is finished
    }
}
