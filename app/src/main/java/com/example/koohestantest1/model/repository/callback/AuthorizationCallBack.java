package com.example.koohestantest1.model.repository.callback;

import com.example.koohestantest1.classDirectory.CheckVerification;
import com.example.koohestantest1.classDirectory.GetResualt;

public interface AuthorizationCallBack {
    void onSuccessSms(GetResualt res);

    void onSuccessPassword(GetResualt res);

    void onSuccessVerifiedCode(CheckVerification res);
}
