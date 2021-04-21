package com.example.koohestantest1.model.repository;

import android.util.Log;

import com.example.koohestantest1.model.BodyVerifiedCode;
import com.example.koohestantest1.model.NewPasswordBody;
import com.example.koohestantest1.model.SmsRecoveryBody;
import com.example.koohestantest1.model.network.AuthorizationAPI;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.example.koohestantest1.model.repository.callback.AuthorizationCallBack;

import com.example.koohestantest1.classDirectory.CheckVerification;
import com.example.koohestantest1.classDirectory.GetResualt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuthorizationRepository {
    private Retrofit retrofit;
    private AuthorizationAPI authorizationAPI;
    private AuthorizationCallBack authorizationCallBack;
    private String TAG = AuthorizationRepository.class.getSimpleName();

    public AuthorizationRepository(AuthorizationCallBack authorizationCallBack) {
        retrofit = RetrofitInstance.getRetrofit();
        authorizationAPI = retrofit.create(AuthorizationAPI.class);
        this.authorizationCallBack = authorizationCallBack;
    }

    public void getSmsCode(SmsRecoveryBody smsRecoveryBody) {
        authorizationAPI.getSmsRecovery(smsRecoveryBody).enqueue(new Callback<GetResualt>() {
            @Override
            public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                Log.d(TAG, "onResponse: " + response.body());
                authorizationCallBack.onSuccessSms(response.body());
            }

            @Override
            public void onFailure(Call<GetResualt> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void setNewPassword(NewPasswordBody newPassword) {
        authorizationAPI.setUpNewPass(newPassword).enqueue(new Callback<GetResualt>() {
            @Override
            public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                Log.d(TAG, "onResponse: " + response.body());
                authorizationCallBack.onSuccessPassword(response.body());
            }

            @Override
            public void onFailure(Call<GetResualt> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void checkVerifiedCode(BodyVerifiedCode bodyVerifiedCode) {
        authorizationAPI.checkCode(bodyVerifiedCode).enqueue(new Callback<CheckVerification>() {
            @Override
            public void onResponse(Call<CheckVerification> call, Response<CheckVerification> response) {
                Log.d(TAG, "onResponse: " + response.body());
                authorizationCallBack.onSuccessVerifiedCode(response.body());
            }

            @Override
            public void onFailure(Call<CheckVerification> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
