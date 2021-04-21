package com.example.koohestantest1.model;

import com.google.gson.annotations.SerializedName;

public class EmployeeAdding {
    @SerializedName("Token")
    private String token;

    @SerializedName("UserID")
    private String userId;

    @SerializedName("CompanyID")
    private String companyId;

    @SerializedName("CompanyName")
    private String companyName;

    @SerializedName("Notes")
    private String notes;

    @SerializedName("UserName")
    private String userName;

    @SerializedName("PassWord")
    private String password;

    @SerializedName("Position")
    private String position;

    @SerializedName("AccessLlevel")
    private int accessLevel;

    public EmployeeAdding(String token, String userId, String companyId, String companyName, String notes, String userName, String password, String position, int accessLevel) {
        this.token = token;
        this.userId = userId;
        this.companyId = companyId;
        this.companyName = companyName;
        this.notes = notes;
        this.userName = userName;
        this.password = password;
        this.position = position;
        this.accessLevel = accessLevel;
    }
}
