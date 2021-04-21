package com.example.koohestantest1.InfoDirectory;

import java.util.List;

import com.example.koohestantest1.ApiDirectory.AddressApi;
import com.example.koohestantest1.classDirectory.AddressViewModel;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.GetResualt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MangeAddressClass {

    final Retrofit retrofit;
    BaseCodeClass baseCodeClass;

    AddressApi addressApi;
    AddressApi callBackAddress;

    public MangeAddressClass(AddressApi apiAddress) {

        baseCodeClass = new BaseCodeClass();


        retrofit = new Retrofit.Builder()
                .baseUrl(baseCodeClass.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();



        addressApi = retrofit.create(AddressApi.class);

        callBackAddress = apiAddress;
    }

    public void getMyAddress(String objectID){

        Call<List<AddressViewModel>> call = addressApi.getAddress(new AddressViewModel(baseCodeClass.getUserID(),
                baseCodeClass.getToken(), objectID, null));
        call.enqueue(new Callback<List<AddressViewModel>>() {
            @Override
            public void onResponse(Call<List<AddressViewModel>> call, Response<List<AddressViewModel>> response) {
                callBackAddress.onResponseGetMyAddress(response.body());
            }

            @Override
            public void onFailure(Call<List<AddressViewModel>> call, Throwable t) {

            }
        });
    }

    public void getAddressByID(String addressID){
        Call<AddressViewModel> call = addressApi.getAddress(addressID);
        call.enqueue(new Callback<AddressViewModel>() {
            @Override
            public void onResponse(Call<AddressViewModel> call, Response<AddressViewModel> response) {
                callBackAddress.onResponseGetAddress(response.body());
            }

            @Override
            public void onFailure(Call<AddressViewModel> call, Throwable t) {

            }
        });
    }

    public void setMyAddress(AddressViewModel myAddress){
        Call<GetResualt> call = addressApi.setAddress(myAddress);
        call.enqueue(new Callback<GetResualt>() {
            @Override
            public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                callBackAddress.onResponseSetAddress(response.body());
            }

            @Override
            public void onFailure(Call<GetResualt> call, Throwable t) {

            }
        });
    }

    public void DelMyAddress(String addressID){
        Call<GetResualt> call = addressApi.delAddress(new AddressViewModel(baseCodeClass.getUserID(),
                baseCodeClass.getToken(), null, addressID));
        call.enqueue(new Callback<GetResualt>() {
            @Override
            public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                callBackAddress.onResponseDelAddress(response.body());
            }

            @Override
            public void onFailure(Call<GetResualt> call, Throwable t) {

            }
        });
    }

    public void DelMyAddress(AddressViewModel address){
        DelMyAddress(address.getId());
    }
}
