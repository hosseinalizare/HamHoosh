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
import com.example.koohestantest1.local_db.entity.NewsLetter;
import com.example.koohestantest1.model.DeleteNewsLetter;
import com.example.koohestantest1.model.NewsLetterModel;
import com.example.koohestantest1.model.VersioCheck;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    Call<GetResualt> setCostumerID(@Query("company") String company, @Query("User") String user, @Query("customerID") String custumerID);

    void onResponseCostumerID(GetResualt getResualt);

    @POST("Group/AppRules")
    Call<TermsAndConditions> getTerms(@Query("CompanyID") String companyId);

    @POST("Company/CarAndPAyField")
    Call<List<HowToPay>> getPays(@Query("CompanyID") String companyId);

    @POST("User/VersionCheck")
    Call<GetResualt> getLastVersion(@Body VersioCheck versioCheck);

    @POST("News/LoadUpdatedNews")
    Call<List<NewsLetter>> getAllNews(@Query("CompanyId") String companyId,
                                      @Query("Time") long time,
                                      @Query("UserID") String userId);

    @POST("News/SaveNews")
    Call<GetResualt> setNewsLetter(@Body NewsLetterModel newsLetter);

    @Multipart
    @POST("Products/Post")
    Call<GetResualt> uploadNewsLetterImage(@Query("ProductID") String newsLetterId,
                                           @Query("CompanyID") String coId,
                                           @Query("UserID") String uID,
                                           @Query("Token") String token,
                                           @Part List<MultipartBody.Part> files);

    @POST("News/DeleteNews")
    Call<GetResualt> deleteOneNewsLetter(@Body DeleteNewsLetter deleteNewsLetter);
}
