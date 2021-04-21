package com.example.koohestantest1.ApiDirectory;

import com.example.koohestantest1.model.BodySettingRequest;
import com.example.koohestantest1.model.CompanySetting;
import com.example.koohestantest1.model.EmployeeAdding;
import com.example.koohestantest1.model.EmployeeDeleting;
import com.example.koohestantest1.model.SettingField;

import java.util.List;

import com.example.koohestantest1.ViewModels.CompanyFollowerViewModel;
import com.example.koohestantest1.ViewModels.CompanyProfileFieldViewModel;
import com.example.koohestantest1.classDirectory.CompanyEmployeesClass;
import com.example.koohestantest1.classDirectory.CompanyProfile;
import com.example.koohestantest1.classDirectory.CompanyRequest;
import com.example.koohestantest1.classDirectory.CreateCompany;
import com.example.koohestantest1.classDirectory.GetResualt;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface CompanyApi {
    @POST("Company/CreateMyCompany")
    Call<GetResualt> CreateMyCompany(@Body CreateCompany createCompany);

    void onResponseCreateMyCompany(GetResualt getResualt);

    @POST("Company/UpdateCompanyProfile")
    Call<GetResualt> UpdateCompanyProfile(@Body CompanyProfile companyProfile);

    void onResponseUpdateCompanyProfile(GetResualt getResualt);

    @POST("Company/ReadCompanyProfile")
    Call<CompanyProfile> ReadCompanyProfile(@Body CompanyRequest companyRequest);

    void onResponseGetCompanyProfile(CompanyProfile companyProfile);

    @POST("Company/Employees")
    Call<List<CompanyEmployeesClass>> getCompanyEmployee(@Query("CompanyID") String companyID);

    void onResponseGetCompanyEmployee(List<CompanyEmployeesClass> employeesClasses);

    @POST("Company/UpdateCompanyProfileField")
    Call<GetResualt> updateCompanyProfileField(@Body CompanyProfileFieldViewModel companyProfileFieldViewModel);

    void onResponseUpdateCompany(List<GetResualt> getResualts);

    @POST("Company/GetFollowing")
    Call<List<CompanyFollowerViewModel>> getCompanyFollower(@Query("CompanyID") String CompanyID);

    void onResponseGetCompanyFollower(List<CompanyFollowerViewModel> companyFollowerViewModel);

    @POST("Company/AddEmployeeToCompany")
    Call<GetResualt> callForAddingEmployee(@Body EmployeeAdding employeeAdding);


    @POST("Company/DeleteEmployee")
    Call<GetResualt> deleteEmployee(@Body EmployeeDeleting employeeDeleting);

    @POST("Company/getSettingFields")
    Call<List<CompanySetting>> getCompanySettings(@Body BodySettingRequest bodySettingRequest);

    @POST("Company/setSettingFields")
    Call<GetResualt> setSettingField(@Body SettingField settingField);

    @Multipart
    @POST("Company/Post")
    Flowable<GetResualt> uploadCompanyPhoto(@Query("CompanyID") String companyId, @Query("UserID") String userId, @Query("Token") String token , @Part MultipartBody.Part file);
}
