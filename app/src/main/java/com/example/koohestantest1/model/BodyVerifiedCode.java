package com.example.koohestantest1.model;

import com.google.gson.annotations.SerializedName;

public class BodyVerifiedCode {
    @SerializedName("code")
    private String code;
    @SerializedName("mobileNumber")
    private String phone;
    @SerializedName("DeviceId")
    private String IMEI;
    @SerializedName("Devicemodel")
    private String deviceModel;

    public BodyVerifiedCode(String code, String phone, String IMEI, String deviceModel) {
        this.code = code;
        this.phone = phone;
        this.IMEI = IMEI;
        this.deviceModel = deviceModel;
    }

}
