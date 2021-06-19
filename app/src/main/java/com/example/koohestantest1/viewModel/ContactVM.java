package com.example.koohestantest1.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koohestantest1.ApiDirectory.MessageApi;
import com.example.koohestantest1.ViewModels.ContactListViewModel;
import com.example.koohestantest1.model.network.RetrofitInstance;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Query;

public class ContactVM extends ViewModel {
    private MutableLiveData<List<ContactListViewModel>> contactLiveData;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MutableLiveData<List<ContactListViewModel>> getContact(String token,String userID,String objectID){
        if (contactLiveData ==null){
            contactLiveData = new MutableLiveData<>();
            Single<List<ContactListViewModel>> listSingle = RetrofitInstance.getRetrofit().create(MessageApi.class).getcontacts(token,userID,objectID);
            compositeDisposable.add(listSingle.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<ContactListViewModel>>() {
                        @Override
                        public void onSuccess(@NotNull List<ContactListViewModel> contactListViewModels) {
                            contactLiveData.setValue(contactListViewModels);
                        }

                        @Override
                        public void onError(@NotNull Throwable e) {
                            Log.i("LOG","ContactVM>>> "+e.getMessage());

                        }
                    })
            );
        }
        return contactLiveData;
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
