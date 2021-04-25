package com.example.koohestantest1;

import com.example.koohestantest1.model.network.RetrofitInstance;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApiClient {
    private static Retrofit retrofit;
    private static final String BASE_URL2 = "https://dehkade.nokhbgan.ir/api/";
    public static Retrofit getRetrofitTest() {
        if (retrofit == null) {
            synchronized (MyApiClient.class) {
                if (retrofit == null) {

                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL2)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

}
