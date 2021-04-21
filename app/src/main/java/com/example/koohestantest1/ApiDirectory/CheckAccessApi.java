package com.example.koohestantest1.ApiDirectory;

import com.example.koohestantest1.classDirectory.GetCheckAccess;
import com.example.koohestantest1.classDirectory.SendCheckAccess;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CheckAccessApi {
   // public String BASE_URL = "http://dehkade.nokhbgan.ir/api/";

    @POST("Login/CheckAccess")
    Call<GetCheckAccess> getCheckAccess(@Body SendCheckAccess sendCheckAccess);
    void onResponseCheckAccess(GetCheckAccess getCheckAccess);
}
