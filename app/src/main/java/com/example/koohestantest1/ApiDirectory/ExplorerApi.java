package com.example.koohestantest1.ApiDirectory;

import java.util.List;

import com.example.koohestantest1.classDirectory.SendHashtagClass;
import com.example.koohestantest1.classDirectory.SendProductClass;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ExplorerApi {

    @POST("Products/Sreach")
    Call<List<SendProductClass>> loadAllProduct(@Query("Name")String name, @Query("OrderType")String orderType);

    @POST("Products/SreachHashtak")
    Call<List<SendProductClass>> searchAllTag(@Body SendHashtagClass sendHashtagClass);

    @POST("Products/newSreachHashtak")
    Call<List<SendProductClass>> searchAllTagNewVersion(@Body SendHashtagClass sendHashtagClass);

    @POST("Products/LimitedSreach")
    Call<List<SendProductClass>> loadProduct(@Query("Name")String name, @Query("OrderType")String orderType, @Query("CompanyID") String companyID);

    @POST("Products/LimitedSreachHashtak")
    Call<List<SendProductClass>> searchTag(@Query("Name")String name, @Query("OrderType")String orderType, @Query("CompanyID") String companyID);
}
