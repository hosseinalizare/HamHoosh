package com.example.koohestantest1.ApiDirectory;

import com.example.koohestantest1.classDirectory.GetCheckAccess;
import com.example.koohestantest1.classDirectory.SendRecoveryPass;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RecoveryPassApi {

    @POST("SmssVerification/ReCoveryPass")
    Call<GetCheckAccess> getCheckAccess(@Body SendRecoveryPass sendRecoveryPass);
    void onResponseCheckRecovery(GetCheckAccess getCheckAccess);
}
