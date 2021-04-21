package com.example.koohestantest1.classDirectory;

public class GetSignUp {
    private  String UserID;
    private String Token;
    private String msg;

    public GetSignUp(String userID, String token, String msg) {
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
}
