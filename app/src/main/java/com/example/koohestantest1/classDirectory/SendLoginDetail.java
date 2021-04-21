package com.example.koohestantest1.classDirectory;

public class SendLoginDetail {
    private String Username;
    private String Pass;
    private String DeviceId;
    private String Devicemodel;

    public SendLoginDetail(String username, String pass, String deviceId, String devicemodel) {
        Username = username;
        Pass = pass;
        DeviceId = deviceId;
        Devicemodel = devicemodel;
    }
}
