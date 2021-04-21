package com.example.koohestantest1.model.repository.callback;

import com.example.koohestantest1.classDirectory.GetResualt;

public interface CompanyCallBacks {
    void onSuccess(GetResualt result);
    void onError(String error);
}
