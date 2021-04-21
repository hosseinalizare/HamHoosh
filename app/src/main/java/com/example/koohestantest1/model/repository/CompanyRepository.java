package com.example.koohestantest1.model.repository;

import android.util.Log;

import com.example.koohestantest1.model.BodySettingRequest;
import com.example.koohestantest1.model.CompanySetting;
import com.example.koohestantest1.model.EmployeeAdding;
import com.example.koohestantest1.model.EmployeeDeleting;
import com.example.koohestantest1.model.SettingField;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.example.koohestantest1.model.repository.callback.CompanyCallBacks;
import com.example.koohestantest1.model.repository.callback.CompanyCustomersCallBack;
import com.example.koohestantest1.model.repository.callback.CompanySettingCallBacks;
import com.example.koohestantest1.model.repository.callback.DeletingEmployeeCallBacks;
import com.example.koohestantest1.model.repository.callback.EditSettingCallBack;

import java.util.List;

import com.example.koohestantest1.ApiDirectory.CompanyApi;
import com.example.koohestantest1.ViewModels.CompanyFollowerViewModel;
import com.example.koohestantest1.classDirectory.GetResualt;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CompanyRepository {
    private Retrofit retrofit;
    private CompanyApi companyAPI;
    private CompanyCustomersCallBack customersCallBack;
    private DeletingEmployeeCallBacks deletingEmployeeCallBacks;
    private CompanyCallBacks companyCallBacks;
    private String TAG = CompanyRepository.class.getSimpleName();
    private CompanySettingCallBacks companySettingCallBacks;

    public CompanyRepository(CompanyCallBacks companyCallBacks, CompanyCustomersCallBack companyCustomersCallBack,
                             DeletingEmployeeCallBacks deletingEmployeeCallBacks, CompanySettingCallBacks companySettingCallBacks) {
        this.companyCallBacks = companyCallBacks;
        this.deletingEmployeeCallBacks = deletingEmployeeCallBacks;
        this.customersCallBack = companyCustomersCallBack;
        retrofit = RetrofitInstance.getRetrofit();
        companyAPI = retrofit.create(CompanyApi.class);
        this.companySettingCallBacks = companySettingCallBacks;
    }

    public void deleteEmployee(EmployeeDeleting employeeDeleting) {
        companyAPI.deleteEmployee(employeeDeleting).enqueue(new Callback<GetResualt>() {
            @Override
            public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                deletingEmployeeCallBacks.onSuccessDeleting(response.body());
                Log.d(TAG, "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<GetResualt> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                deletingEmployeeCallBacks.onErrorDeleting(t.getMessage());
            }
        });
    }

    public void callCompanyCustomers(String companyId) {
        companyAPI.getCompanyFollower(companyId).enqueue(new Callback<List<CompanyFollowerViewModel>>() {
            @Override
            public void onResponse(Call<List<CompanyFollowerViewModel>> call, Response<List<CompanyFollowerViewModel>> response) {
                if (response.isSuccessful())
                    customersCallBack.onCustomersSuccess(response.body());
                Log.d(TAG, "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<List<CompanyFollowerViewModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                customersCallBack.onCustomersError(t.getMessage());
            }
        });
    }

    public void callForAddingEmployeeToCo(EmployeeAdding employeeAdding) {
        Call<GetResualt> employeeAddingCall = companyAPI.callForAddingEmployee(employeeAdding);
        employeeAddingCall.enqueue(new Callback<GetResualt>() {
            @Override
            public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                if (response.isSuccessful())
                    companyCallBacks.onSuccess(response.body());
                Log.d(TAG, "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<GetResualt> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                companyCallBacks.onError(t.getMessage());
            }
        });
    }

    public void callForGettingCompanySettings(BodySettingRequest bodySettingRequest) {
        companyAPI.getCompanySettings(bodySettingRequest).enqueue(new Callback<List<CompanySetting>>() {
            @Override
            public void onResponse(Call<List<CompanySetting>> call, Response<List<CompanySetting>> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful())
                    companySettingCallBacks.onSuccessSettings(response.body());
            }

            @Override
            public void onFailure(Call<List<CompanySetting>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void setSettingField(SettingField settingField, EditSettingCallBack editSettingCallBack) {
        companyAPI.setSettingField(settingField).enqueue(new Callback<GetResualt>() {
            @Override
            public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful())
                    editSettingCallBack.onEditSuccessful(response.body());
            }

            @Override
            public void onFailure(Call<GetResualt> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public Flowable<GetResualt> uploadCompanyImage(String companyId, String userId, String token, MultipartBody.Part part) {
        return companyAPI.uploadCompanyPhoto(companyId, userId, token, part)
                .onErrorReturn(throwable -> new GetResualt("-1" , throwable.getMessage()))
                .subscribeOn(Schedulers.io());
    }
}
