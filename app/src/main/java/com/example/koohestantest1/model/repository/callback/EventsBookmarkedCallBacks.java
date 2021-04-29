package com.example.koohestantest1.model.repository.callback;

import java.util.List;

import com.example.koohestantest1.classDirectory.ReceiveProductClass;

public interface EventsBookmarkedCallBacks {
    void onSuccessBookmarked(List<ReceiveProductClass> res);

    void onErrorBookmarked(String err);
}
