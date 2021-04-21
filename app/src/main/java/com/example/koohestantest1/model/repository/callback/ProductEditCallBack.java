package com.example.koohestantest1.model.repository.callback;

import com.example.koohestantest1.classDirectory.GetResualt;

public interface ProductEditCallBack {
    void onSuccessEdit(GetResualt resualt);

    void onErrorEdit(String error);
}
