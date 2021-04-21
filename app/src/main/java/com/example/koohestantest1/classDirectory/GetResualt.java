package com.example.koohestantest1.classDirectory;

public class GetResualt {
    private String Resualt;
    private  String Msg;

    public String getResualt() {
        return Resualt;
    }

    public String getMsg() {
        return Msg;
    }

    public GetResualt(String resualt, String msg) {
        Resualt = resualt;
        Msg = msg;
    }

    @Override
    public String toString() {
        return "GetResualt{" +
                "Resualt='" + Resualt + '\'' +
                ", Msg='" + Msg + '\'' +
                '}';
    }
}
