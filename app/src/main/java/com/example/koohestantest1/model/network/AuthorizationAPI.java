package com.example.koohestantest1.model.network;

import com.example.koohestantest1.model.BodyVerifiedCode;
import com.example.koohestantest1.model.NewPasswordBody;
import com.example.koohestantest1.model.SmsRecoveryBody;

import com.example.koohestantest1.classDirectory.CheckVerification;
import com.example.koohestantest1.classDirectory.GetResualt;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthorizationAPI {

    @POST("SmssVerification/ReCoveryPass")
    Call<GetResualt> getSmsRecovery(@Body SmsRecoveryBody smsRecoveryBody);

    @POST("SmssVerification/SetPasswordRecovery")
    Call<GetResualt> setUpNewPass(@Body NewPasswordBody newPasswordBody);

    @POST("SmssVerification/GetVerfyCode")
    Call<CheckVerification> checkCode(@Body BodyVerifiedCode bodyVerifiedCode);
}
