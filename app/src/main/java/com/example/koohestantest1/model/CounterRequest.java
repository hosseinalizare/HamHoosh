package com.example.koohestantest1.model;

import com.google.gson.annotations.SerializedName;

public class CounterRequest {
    @SerializedName("UserID")
    private String userId;
    @SerializedName("Token")
    private String token;
    @SerializedName("CompanyID")
    private String companyId;

    public CounterRequest(String userId, String token, String companyId) {
        this.userId = userId;
        this.token = token;
        this.companyId = companyId;
    }

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public String getCompanyId() {
        return companyId;
    }
}
