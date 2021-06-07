package com.example.koohestantest1.ApiDirectory;

import com.example.koohestantest1.model.ProfileModel;

import io.reactivex.Single;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProfileService {
    @POST("Company/getGlobalProfileItem")
    Single<ProfileModel> getProfileData(@Query("CompanyId") String companyId,@Query("UserId") String userId);
}
