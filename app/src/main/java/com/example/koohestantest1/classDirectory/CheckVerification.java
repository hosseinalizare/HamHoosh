package com.example.koohestantest1.classDirectory;

public class CheckVerification {
    private String UserID;
    private String Token;
    private String msg;

    public CheckVerification(String userID, String token, String msg) {
        UserID = userID;
        Token = token;
        this.msg = msg;
    }

    public String getUserID() {
        return UserID;
    }

    public String getToken() {
        return Token;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "CheckVerification{" +
                "UserID='" + UserID + '\'' +
                ", Token='" + Token + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
