package com.example.koohestantest1.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koohestantest1.ApiDirectory.MessageApi;
import com.example.koohestantest1.ViewModels.SendMessageViewModel;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.model.network.RetrofitInstance;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class SendMessageVM extends ViewModel {
    private MutableLiveData<GetResualt> messageLiveData;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LiveData<GetResualt> sendMessage(SendMessageViewModel sendMessageViewModel){
        messageLiveData = new MutableLiveData<>();
       Single<GetResualt> resualtSingle = RetrofitInstance.getRetrofit().create(MessageApi.class).sendAMessage(sendMessageViewModel);
       compositeDisposable.add(resualtSingle.subscribeOn(Schedulers.newThread())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribeWith(new DisposableSingleObserver<GetResualt>() {
                   @Override
                   public void onSuccess(@NonNull GetResualt getResualt) {
                       messageLiveData.setValue(getResualt);
                   }

                   @Override
                   public void onError(@NonNull Throwable e) {

                   }
               })

       );

       return messageLiveData;
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
