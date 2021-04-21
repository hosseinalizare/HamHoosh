package com.example.koohestantest1.classDirectory;

public class SendVerifyCode {
    private String code;
    private String mobileNumber;
    private String DeviceId;
    private String Devicemodel;

    public SendVerifyCode(String code, String mobileNumber, String deviceId, String devicemodel) {
        this.code = code;
        this.mobileNumber = mobileNumber;
        DeviceId = deviceId;
        Devicemodel = devicemodel;
    }
}
