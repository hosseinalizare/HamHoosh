package com.example.koohestantest1.ApiDirectory;

import android.view.View;

import java.util.List;

import com.example.koohestantest1.classDirectory.AddressViewModel;
import com.example.koohestantest1.classDirectory.GetResualt;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AddressApi {

    ///Hole

    @POST("Address/ListOfStait")
    Call<List<String>> listState();
    void onResponseListState(List<String> stateList);

    @POST("Address/ListCityOfStait")
    Call<List<String>> listCity(@Query("Stait")String state);
    void onResponseListCity(List<String> cityList);

    @POST("User/SaveMyAddress")
    Call<GetResualt> setAddress(@Body AddressViewModel addressViewModel);
    void onResponseSetAddress(GetResualt getResualt);

    @POST("User/GetMyAddress")
    Call<List<AddressViewModel>> getAddress(@Body AddressViewModel addressViewModel);
    void onResponseGetMyAddress(List<AddressViewModel> addressViewModel);

    @POST("User/DeleteMyAddress")
    Call<GetResualt> delAddress(@Body AddressViewModel addressViewModel);
    void onResponseDelAddress(GetResualt getResualt);

    @POST("User/GetAddressByID")
    Call<AddressViewModel> getAddress(@Query("AddressID") String addressID);
    void onResponseGetAddress(AddressViewModel addressViewModel);

    void AddressOnCliclckListener(AddressViewModel address);
    void CartRecyclerViewClickListener(View v, boolean b);
    void CartPaymentClickListener(int id);
}
