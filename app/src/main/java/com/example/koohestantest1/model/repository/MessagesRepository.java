package com.example.koohestantest1.model.repository;

import android.util.Log;

import com.example.koohestantest1.model.network.RetrofitInstance;
import com.example.koohestantest1.model.repository.callback.MessagesCallBack;

import java.util.List;

import com.example.koohestantest1.ApiDirectory.MessageApi;
import com.example.koohestantest1.ViewModels.ContactListViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MessagesRepository {
    private final Retrofit retrofit = RetrofitInstance.getRetrofit();
    private final MessageApi messageApi;
    private final MessagesCallBack messagesCallBack;
    private final String TAG = MessagesRepository.class.getSimpleName();

    public MessagesRepository(MessagesCallBack messagesCallBack) {
        this.messagesCallBack = messagesCallBack;
        messageApi = retrofit.create(MessageApi.class);
    }

    public void getMessageCount(String token, String userId, String objectId) {
        messageApi.getContactV2(token, userId, objectId).enqueue(new Callback<List<ContactListViewModel>>() {
            @Override
            public void onResponse(Call<List<ContactListViewModel>> call, Response<List<ContactListViewModel>> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {

                        int messagesCount = 0;

                        for (ContactListViewModel item :
                                response.body()) {

                            int count = Integer.parseInt(item.getCountNewMsg());

                            if (count > 0) {
                                messagesCount++;
                                Log.d(TAG, "onResponse: " + messagesCount);
                            }
                        }
                        Log.d(TAG, "onResponse: " + messagesCount);
                        messagesCallBack.onSuccess(messagesCount);
                    } else {
                        Log.d(TAG, "onResponse: body is null");
                        messagesCallBack.onError("body is null");
                    }
                }
                Log.d(TAG, "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<List<ContactListViewModel>> call, Throwable t) {
                messagesCallBack.onError(t.getMessage());
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
