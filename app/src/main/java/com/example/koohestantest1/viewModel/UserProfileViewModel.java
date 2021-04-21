package com.example.koohestantest1.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koohestantest1.model.repository.callback.UserProfileCallBack;
import com.example.koohestantest1.model.repository.UserProfileRepository;

import com.example.koohestantest1.classDirectory.UserProfile;

public class UserProfileViewModel extends ViewModel implements UserProfileCallBack {
    private UserProfileRepository repository;
    private MutableLiveData<UserProfile> liveDataUserProfile = new MutableLiveData<>();
    private MutableLiveData<String> liveDataError = new MutableLiveData<>();

    public UserProfileViewModel() {
        repository = new UserProfileRepository(this);
    }

    public void callForUserProfile(String userId, String token, String reqId) {
        repository.callForUserData(userId, token, reqId);
    }

    public LiveData<UserProfile> getUserProfileLiveData() {
        return liveDataUserProfile;
    }

    public LiveData<String> getErrorLiveData() {
        return liveDataError;
    }

    @Override
    public void onSuccess(UserProfile userProfile) {
        liveDataUserProfile.setValue(userProfile);
    }

    @Override
    public void onFailure(String error) {
        liveDataError.setValue(error);
    }
}
