package com.example.koohestantest1.ApiDirectory;

import java.util.List;

import com.example.koohestantest1.ViewModels.ContactListViewModel;
import com.example.koohestantest1.ViewModels.SendMessageViewModel;
import com.example.koohestantest1.ViewModels.SendReportViewModel;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.SendOrderClass;
import com.example.koohestantest1.model.DeleteMessageM;
import com.example.koohestantest1.model.ForwardMsgM;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface MessageApi {

    @POST("User/SendMessage")
    Call<GetResualt> sendMessage(@Body SendMessageViewModel sendMessage);
    void onResponseSendMessage(GetResualt getResualt);


    @POST("User/SendMessage")
    Single<GetResualt> sendAMessage(@Body SendMessageViewModel sendMessage);


    @POST("User/SendForwardedMessage")
    Single<GetResualt> forwardMessage(@Body ForwardMsgM forwardMsgM);


    @Multipart
    @POST("User/ChatFilesPost")
    Single<GetResualt> uploadMessageImage(@Query("MsgId") int MsgId, @Part MultipartBody.Part file);

    @Multipart
    @POST("User/thumbnail")
    Single<GetResualt> sendThumbnail(@Query("MsgId") int MsgId, @Part MultipartBody.Part file);

    @POST("User/Deletemessage")
    Single<GetResualt> deleteMessage(@Body DeleteMessageM deleteMessageM);



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

    @POST("User/GetNewContactList")
    Single<List<ContactListViewModel>> getcontacts(@Query("Token") String token, @Query("UserID") String userID, @Query("ObjectID") String objectID);




    @POST("Order/downloadOrder")
    Single<SendOrderClass> getOrderData(@Query("OrderID") String orderId);

    @POST("Order/downloadOrder")
    Call<SendOrderClass> getOrderData2(@Query("OrderID") String orderId);
}
