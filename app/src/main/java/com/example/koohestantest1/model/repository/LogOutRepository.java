package com.example.koohestantest1.model.repository;

import android.content.Context;

import com.example.koohestantest1.model.repository.callback.LogOutCallBacks;



import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LogOutRepository {

    private LogOutCallBacks callBacks;
    public LogOutRepository(Context context , LogOutCallBacks logOutCallBacks) {

        callBacks = logOutCallBacks;
    }

    public void deleteAllLocalData() {

        Observable observable = Observable.create(emitter -> {
            try {
                //dataBase.deleteAllData();
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io());
        Observer observer = new Observer() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Object o) {

            }

            @Override
            public void onError(@NonNull Throwable e) {
                callBacks.onError(e.getMessage());
            }

            @Override
            public void onComplete() {
                callBacks.onSuccess();
            }
        };

        observable.subscribe(observer);
    }

}
