package com.example.koohestantest1.model.repository.callback;

import java.util.List;

import com.example.koohestantest1.classDirectory.SendProductClass;

public interface EventsLikedCallBacks {
    void onSuccessLiked(List<SendProductClass> res);
    void onErrorLiked(String err);
}
