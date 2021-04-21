package com.example.koohestantest1.model.repository;

import com.example.koohestantest1.model.Report;
import com.example.koohestantest1.model.network.ReportAPI;
import com.example.koohestantest1.model.network.RetrofitInstance;

import com.example.koohestantest1.classDirectory.GetResualt;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ReportRepository {
    private Retrofit retrofit = RetrofitInstance.getRetrofit();
    private ReportAPI reportAPI;

/*    public ReportRepository() {
        reportAPI = retrofit.create(ReportAPI.class);
    }

    public Flowable<GetResualt> sendReport(Report report) {
        return reportAPI.sendReport(report)
                .onErrorReturn(throwable -> new GetResualt("-1", throwable.getMessage()))
                .subscribeOn(Schedulers.io());
    }*/
}
