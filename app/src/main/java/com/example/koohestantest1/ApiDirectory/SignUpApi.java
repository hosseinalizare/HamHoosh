package com.example.koohestantest1.ApiDirectory;

import com.example.koohestantest1.classDirectory.GetSignUp;
import com.example.koohestantest1.classDirectory.SendSignUp;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SignUpApi {
   // public String BASE_URL = "http://dehkade.nokhbgan.ir/api/";

    @POST("Login/SignUP")
    Call<GetSignUp> getSignUp(@Body SendSignUp sendSignUp);
    void onResponseSingUp(GetSignUp getSignUp);

}
