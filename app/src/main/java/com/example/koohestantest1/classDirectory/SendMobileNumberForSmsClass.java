package com.example.koohestantest1.classDirectory;

public class SendMobileNumberForSmsClass {

    private String mobileNumber;
    private String DeviceId;
    private String Devicemodel;

    public SendMobileNumberForSmsClass(String mobileNumber, String deviceId, String devicemodel) {
        this.mobileNumber = mobileNumber;
        DeviceId = deviceId;
        Devicemodel = devicemodel;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public String getDevicemodel() {
        return Devicemodel;
    }

    public void setDevicemodel(String devicemodel) {
        Devicemodel = devicemodel;
    }
}
