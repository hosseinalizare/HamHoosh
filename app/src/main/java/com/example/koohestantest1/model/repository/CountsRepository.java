package com.example.koohestantest1.model.repository;

import android.util.Log;

import com.example.koohestantest1.model.Count;
import com.example.koohestantest1.model.CounterRequest;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.example.koohestantest1.model.repository.callback.CountsCallBack;

import com.example.koohestantest1.model.network.CounterAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CountsRepository {
    private Retrofit retrofit = RetrofitInstance.getRetrofit();
    private CounterAPI counterAPI;
    private CountsCallBack countsCallBack;
    private String TAG = CountsRepository.class.getSimpleName();

    public CountsRepository(CountsCallBack countsCallBack) {
        this.countsCallBack = countsCallBack;
        counterAPI = retrofit.create(CounterAPI.class);
    }

    public void callForCounts(String userId, String token, String companyId) {
        Log.d(TAG, "callForCounts: " + userId + "     " + companyId);
        counterAPI.getCounts(new CounterRequest(userId,token,companyId)).enqueue(new Callback<Count>() {
            @Override
            public void onResponse(Call<Count> call, Response<Count> response) {
                if (response.isSuccessful()) {
                    countsCallBack.onSuccess(response.body());

                    Log.d(TAG, "onResponse: " + response.body().getCompanyDetails().toString());
                }
            }

            @Override
            public void onFailure(Call<Count> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                countsCallBack.onError(t.getMessage());
            }
        });
    }
}
