package com.example.koohestantest1.ApiDirectory;

import com.example.koohestantest1.classDirectory.GetCheckAccess;
import com.example.koohestantest1.classDirectory.SendPasswordRecovery;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PasswordRecoveryApi {

    @POST("SmssVerification/SetPasswordRecovery")
    Call<GetCheckAccess> getCheckAccess(@Body SendPasswordRecovery sendPasswordRecovery);
    void onResponsePasswordRecovery(GetCheckAccess getCheckAccess);
}
