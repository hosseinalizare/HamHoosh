package com.example.koohestantest1.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koohestantest1.ApiDirectory.MessageApi;
import com.example.koohestantest1.MyApiClient;
import com.example.koohestantest1.ViewModels.SendMessageViewModel;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.model.network.RetrofitInstance;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.Part;

public class SendMessageVM extends ViewModel {
    private MutableLiveData<GetResualt> messageLiveData;
    private MutableLiveData<GetResualt> uploadImageMessageLiveData;
  public   CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LiveData<GetResualt> sendMessage(SendMessageViewModel sendMessageViewModel){
        messageLiveData = new MutableLiveData<>();
       Single<GetResualt> resualtSingle = MyApiClient.getRetrofitTest().create(MessageApi.class).sendAMessage(sendMessageViewModel);
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

    public LiveData<GetResualt> sendImageMessage(int msgId, MultipartBody.Part body){
        uploadImageMessageLiveData = new MutableLiveData<>();
        Single<GetResualt> resualtSingle = MyApiClient.getRetrofitTest().create(MessageApi.class).uploadMessageImage(msgId,body);
        compositeDisposable.add(resualtSingle.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<GetResualt>(){

                    @Override
                    public void onSuccess(@NonNull GetResualt getResualt) {
                        uploadImageMessageLiveData.setValue(getResualt);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                })
        );
        return uploadImageMessageLiveData;
    }



    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
