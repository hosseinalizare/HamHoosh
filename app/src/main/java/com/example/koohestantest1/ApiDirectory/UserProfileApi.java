package com.example.koohestantest1.ApiDirectory;

import java.util.List;

import com.example.koohestantest1.ViewModels.CompanyProfileFieldViewModel;
import com.example.koohestantest1.classDirectory.SendCheckAccess;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.UserProfile;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface UserProfileApi {
    public
    @POST("User/GetMyProfile")
    Call<UserProfile> GetMyProfile(@Body SendCheckAccess sendCheckAccess );
    void onResponseGetMyProfile(UserProfile userProfile);

    @POST("User/SetMyProfileData")
    Call<GetResualt> SetMyProfileData(@Body UserProfile userProfile );
    void onResponseSetMyProfileData(GetResualt getResualt);

    @POST("User/UpdateProfileField")
    Call<GetResualt> updateUserProfileField(@Body CompanyProfileFieldViewModel userProfileFieldViewModel);
    void onResponseUpdateUserProfile(List<GetResualt> getResualts);

}
