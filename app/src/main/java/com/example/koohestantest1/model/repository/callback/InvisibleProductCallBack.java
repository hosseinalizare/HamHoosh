package com.example.koohestantest1.model.repository.callback;

import java.util.List;

import com.example.koohestantest1.classDirectory.ReceiveProductClass;
import com.example.koohestantest1.local_db.entity.Product;

public interface InvisibleProductCallBack {
    void onSuccessInvisible(List<ReceiveProductClass> results);
    void onErrorInvisible(String error);
}
