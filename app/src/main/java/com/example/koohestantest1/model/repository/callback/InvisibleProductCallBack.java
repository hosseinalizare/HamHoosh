package com.example.koohestantest1.model.repository.callback;

import java.util.List;

import com.example.koohestantest1.classDirectory.SendProductClass;

public interface InvisibleProductCallBack {
    void onSuccessInvisible(List<SendProductClass> results);
    void onErrorInvisible(String error);
}
