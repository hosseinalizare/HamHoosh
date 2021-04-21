package com.example.koohestantest1.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koohestantest1.model.EditPermission;
import com.example.koohestantest1.model.Permission;
import com.example.koohestantest1.model.repository.PermissionRepository;
import com.example.koohestantest1.model.repository.callback.PermissionCallBack;

import java.util.List;

import com.example.koohestantest1.classDirectory.GetResualt;

public class PermissionViewModel extends ViewModel implements PermissionCallBack {

    private final MutableLiveData<List<Permission>> liveListPermissions = new MutableLiveData<>();
    private final MutableLiveData<String> liveErrorPermissions = new MutableLiveData<>();

    private final MutableLiveData<GetResualt> liveEditPermissionRes = new MutableLiveData<>();
    private final MutableLiveData<String> liveErrorEditPermission = new MutableLiveData<>();

    private final PermissionRepository permissionRepository = new PermissionRepository(this);

    public void callForEditEmployeePermission(EditPermission editPermission) {
        permissionRepository.editEmployeePermission(editPermission);
    }

    public void callForPermissionList(String permissionList) {
        permissionRepository.getEmployeePermissions(permissionList);
    }

    @Override
    public void onSuccessPermissionList(List<Permission> permissions) {
        liveListPermissions.setValue(permissions);
    }

    @Override
    public void onErrorPermissionList(String error) {
        liveErrorPermissions.setValue(error);
    }

    @Override
    public void onSuccessEditPermission(GetResualt result) {
        liveEditPermissionRes.setValue(result);
    }

    @Override
    public void onErrorEditPermission(String error) {

    }

    public LiveData<String> getLiveErrorPermissions() {
        return liveErrorPermissions;
    }

    public LiveData<List<Permission>> getLiveListPermissions() {
        return liveListPermissions;
    }

    public LiveData<GetResualt> getLiveEditPermissionRes() {
        return liveEditPermissionRes;
    }

    public LiveData<String> getLiveErrorEditPermission() {
        return liveErrorEditPermission;
    }
}
