package com.example.koohestantest1.classDirectory;

public class GetVerifySmsClass {

    private String UserID;
    private String msg;
    private String msgDetaile;

    public GetVerifySmsClass(String userID, String msg, String msgDetaile) {
        UserID = userID;
        this.msg = msg;
        this.msgDetaile = msgDetaile;
    }

    public String getUserID() {
        return UserID;
    }

    public String getMsg() {
        return msg;
    }

    public String getMsgDetaile() {
        return msgDetaile;
    }
}
