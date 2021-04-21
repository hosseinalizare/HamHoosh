package com.example.koohestantest1.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koohestantest1.model.repository.callback.EventsBookmarkedCallBacks;
import com.example.koohestantest1.model.repository.callback.EventsLikedCallBacks;
import com.example.koohestantest1.model.repository.EventsRepository;

import java.util.List;

import com.example.koohestantest1.classDirectory.SendProductClass;

public class EventsViewModel extends ViewModel implements EventsLikedCallBacks, EventsBookmarkedCallBacks {
    private EventsRepository eventsRepository;
    private MutableLiveData<List<SendProductClass>> liveDataBookmarks = new MutableLiveData<>();
    private MutableLiveData<List<SendProductClass>> liveDataFavs = new MutableLiveData<>();
    private MutableLiveData<String> liveDataErrBookmarks = new MutableLiveData<>();
    private MutableLiveData<String> liveDataErrFavs = new MutableLiveData<>();

    public EventsViewModel() {
        eventsRepository = new EventsRepository(this, this);
    }

    public void callForBookmarks(String companyId, String userId) {
        eventsRepository.callForBookmarkedProducts(companyId, userId);
    }

    public void callForFavs(String companyId, String userId) {
        eventsRepository.callForLikedProducts(companyId, userId);
    }

    public LiveData<List<SendProductClass>> getLiveBookmarks() {
        return liveDataBookmarks;
    }

    public LiveData<List<SendProductClass>> getLiveFavs() {
        return liveDataFavs;
    }

    public LiveData<String> getErrorBookmark() {
        return liveDataErrBookmarks;
    }

    public LiveData<String> getErrorFav() {
        return liveDataErrFavs;
    }

    @Override
    public void onSuccessBookmarked(List<SendProductClass> res) {
        liveDataBookmarks.setValue(res);
    }

    @Override
    public void onErrorBookmarked(String err) {
        liveDataErrBookmarks.setValue(err);
    }

    @Override
    public void onSuccessLiked(List<SendProductClass> res) {
        liveDataFavs.setValue(res);
    }

    @Override
    public void onErrorLiked(String err) {
        liveDataErrFavs.setValue(err);
    }
}
