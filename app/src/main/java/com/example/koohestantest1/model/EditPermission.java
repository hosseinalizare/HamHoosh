package com.example.koohestantest1.model;

import com.google.gson.annotations.SerializedName;

public class EditPermission {
    @SerializedName("Token")
    private String token;
    @SerializedName("UserID")
    private String userId;
    @SerializedName("EmploeeId")
    private String employeeId;
    @SerializedName("Enume")
    private int permissionPosition;
    @SerializedName("Value")
    private boolean state;
    @SerializedName("mode")
    private int mode;

    public EditPermission(String token, String userId, String employeeId, int permissionPosition, boolean state, int mode) {
        this.token = token;
        this.userId = userId;
        this.employeeId = employeeId;
        this.permissionPosition = permissionPosition;
        this.state = state;
        this.mode = mode;
    }

//    public EditPermission(String token, String userId, String employeeId, int permissionPosition, boolean state) {
//        this.token = token;
//        this.userId = userId;
//        this.employeeId = employeeId;
//        this.permissionPosition = permissionPosition;
//        this.state = state;
//    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public int getPermissionPosition() {
        return permissionPosition;
    }

    public boolean getState() {
        return state;
    }


    public int getMode() {
        return mode;
    }
}
