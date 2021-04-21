package com.example.koohestantest1.model.repository;

import android.util.Log;

import com.example.koohestantest1.model.network.EventsAPI;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.example.koohestantest1.model.repository.callback.EventsBookmarkedCallBacks;
import com.example.koohestantest1.model.repository.callback.EventsLikedCallBacks;

import java.util.List;

import com.example.koohestantest1.classDirectory.SendProductClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EventsRepository {
    private String TAG = EventsRepository.class.getSimpleName();
    private EventsAPI eventsAPI;
    private EventsLikedCallBacks eventsLikedCallBacks;
    private EventsBookmarkedCallBacks eventsBookmarkedCallBacks;

    public EventsRepository(EventsLikedCallBacks eventsLikedCallBacks, EventsBookmarkedCallBacks eventsBookmarkedCallBacks) {
        this.eventsBookmarkedCallBacks = eventsBookmarkedCallBacks;
        this.eventsLikedCallBacks = eventsLikedCallBacks;

        Retrofit retrofitInstance = RetrofitInstance.getRetrofit();
        eventsAPI = retrofitInstance.create(EventsAPI.class);
    }

    public void callForLikedProducts(String companyId, String userId) {
        Call<List<SendProductClass>> call = eventsAPI.getLikedProducts(companyId, userId);
        call.enqueue(new Callback<List<SendProductClass>>() {
            @Override
            public void onResponse(Call<List<SendProductClass>> call, Response<List<SendProductClass>> response) {
                if (response.isSuccessful())
                    eventsLikedCallBacks.onSuccessLiked(response.body());
                Log.d(TAG, "onResponse: " + response.isSuccessful() + " - " + response.body().size());
            }

            @Override
            public void onFailure(Call<List<SendProductClass>> call, Throwable t) {
                eventsLikedCallBacks.onErrorLiked(t.getMessage());

                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    public void callForBookmarkedProducts(String companyId, String userId) {

        Call<List<SendProductClass>> call = eventsAPI.getSavedProducts(companyId, userId);
        call.enqueue(new Callback<List<SendProductClass>>() {
            @Override
            public void onResponse(Call<List<SendProductClass>> call, Response<List<SendProductClass>> response) {
                if (response.isSuccessful())
                    eventsBookmarkedCallBacks.onSuccessBookmarked(response.body());
                Log.d(TAG, "onResponse: " + response.isSuccessful() + " - " + response.body().size());


            }

            @Override
            public void onFailure(Call<List<SendProductClass>> call, Throwable t) {
                eventsBookmarkedCallBacks.onErrorBookmarked(t.getMessage());
                Log.d(TAG, "onFailure: " + t.getMessage());

            }
        });
    }
}

