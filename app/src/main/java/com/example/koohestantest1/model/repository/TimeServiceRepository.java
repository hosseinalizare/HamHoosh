package com.example.koohestantest1.model.repository;

import android.util.Log;

import com.example.koohestantest1.model.MyTime;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.example.koohestantest1.model.network.TimeApi;
import com.example.koohestantest1.model.repository.callback.TimeServiceCallBacks;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TimeServiceRepository {
    private final TimeServiceCallBacks timeServiceCallBacks;
    private final String TAG = TimeServiceRepository.class.getSimpleName();
    private final TimeApi timeApi;

    public TimeServiceRepository(TimeServiceCallBacks timeServiceCallBacks) {
        this.timeServiceCallBacks = timeServiceCallBacks;
        Retrofit retrofit = RetrofitInstance.getRetrofit();
        timeApi = retrofit.create(TimeApi.class);
    }

    public void callCurrentTimeAPI(int timeMode){
        Call<MyTime> getCurrentTime = timeApi.getCurrentTime(timeMode);
        getCurrentTime.enqueue(new Callback<MyTime>() {
            @Override
            public void onResponse(@NotNull Call<MyTime> call, @NotNull Response<MyTime> response) {
                Log.d(TAG, "onResponse: " + response.body());
                Log.d(TAG, "onResponse: header" + response.headers());
                Log.d(TAG, "onResponse: message" + response.message());
                Log.d(TAG, "onResponse: " + response.errorBody());
                Log.d(TAG, "onResponse: " + response.code());

                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: success");
                    timeServiceCallBacks.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(@NotNull Call<MyTime> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                timeServiceCallBacks.onError(t.getMessage());
            }
        });
    }
}
