package com.example.koohestantest1.model;

import com.google.gson.annotations.SerializedName;

public class SettingField {
    @SerializedName("UserID")
    private String userId;

    @SerializedName("Token")
    private String token;

    @SerializedName("CompanyId")
    private String companyId;

    @SerializedName("Value")
    private String value;

    @SerializedName("EnumId")
    private int enumId;

    public SettingField(String userId, String token, String companyId,
                        String value, int enumId) {
        this.userId = userId;
        this.token = token;
        this.companyId = companyId;
        this.value = value;
        this.enumId = enumId;
    }

}
