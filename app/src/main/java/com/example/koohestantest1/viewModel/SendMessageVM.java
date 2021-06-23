package com.example.koohestantest1.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koohestantest1.ApiDirectory.MessageApi;
import com.example.koohestantest1.ViewModels.SendMessageViewModel;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.SendOrderClass;
import com.example.koohestantest1.model.DeleteMessageM;
import com.example.koohestantest1.model.ForwardMsgM;
import com.example.koohestantest1.model.network.RetrofitInstance;

import org.jetbrains.annotations.NotNull;

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
    private MutableLiveData<GetResualt> uploadDocMessageLiveData;
    private MutableLiveData<GetResualt> deleteMessageLiveData;
    private MutableLiveData<GetResualt> forwardMessageLiveData;
    private MutableLiveData<SendOrderClass> orderDataLiveData;
  public   CompositeDisposable compositeDisposable = new CompositeDisposable();

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

    public LiveData<GetResualt> sendImageMessage(int msgId, MultipartBody.Part body){
        uploadImageMessageLiveData = new MutableLiveData<>();
        Single<GetResualt> resualtSingle = RetrofitInstance.getRetrofit().create(MessageApi.class).uploadMessageImage(msgId,body);
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
    public LiveData<GetResualt> sendDocMessage(int msgId, MultipartBody.Part body){
        uploadDocMessageLiveData = new MutableLiveData<>();
        Single<GetResualt> resualtSingle = RetrofitInstance.getRetrofit().create(MessageApi.class).uploadMessageImage(msgId,body);
        compositeDisposable.add(resualtSingle.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<GetResualt>(){

                    @Override
                    public void onSuccess(@NonNull GetResualt getResualt) {
                        uploadDocMessageLiveData.setValue(getResualt);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                })
        );
        return uploadDocMessageLiveData;
    }

    public LiveData<GetResualt> deleteMessage(DeleteMessageM deleteMessageM){
        deleteMessageLiveData = new MutableLiveData<>();
        Single<GetResualt> resualtSingle = RetrofitInstance.getRetrofit().create(MessageApi.class).deleteMessage(deleteMessageM);
        compositeDisposable.add(resualtSingle.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<GetResualt>() {
                    @Override
                    public void onSuccess(@NonNull GetResualt getResualt) {
                        deleteMessageLiveData.setValue(getResualt);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                })

        );
        return deleteMessageLiveData;


    }

    public LiveData<SendOrderClass> getOrderData(String orderId){
        orderDataLiveData = new MutableLiveData<>();
        Single<SendOrderClass> orderClassSingle = RetrofitInstance.getRetrofit().create(MessageApi.class).getOrderData(orderId);
        compositeDisposable.add(orderClassSingle.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SendOrderClass>() {
                    @Override
                    public void onSuccess(@NonNull SendOrderClass sendOrderClass) {
                        orderDataLiveData.setValue(sendOrderClass);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                })

        );

        return orderDataLiveData;

    }

    public LiveData<GetResualt> forwardMessage(ForwardMsgM forwardMsgM){
        if (forwardMessageLiveData == null){
            forwardMessageLiveData = new MutableLiveData<>();
            Single<GetResualt> resualtSingle = RetrofitInstance.getRetrofit().create(MessageApi.class).forwardMessage(forwardMsgM);
            compositeDisposable.add(resualtSingle.subscribeOn(Schedulers.newThread())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<GetResualt>() {
                        @Override
                        public void onSuccess(@NotNull GetResualt getResualt) {
                            forwardMessageLiveData.postValue(getResualt);
                        }

                        @Override
                        public void onError(@NotNull Throwable e) {
                            Log.i("LOG","SendMessageVM>>> : forwardMessage>>>>>   "+e.getMessage());
                        }
                    })

            );
        }
        return forwardMessageLiveData;
    }



    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
