package com.example.koohestantest1.model.repository.callback;

import com.example.koohestantest1.model.Count;

import java.util.List;

public interface CountsCallBack {
    void onSuccess(Count counts);
    void onError(String error);
}
