package com.example.koohestantest1.ApiDirectory;

import com.example.koohestantest1.model.UpdateModeModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UpdateModeApi {
    @GET("DataChanges/GetUpdateMode")
    Call<Integer> getUpdateMode();

    @GET("DataChanges/GetUpdateMode")
    Call<List<UpdateModeModel>> getUpdateMode2(@Query("UID") String uid, @Query("CID") String cid);

    @GET("ServerInfo/SET")
    Call<String>  sendUserId(@Query("UserID") String UserID);

    @GET("ServerInfo/GETUID")
    Call<String>  getUserId();
}
