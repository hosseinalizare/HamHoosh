package com.example.koohestantest1.model.repository.callback;

import java.util.List;

import com.example.koohestantest1.classDirectory.ReceiveProductClass;

public interface NotInStockCallBack {
    void onSuccessStock(List<ReceiveProductClass> res);
    void onErrorStock(String error);
}
