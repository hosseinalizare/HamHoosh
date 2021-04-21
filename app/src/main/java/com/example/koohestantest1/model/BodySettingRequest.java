package com.example.koohestantest1.model;

import com.google.gson.annotations.SerializedName;

public class BodySettingRequest {
    @SerializedName("UserID")
    private String userId;
    @SerializedName("CompanyID")
    private String companyId;
    @SerializedName("Token")
    private String token;

    public BodySettingRequest(String userId, String companyId, String token) {
        this.userId = userId;
        this.companyId = companyId;
        this.token = token;
    }
}
