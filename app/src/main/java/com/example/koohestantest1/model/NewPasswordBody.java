package com.example.koohestantest1.model;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.POST;

public class NewPasswordBody {
    private String code;
    @SerializedName("UserName")
    private String username;
    @SerializedName("PassWord")
    private String password;
    @SerializedName("DeviceId")
    private String IMEI;
    @SerializedName("deviceModel")
    private String deviceModel;

    public NewPasswordBody(String code, String username, String password, String IMEI, String deviceModel) {
        this.code = code;
        this.username = username;
        this.password = password;
        this.IMEI = IMEI;
        this.deviceModel = deviceModel;
    }
}
