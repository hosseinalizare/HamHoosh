package com.example.koohestantest1.model.repository;

import android.util.Log;

import com.example.koohestantest1.model.EditBodyRequest;
import com.example.koohestantest1.model.network.ProductAPI;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.example.koohestantest1.model.repository.callback.InvisibleProductCallBack;
import com.example.koohestantest1.model.repository.callback.NotInStockCallBack;
import com.example.koohestantest1.model.repository.callback.ProductEditCallBack;

import java.util.List;

import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.ReceiveProductClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductRepository {
    private Retrofit retrofit = RetrofitInstance.getRetrofit();
    private ProductAPI productAPI;
    private ProductEditCallBack callBack;
    private InvisibleProductCallBack invisibleProductCallBack;
    private NotInStockCallBack notInStockCallBack;
    private String TAG = ProductRepository.class.getSimpleName();

    public ProductRepository(ProductEditCallBack callBack, InvisibleProductCallBack invisibleProductCallBack, NotInStockCallBack notInStockCallBack) {
        this.callBack = callBack;
        productAPI = retrofit.create(ProductAPI.class);
        this.invisibleProductCallBack = invisibleProductCallBack;
        this.notInStockCallBack = notInStockCallBack;
    }

    public void callForEditingProduct(EditBodyRequest bodyRequest) {
        productAPI.callForEditingProduct(bodyRequest).enqueue(new Callback<GetResualt>() {
            @Override
            public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                Log.d(TAG, "onResponse: " + response.body().getMsg());
                if (response.isSuccessful()) {
                    callBack.onSuccessEdit(response.body());
                }
            }

            @Override
            public void onFailure(Call<GetResualt> call, Throwable t) {
                callBack.onErrorEdit(t.getMessage());
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void callForInvisibleProducts(String companyId, String userId, String token) {
        productAPI.callForInvisibleProducts(companyId, userId, token).enqueue(new Callback<List<ReceiveProductClass>>() {
            @Override
            public void onResponse(Call<List<ReceiveProductClass>> call, Response<List<ReceiveProductClass>> response) {
                Log.d(TAG, "onResponse: " + response.body().size());
                if (response.isSuccessful())
                    invisibleProductCallBack.onSuccessInvisible(response.body());
            }

            @Override
            public void onFailure(Call<List<ReceiveProductClass>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                invisibleProductCallBack.onErrorInvisible(t.getMessage());
            }
        });
    }

    public void callForNotInStockProducts(String companyId, String userId, String token) {
        productAPI.callForNotInStock(companyId, userId, token).enqueue(new Callback<List<ReceiveProductClass>>() {
            @Override
            public void onResponse(Call<List<ReceiveProductClass>> call, Response<List<ReceiveProductClass>> response) {
                Log.d(TAG, "onResponse: " + response.body().size());
                if (response.isSuccessful())
                    notInStockCallBack.onSuccessStock(response.body());
            }

            @Override
            public void onFailure(Call<List<ReceiveProductClass>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                notInStockCallBack.onErrorStock(t.getMessage());
            }
        });
    }
}
