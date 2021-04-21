package com.example.koohestantest1.model;

public class VersionBody {
    private String userId;
    private String versionName;
    private String companyId;

    public VersionBody(String userId, String versionName, String companyId) {
        this.userId = userId;
        this.versionName = versionName;
        this.companyId = companyId;
    }
}
