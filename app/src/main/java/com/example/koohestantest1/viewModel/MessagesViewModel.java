package com.example.koohestantest1.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koohestantest1.model.repository.MessagesRepository;
import com.example.koohestantest1.model.repository.callback.MessagesCallBack;

public class MessagesViewModel extends ViewModel implements MessagesCallBack {
    private final MessagesRepository repository = new MessagesRepository(this);
    private final MutableLiveData<Integer> liveMessageCount = new MutableLiveData<>();
    private final MutableLiveData<String> liveErrorMessageCount = new MutableLiveData<>();

    public void callForMessagesCount(String token, String userId, String objectId) {
        repository.getMessageCount(token, userId, objectId);
    }

    public LiveData<Integer> getLiveMessageCount() {
        return liveMessageCount;
    }

    public LiveData<String> getLiveErrorMessageCount() {
        return liveErrorMessageCount;
    }

    @Override
    public void onSuccess(int count) {
        liveMessageCount.setValue(count);
    }

    @Override
    public void onError(String error) {
        liveErrorMessageCount.setValue(error);
    }
}
