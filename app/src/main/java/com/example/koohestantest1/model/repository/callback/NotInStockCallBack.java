package com.example.koohestantest1.model.repository.callback;

import java.util.List;

import com.example.koohestantest1.classDirectory.SendProductClass;

public interface NotInStockCallBack {
    void onSuccessStock(List<SendProductClass> res);
    void onErrorStock(String error);
}
