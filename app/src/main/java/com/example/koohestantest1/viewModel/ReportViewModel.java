package com.example.koohestantest1.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.koohestantest1.model.Report;
import com.example.koohestantest1.model.network.ReportAPI;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.example.koohestantest1.model.repository.ReportRepository;

import com.example.koohestantest1.classDirectory.GetResualt;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ReportViewModel extends ViewModel {
   private MutableLiveData<GetResualt> reportLiveData ;
   private CompositeDisposable compositeDisposable = new CompositeDisposable();



    public LiveData<GetResualt> sendReport(Report report){
        reportLiveData = new MutableLiveData<>();
        Single<GetResualt> resualtSingle = RetrofitInstance.getRetrofit().create(ReportAPI.class).sendReport(report);
        compositeDisposable.add(resualtSingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<GetResualt>() {
                    @Override
                    public void onSuccess(@NonNull GetResualt getResualt) {
                        reportLiveData.setValue(getResualt);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i("LOG",e.getMessage());

                    }
                })

        );
        return reportLiveData;

    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }

    /*  private ReportRepository repository;
    private MutableLiveData<Report> reporter = new MutableLiveData<>();

    public ReportViewModel() {
        repository = new ReportRepository();
    }

    public void sendReport(Report report) {
        reporter.setValue(report);
    }

    public LiveData<GetResualt> liveReportRes() {
        return Transformations.switchMap(reporter, report -> LiveDataReactiveStreams.fromPublisher(repository.sendReport(report)));
    }*/
}
