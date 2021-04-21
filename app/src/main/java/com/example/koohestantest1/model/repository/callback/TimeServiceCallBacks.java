package com.example.koohestantest1.model.repository.callback;

import com.example.koohestantest1.model.MyTime;

import java.util.Date;

public interface TimeServiceCallBacks {
    void onSuccess(MyTime res);
    void onError(String err);
}
