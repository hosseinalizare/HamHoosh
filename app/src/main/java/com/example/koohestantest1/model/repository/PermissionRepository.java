package com.example.koohestantest1.model.repository;

import android.util.Log;

import com.example.koohestantest1.model.EditPermission;
import com.example.koohestantest1.model.Permission;
import com.example.koohestantest1.model.network.PermissionAPI;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.example.koohestantest1.model.repository.callback.PermissionCallBack;

import java.util.List;

import com.example.koohestantest1.classDirectory.GetResualt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PermissionRepository {
    private Retrofit retrofit = RetrofitInstance.getRetrofit();
    private PermissionAPI permissionAPI;
    private PermissionCallBack permissionCallBack;
    private String TAG = PermissionRepository.class.getSimpleName();

    public PermissionRepository(PermissionCallBack permissionCallBack) {
        this.permissionCallBack = permissionCallBack;
        permissionAPI = retrofit.create(PermissionAPI.class);
    }

    public void editEmployeePermission(EditPermission editPermission) {
        permissionAPI.editEmployeePermission(editPermission).enqueue(new Callback<GetResualt>() {
            @Override
            public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                permissionCallBack.onSuccessEditPermission(response.body());
                Log.d(TAG, "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<GetResualt> call, Throwable t) {
                permissionCallBack.onErrorEditPermission(t.getMessage());
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void getEmployeePermissions(String employeeId) {
        permissionAPI.getEmployeePermissions(employeeId).enqueue(new Callback<List<Permission>>() {
            @Override
            public void onResponse(Call<List<Permission>> call, Response<List<Permission>> response) {
                permissionCallBack.onSuccessPermissionList(response.body());
                Log.d(TAG, "onResponse: ");
                Log.d(TAG, "onResponse: " + response.body().size());
                Log.d(TAG, "onResponse: " + response.body().toString());
            }

            @Override
            public void onFailure(Call<List<Permission>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                permissionCallBack.onErrorPermissionList(t.getMessage());
            }
        });
    }
}
