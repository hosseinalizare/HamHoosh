package com.example.koohestantest1.model.repository;

import com.example.koohestantest1.model.network.RetrofitInstance;
import com.example.koohestantest1.model.repository.callback.UpdateOrderCallBack;

import com.example.koohestantest1.ApiDirectory.CartApi;
import com.example.koohestantest1.ViewModels.UpdateOrderClass;
import com.example.koohestantest1.classDirectory.GetResualt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdateOrderRepository {
    private Retrofit retrofit;
    private CartApi cartApi;
    private UpdateOrderCallBack updateOrderCallBack;

    public UpdateOrderRepository(UpdateOrderCallBack updateOrderCallBack) {
        this.updateOrderCallBack = updateOrderCallBack;
        retrofit = RetrofitInstance.getRetrofit();
        cartApi = retrofit.create(CartApi.class);
    }

    public void callForUpdating(UpdateOrderClass updateOrderClass) {
        cartApi.updateOrder(updateOrderClass).enqueue(new Callback<GetResualt>() {
            @Override
            public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                if (response.isSuccessful())
                    updateOrderCallBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<GetResualt> call, Throwable t) {
                updateOrderCallBack.onError(t.getMessage());
            }
        });
    }


}
