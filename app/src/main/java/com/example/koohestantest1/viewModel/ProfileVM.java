package com.example.koohestantest1.viewModel;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koohestantest1.ApiDirectory.ProfileService;
import com.example.koohestantest1.model.ProfileModel;
import com.example.koohestantest1.model.network.RetrofitInstance;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ProfileVM extends ViewModel {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    MutableLiveData<ProfileModel> profileData;

    public LiveData<ProfileModel> getProfileData(String companyId,String userId){
        if (profileData ==null){
            profileData = new MutableLiveData<>();
            Single<ProfileModel> single = RetrofitInstance.getRetrofit().create(ProfileService.class).getProfileData(companyId,userId);
            compositeDisposable.add(single.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<ProfileModel>() {
                        @Override
                        public void onSuccess(@NonNull ProfileModel profileModel) {
                            profileData.setValue(profileModel);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.d("LOG","ProfileVM>> "+e.getMessage());
                        }
                    })
            );

        }

        return profileData;
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
