package com.example.koohestantest1.classDirectory;

public class SendSignUp {
    private String Username;
    private String Pass;
    private String id;
    private String token;
    private String Email;
    private String DeviceId;
    private String Devicemodel;
    private String FirstName;
    private String LastName;

    public SendSignUp(String username, String pass, String id, String token, String email, String deviceId,
                      String devicemodel, String FirstName, String LastName) {
        Username = username;
        Pass = pass;
        this.id = id;
        this.token = token;
        Email = email;
        DeviceId = deviceId;
        Devicemodel = devicemodel;
        this.FirstName = FirstName;
        this.LastName = LastName;
    }

    public String getUsername() {
        return Username;
    }

    public String getPass() {
        return Pass;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return Email;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public String getDevicemodel() {
        return Devicemodel;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }
}
