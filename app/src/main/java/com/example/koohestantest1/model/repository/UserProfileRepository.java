package com.example.koohestantest1.model.repository;

import android.util.Log;

import com.example.koohestantest1.model.network.RetrofitInstance;
import com.example.koohestantest1.model.network.UserProfileAPI;
import com.example.koohestantest1.model.repository.callback.UserProfileCallBack;

import com.example.koohestantest1.classDirectory.UserProfile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserProfileRepository {
    private Retrofit retrofit;
    private UserProfileAPI userProfileAPI;
    private UserProfileCallBack profileCallBack;
    private String TAG = UserProfileRepository.class.getSimpleName();

    public UserProfileRepository(UserProfileCallBack userProfileCallBack) {
        retrofit = RetrofitInstance.getRetrofit();
        userProfileAPI = retrofit.create(UserProfileAPI.class);
        profileCallBack = userProfileCallBack;
    }


    public void callForUserData(String userId, String token, String reqId) {
        Call<UserProfile> call = userProfileAPI.callForUserById(userId, token, reqId);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful())
                    profileCallBack.onSuccess(response.body());
                Log.d(TAG, "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                profileCallBack.onFailure(t.getMessage());
            }
        });
    }

}
