package com.example.koohestantest1.ApiDirectory;

import java.util.List;

import com.example.koohestantest1.classDirectory.CheckVerification;
import com.example.koohestantest1.classDirectory.GetLoginDetail;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.GetVerifySmsClass;
import com.example.koohestantest1.classDirectory.HowToPay;
import com.example.koohestantest1.classDirectory.SendLoginDetail;
import com.example.koohestantest1.classDirectory.SendMobileNumberForSmsClass;
import com.example.koohestantest1.classDirectory.SendVerifyCode;
import com.example.koohestantest1.classDirectory.TermsAndConditions;
import com.example.koohestantest1.model.VersioCheck;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JsonApi {

  //  public String BASE_URL = "https://dehkade.nokhbgan.ir/api/";

    @POST("SmssVerification/Verify")
    Call<GetVerifySmsClass> getVerifySms(@Body SendMobileNumberForSmsClass sendMobileNumberForSmsClass);

    @POST("SmssVerification/GetVerfyCode")
    Call<CheckVerification> checkVerification(@Body SendVerifyCode sendVerifyCode);
    void onResponseCheckVerify(CheckVerification checkVerification);

    @POST("Login/Login")
    Call<GetLoginDetail> getLoginDetail(@Body SendLoginDetail sendLoginDetail);
    void onResponseLogin(GetLoginDetail getLoginDetail);

    @POST("Programer/Customer")
    Call<GetResualt> setCostumerID(@Query("company") String company, @Query("User")String user, @Query("customerID") String custumerID);
    void onResponseCostumerID(GetResualt getResualt);

    @POST("Group/AppRules")
    Call<TermsAndConditions> getTerms(@Query("CompanyID") String companyId);

    @POST("Company/CarAndPAyField")
    Call<List<HowToPay>> getPays(@Query("CompanyID") String companyId);

    @POST("User/VersionCheck")
    Call<GetResualt> getLastVersion(@Body VersioCheck versioCheck);
}
