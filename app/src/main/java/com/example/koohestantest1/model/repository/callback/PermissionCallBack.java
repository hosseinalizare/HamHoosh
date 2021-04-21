package com.example.koohestantest1.model.repository.callback;

import com.example.koohestantest1.model.Permission;

import java.util.List;

import com.example.koohestantest1.classDirectory.GetResualt;

public interface PermissionCallBack {
    void onSuccessPermissionList(List<Permission> permissions);

    void onErrorPermissionList(String error);

    void onSuccessEditPermission(GetResualt result);

    void onErrorEditPermission(String error);
}
