package com.example.koohestantest1.ApiDirectory;

import java.util.List;

import com.example.koohestantest1.classDirectory.SendHashtagClass;
import com.example.koohestantest1.classDirectory.ReceiveProductClass;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ExplorerApi {

    @POST("Products/Sreach")
    Call<List<ReceiveProductClass>> loadAllProduct(@Query("Name")String name, @Query("OrderType")String orderType);

    @POST("Products/SreachHashtak")
    Call<List<ReceiveProductClass>> searchAllTag(@Body SendHashtagClass sendHashtagClass);

    @POST("Products/newSreachHashtak")
    Call<List<ReceiveProductClass>> searchAllTagNewVersion(@Body SendHashtagClass sendHashtagClass);

    @POST("Products/LimitedSreach")
    Call<List<ReceiveProductClass>> loadProduct(@Query("Name")String name, @Query("OrderType")String orderType, @Query("CompanyID") String companyID);

    @POST("Products/LimitedSreachHashtak")
    Call<List<ReceiveProductClass>> searchTag(@Query("Name")String name, @Query("OrderType")String orderType, @Query("CompanyID") String companyID);
}
