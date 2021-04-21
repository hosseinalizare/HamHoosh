package com.example.koohestantest1.model.network;

import com.example.koohestantest1.model.EditBodyRequest;

import java.util.List;

import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.SendProductClass;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProductAPI {
    @POST("Products/ChangeproductFieldItem")
    Call<GetResualt> callForEditingProduct(@Body EditBodyRequest bodyRequest);

    @POST("Products/LoadUnVisibleProduct")
    Call<List<SendProductClass>> callForInvisibleProducts(@Query("CompanyID") String companyId, @Query("UserID") String userId, @Query("Token") String token);

    @POST("Products/LoadNonexistentProduct")
    Call<List<SendProductClass>> callForNotInStock(@Query("CompanyID") String companyId, @Query("UserID") String userId, @Query("Token") String token);
}
