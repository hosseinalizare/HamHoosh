package com.example.koohestantest1.ViewModels;

public class UpdateOrderClass {

    private String Token;
    private String UserID;
    private String OrderID;
    private String StatusID;

    public UpdateOrderClass(String token, String userID, String orderID, String statusID) {
        Token = token;
        UserID = userID;
        OrderID = orderID;
        StatusID = statusID;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getStatusID() {
        return StatusID;
    }

    public void setStatusID(String statusID) {
        StatusID = statusID;
    }
}
