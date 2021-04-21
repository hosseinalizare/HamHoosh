package com.example.koohestantest1.model.network;

import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.UserProfile;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UserProfileAPI {
    @POST("User/GetGlobalUserData")
    Call<UserProfile> callForUserById(@Query("UserID") String userId, @Query("Token") String token, @Query("RequUser") String requestedId);

    @Multipart
    @POST("User/Post")
    Call<GetResualt> uploadUserProfile(@Query("UserID") String userId, @Query("Token") String token , @Part MultipartBody.Part file);
}
