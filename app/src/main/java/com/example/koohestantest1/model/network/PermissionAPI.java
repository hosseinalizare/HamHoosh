package com.example.koohestantest1.model.network;

import com.example.koohestantest1.model.EditPermission;
import com.example.koohestantest1.model.Permission;

import java.util.List;

import com.example.koohestantest1.classDirectory.GetResualt;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PermissionAPI {
    @POST("Company/GetEmploeeAccseeLevel")
    Call<List<Permission>> getEmployeePermissions(@Query("EmploeeId") String employeeId);

    @POST("Company/EditeEmployeeAccessLevel")
    Call<GetResualt> editEmployeePermission(@Body EditPermission editPermission);
}
