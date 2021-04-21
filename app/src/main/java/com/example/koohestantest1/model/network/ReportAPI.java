package com.example.koohestantest1.model.network;

import com.example.koohestantest1.model.Report;

import com.example.koohestantest1.classDirectory.GetResualt;
import io.reactivex.Flowable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReportAPI {
    @POST("User/ReportMessage")
    Single<GetResualt> sendReport(@Body Report report);
}
