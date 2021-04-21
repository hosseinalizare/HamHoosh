package com.example.koohestantest1.model;

import com.google.gson.annotations.SerializedName;

public class EmployeeDeleting {
    @SerializedName("UserID")
    private String UserId;
    @SerializedName("Token")
    private String token;
    @SerializedName("EmployeeID")
    private String EmployeeId;

    public EmployeeDeleting(String userId, String token, String employeeId) {
        UserId = userId;
        this.token = token;
        EmployeeId = employeeId;
    }
}
