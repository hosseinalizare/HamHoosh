package com.example.koohestantest1.classDirectory;

public class SendPasswordRecovery {
    private String code;
    private String UserName;
    private String PassWord;
    private String DeviceId;
    private String Devicemodel;

    public SendPasswordRecovery(String code, String userName, String passWord, String deviceId, String devicemodel) {
        this.code = code;
        UserName = userName;
        PassWord = passWord;
        DeviceId = deviceId;
        Devicemodel = devicemodel;
    }
}
