package com.example.koohestantest1.ApiDirectory;

import java.util.List;

import com.example.koohestantest1.ViewModels.ContactListViewModel;
import com.example.koohestantest1.ViewModels.SendMessageViewModel;
import com.example.koohestantest1.ViewModels.SendReportViewModel;
import com.example.koohestantest1.classDirectory.GetResualt;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MessageApi {

    @POST("User/SendMessage")
    Call<GetResualt> sendMessage(@Body SendMessageViewModel sendMessage);
    void onResponseSendMessage(GetResualt getResualt);

    @POST("User/ReportMessage")
    Call<GetResualt> sendReport(@Body SendReportViewModel sendReportViewModel);
    void onResponseSendReport(GetResualt getResualt);

    @POST("User/GetMessages")
    Call<List<SendMessageViewModel>> getMessage(@Body SendMessageViewModel sendMessageViewModel);
    void onResponseGetMessage(List<SendMessageViewModel> sendMessageViewModels);

    @POST("User/GetContactList")
    Call<List<ContactListViewModel>> getContact(@Query("Token") String token, @Query("UserID") String userID, @Query("ObjectID") String objectID);
    void onResponseGetContact(List<ContactListViewModel> contactListViewModels);

    @POST("User/GetNewContactList")
    Call<List<ContactListViewModel>> getContactV2(@Query("Token") String token, @Query("UserID") String userID, @Query("ObjectID") String objectID);
    void onResponseGetContactV2(List<ContactListViewModel> contactListViewModels);
}
