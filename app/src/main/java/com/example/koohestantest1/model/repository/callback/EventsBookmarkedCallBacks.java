package com.example.koohestantest1.model.repository.callback;

import java.util.List;

import com.example.koohestantest1.classDirectory.SendProductClass;

public interface EventsBookmarkedCallBacks {
    void onSuccessBookmarked(List<SendProductClass> res);

    void onErrorBookmarked(String err);
}
