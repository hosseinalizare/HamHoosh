package com.example.koohestantest1.model.network;

import com.example.koohestantest1.model.Count;
import com.example.koohestantest1.model.CounterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CounterAPI {
    @POST("User/GetCounterParameter")
    Call<Count> getCounts(@Body CounterRequest counterRequest);
}
