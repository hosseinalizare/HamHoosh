package com.example.koohestantest1.model.network;

import com.example.koohestantest1.model.MyTime;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TimeApi {
    @POST("Programer/GetTime")
    Call<MyTime> getCurrentTime(@Query("TimeMode") int timeMode);
}
