package com.example.koohestantest1.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.koohestantest1.model.repository.LocalCartRepository;

import com.example.koohestantest1.classDirectory.BaseCodeClass;

public class OnClearFromRecentService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ClearFromRecentService", "Service Started");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ClearFromRecentService", "Service Destroyed");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");
        //Code here
        LocalCartRepository localCartRepository = new LocalCartRepository(getApplication());
        localCartRepository.updateCartInformation(BaseCodeClass.sendOrderClass);
        stopSelf();
    }
}