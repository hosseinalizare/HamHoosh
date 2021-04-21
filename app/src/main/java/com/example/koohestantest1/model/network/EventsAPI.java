package com.example.koohestantest1.model.network;

import java.util.List;

import com.example.koohestantest1.classDirectory.SendProductClass;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EventsAPI {
    @POST("Products/LoadLikedProduct")
    Call<List<SendProductClass>> getLikedProducts(@Query("CompanyID") String companyId , @Query("UserID") String userId);

    @POST("Products/LoadSavedProduct")
    Call<List<SendProductClass>> getSavedProducts(@Query("CompanyID") String companyId , @Query("UserID") String userId);
}
