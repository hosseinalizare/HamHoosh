package com.example.koohestantest1.model;

import com.google.gson.annotations.SerializedName;

public class DeleteNewsLetter {

    @SerializedName("Token")
    private String token;

    @SerializedName("UserID")
    private String userId;

    @SerializedName("CompanyID")
    private String companyId;

    @SerializedName("NewsID")
    private String newsId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }
}
