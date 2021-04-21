package com.example.koohestantest1.model.repository.callback;

import com.example.koohestantest1.classDirectory.UserProfile;

public interface UserProfileCallBack {
    void onSuccess(UserProfile userProfile);
    void onFailure(String error);
}
