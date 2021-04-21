package com.example.koohestantest1.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koohestantest1.model.GetComments;
import com.example.koohestantest1.model.PublishComment;
import com.example.koohestantest1.model.SaveCommentBody;
import com.example.koohestantest1.model.network.RetrofitInstance;

import java.util.List;

import com.example.koohestantest1.ApiDirectory.CommentApi;
import com.example.koohestantest1.classDirectory.GetResualt;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class CommentVM extends ViewModel {
    private MutableLiveData<GetResualt> saveCommentLiveData;
    private MutableLiveData<List<GetComments>> getCommentLiveData;
    private MutableLiveData<GetResualt> deleteCommentLiveData;
    private MutableLiveData<GetResualt> publishCommentLiveData;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MutableLiveData<GetResualt> saveComment(SaveCommentBody saveCommentBody){
        saveCommentLiveData = new MutableLiveData<>();
      Single<GetResualt> getResualtSingle = RetrofitInstance.getRetrofit().create(CommentApi.class).saveComment(saveCommentBody);
      compositeDisposable.add(getResualtSingle.subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribeWith(new DisposableSingleObserver<GetResualt>() {
                  @Override
                  public void onSuccess(@NonNull GetResualt getResualt) {
                      saveCommentLiveData.setValue(getResualt);
                  }

                  @Override
                  public void onError(@NonNull Throwable e) {
                      Log.d("ErrorSaveCommnt",e.getMessage());

                  }
              })
      );
      return saveCommentLiveData;


    }

    public MutableLiveData<List<GetComments>> getComments(String objectId,String uid,String cid){
        getCommentLiveData = new MutableLiveData<>();
        Single<List<GetComments>> comments = RetrofitInstance.getRetrofit().create(CommentApi.class).getComments(objectId,uid,cid);
        compositeDisposable.add(comments.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<GetComments>>() {
                    @Override
                    public void onSuccess(@NonNull List<GetComments> getComments) {
                        getCommentLiveData.setValue(getComments);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("ErrorGetCommnt",e.getMessage());

                    }
                })
        );
        return getCommentLiveData;

    }

    public MutableLiveData<GetResualt> deleteComment(SaveCommentBody saveCommentBody){
        deleteCommentLiveData = new MutableLiveData<>();
        Single<GetResualt> resualtSingle = RetrofitInstance.getRetrofit().create(CommentApi.class).deleteComment(saveCommentBody);
        compositeDisposable.add(resualtSingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<GetResualt>() {
                    @Override
                    public void onSuccess(@NonNull GetResualt getResualt) {
                        deleteCommentLiveData.setValue(getResualt);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.d("ErrorDeleteCommnt",e.getMessage());


                    }
                })
        );
        return deleteCommentLiveData;
    }

    public MutableLiveData<GetResualt> publishComment(PublishComment publishComment){
        publishCommentLiveData = new MutableLiveData<>();
        Single<GetResualt> publishCommentSingle = RetrofitInstance.getRetrofit().create(CommentApi.class).publishComment(publishComment);
        compositeDisposable.add(publishCommentSingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<GetResualt>() {
                    @Override
                    public void onSuccess(@NonNull GetResualt getResualt) {
                        publishCommentLiveData.setValue(getResualt);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i("LOG","Error : PublishComment");

                    }
                })
        );
        return publishCommentLiveData;

    }



    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
