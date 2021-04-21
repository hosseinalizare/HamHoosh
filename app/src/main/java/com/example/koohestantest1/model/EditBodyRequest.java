package com.example.koohestantest1.model;

import com.google.gson.annotations.SerializedName;

public class EditBodyRequest {
    @SerializedName("Token")
    private String token;
    @SerializedName("UserID")
    private String userId;
    @SerializedName("CompanyID")
    private String companyId;
    @SerializedName("ProductID")
    private String productId;
    @SerializedName("FieldItem")
    private String fieldItem;
    @SerializedName("FieldValue")
    private String fieldValue;

    public EditBodyRequest(String token, String userId, String companyId, String productId, String fieldItem, String fieldValue) {
        this.token = token;
        this.userId = userId;
        this.companyId = companyId;
        this.productId = productId;
        this.fieldItem = fieldItem;
        this.fieldValue = fieldValue;
    }
}
