package com.example.koohestantest1.ViewModels;

public class UserReportViewModel {
    private String LastUpdateDate;
    private String Token;
    private String UserID;

    public UserReportViewModel(String lastUpdateDate, String token, String userID) {
        LastUpdateDate = lastUpdateDate;
        Token = token;
        UserID = userID;
    }

    public String getLastUpdateDate() {
        return LastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        LastUpdateDate = lastUpdateDate;
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
}
