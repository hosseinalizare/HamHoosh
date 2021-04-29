package com.example.koohestantest1.model.repository.callback;

import java.util.List;

import com.example.koohestantest1.classDirectory.ReceiveProductClass;

public interface EventsLikedCallBacks {
    void onSuccessLiked(List<ReceiveProductClass> res);
    void onErrorLiked(String err);
}
