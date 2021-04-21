package com.example.koohestantest1.ViewModels;

public class CompanyProfileFieldViewModel {
    private String UserID;
    private String Token;
    private String ObjectID;
    private String Value;
    private String Cenum;

    public CompanyProfileFieldViewModel(String userID, String token, String objectID, String value, String cenum) {
        UserID = userID;
        Token = token;
        ObjectID = objectID;
        Value = value;
        Cenum = cenum;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getObjectID() {
        return ObjectID;
    }

    public void setObjectID(String objectID) {
        ObjectID = objectID;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getCenum() {
        return Cenum;
    }

    public void setCenum(String cenum) {
        Cenum = cenum;
    }
}
