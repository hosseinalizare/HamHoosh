package com.example.koohestantest1.model.network;

import com.example.koohestantest1.Utils.SharedPreferenceUtils;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import com.example.koohestantest1.classDirectory.BaseCodeClass;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.JavaNetCookieJar;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://serverv2.nokhbgan.ir/api/";
    private static final String BASE_URL2 = "https://dehkade.nokhbgan.ir/api/";

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (RetrofitInstance.class) {
                if (retrofit == null) {

                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

    public static Retrofit getRetrofitNew1() {
        if (retrofit == null) {
            synchronized (RetrofitInstance.class) {
                if (retrofit == null) {

                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.level(HttpLoggingInterceptor.Level.BODY);

/*
                    CookieHandler cookieHandler = new CookieManager();
*/
                    CookieManager cookieHandler = new CookieManager();
                    cookieHandler.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
                    CookieHandler.setDefault(cookieHandler);
                    OkHttpClient client = new OkHttpClient.Builder()
                            .addNetworkInterceptor(chain -> {
                                Request original = chain.request();

                                // Customize the request
                                Request request = original.newBuilder()
                                        .header("Accept", "application/json")
                                        .header("sessionId", "sossionId")
                                        .method(original.method(), original.body())
                                        .build();


                                Response response = chain.proceed(request);

                                return response;
                            })
                            .cookieJar(new JavaNetCookieJar(cookieHandler))
                            .connectTimeout(1, TimeUnit.MINUTES)
                            .writeTimeout(15, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .build();


                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .client(client)
                            .build();

                }
            }
        }
        return retrofit;
    }

    public static Retrofit getRetrofitNew2() {
        if (retrofit == null) {

            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {

                            Request oldRequest = chain.request();
                            Request.Builder newRequestBuilder = oldRequest.newBuilder();
                            if (SharedPreferenceUtils.getUserId(BaseCodeClass.context) != null) {
                                newRequestBuilder.addHeader("sessionId", SharedPreferenceUtils.getUserId(BaseCodeClass.context));
                            }
                            newRequestBuilder.method(oldRequest.method(), oldRequest.body());
                            return chain.proceed(newRequestBuilder.build());
                        }
                    }).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }







}
