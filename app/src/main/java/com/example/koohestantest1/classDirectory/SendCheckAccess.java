package com.example.koohestantest1.classDirectory;

public class SendCheckAccess {
    private String id;
    private String token;
    private String DeviceId;
    private String Devicemodel;

    public SendCheckAccess(String id, String token, String deviceId, String devicemodel) {
        this.id = id;
        this.token = token;
        DeviceId = deviceId;
        Devicemodel = devicemodel;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public String getDevicemodel() {
        return Devicemodel;
    }

    @Override
    public String toString() {
        return "SendCheckAccess{" +
                "id='" + id + '\'' +
                ", token='" + token + '\'' +
                ", DeviceId='" + DeviceId + '\'' +
                ", Devicemodel='" + Devicemodel + '\'' +
                '}';
    }
}
