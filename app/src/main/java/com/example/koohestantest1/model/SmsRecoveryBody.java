package com.example.koohestantest1.model;

import com.google.gson.annotations.SerializedName;

public class SmsRecoveryBody {
    @SerializedName("UserName")
    private String username;
    @SerializedName("DeviceId")
    private String IMEI;
    @SerializedName("Devicemodel")
    private String deviceModel;

    public SmsRecoveryBody(String username, String IMEI, String deviceModel) {
        this.username = username;
        this.IMEI = IMEI;
        this.deviceModel = deviceModel;
    }
}
