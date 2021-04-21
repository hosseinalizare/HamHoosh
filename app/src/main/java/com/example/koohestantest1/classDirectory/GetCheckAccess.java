package com.example.koohestantest1.classDirectory;

public class GetCheckAccess {
    private String Resualt;
    private  String Msg;

    public GetCheckAccess(String resualt, String msg) {
        Resualt = resualt;
        Msg = msg;
    }

    public String getResualt() {
        return Resualt;
    }

    public String getMsg() {
        return Msg;
    }

    public void setResualt(String resualt) {
        Resualt = resualt;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }
}
