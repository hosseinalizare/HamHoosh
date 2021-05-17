package com.example.koohestantest1.InfoDirectory;

import android.content.Context;

import java.util.List;

import com.example.koohestantest1.ApiDirectory.MessageApi;
import com.example.koohestantest1.ViewModels.ContactListViewModel;
import com.example.koohestantest1.ViewModels.SendMessageViewModel;
import com.example.koohestantest1.ViewModels.SendReportViewModel;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.model.network.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MessageManagerClass {

    Context mContext;
    final Retrofit retrofit;
    BaseCodeClass baseCodeClass= new BaseCodeClass();
    MessageApi messageApi;
    MessageApi callBackMessage;

    public MessageManagerClass(Context context, MessageApi callBack){
        mContext = context;

/*
        retrofit = MyApiClient.getRetrofitTest();
*/
        retrofit = RetrofitInstance.getRetrofit();

        /*retrofit = RetrofitInstance.getRetrofit();*/
                /*new Retrofit.Builder()
                .baseUrl(baseCodeClass.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();*/

        callBackMessage = callBack;
    }

    public void sendMessage(SendMessageViewModel sendMessageViewModel){
        messageApi = retrofit.create(MessageApi.class);

        Call<GetResualt> call = messageApi.sendMessage(sendMessageViewModel);
        call.enqueue(new Callback<GetResualt>() {
            @Override
            public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                callBackMessage.onResponseSendMessage(response.body());
//                baseCodeClass.logMessage("sendMessage >> " + response.body().getMsg() + " >> " + response.body().getResualt() , mContext);
            }

            @Override
            public void onFailure(Call<GetResualt> call, Throwable t) {
                baseCodeClass.logMessage("MessageClass onFailure > " + t.getMessage(), mContext);
            }
        });
    }

    public void getMessage(String senderUser, String getterUser){
        messageApi = retrofit.create(MessageApi.class);

        Call<List<SendMessageViewModel>> call = messageApi.getMessage(new SendMessageViewModel(baseCodeClass.getToken(),baseCodeClass.getUserID(),"0", senderUser,
                getterUser, "", "9/4/2019 9:18:45 PM", "", "",1,"",1,100));
        call.enqueue(new Callback<List<SendMessageViewModel>>() {
            @Override
            public void onResponse(Call<List<SendMessageViewModel>> call, Response<List<SendMessageViewModel>> response) {
                callBackMessage.onResponseGetMessage(response.body());
            }

            @Override
            public void onFailure(Call<List<SendMessageViewModel>> call, Throwable t) {
                baseCodeClass.logMessage("onFailur >> " + t.getMessage(), mContext);
            }
        });
    }

    public void sendReport(SendReportViewModel sendReportViewModel){
        messageApi = retrofit.create(MessageApi.class);

        Call<GetResualt> call = messageApi.sendReport(sendReportViewModel);
        call.enqueue(new Callback<GetResualt>() {
            @Override
            public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                callBackMessage.onResponseSendReport(response.body());
            }

            @Override
            public void onFailure(Call<GetResualt> call, Throwable t) {
                baseCodeClass.logMessage("MessageClass onFailure report > " + t.getMessage(), mContext);
            }
        });
    }

    public void getContact(String objectID){
        messageApi = retrofit.create(MessageApi.class);

        Call<List<ContactListViewModel>> call = messageApi.getContactV2(baseCodeClass.getToken(), baseCodeClass.getUserID(), objectID);
        call.enqueue(new Callback<List<ContactListViewModel>>() {
            @Override
            public void onResponse(Call<List<ContactListViewModel>> call, Response<List<ContactListViewModel>> response) {
                callBackMessage.onResponseGetContact(response.body());
            }

            @Override
            public void onFailure(Call<List<ContactListViewModel>> call, Throwable t) {

            }
        });
    }


}
