package com.example.koohestantest1.ViewModels;

import com.example.koohestantest1.classDirectory.UserProfile;

public class CompanyFollowerViewModel {
    private String CustomerID;
    private String CompanyID;
    private String UserID;
    private String UpdateDate;
    private String Spare1;
    private String Spare2;
    private String Spare3;
    private UserProfile UserData;

    public CompanyFollowerViewModel(String customerID, String companyID, String userID, String updateDate, String spare1, String spare2, String spare3, UserProfile userData) {
        CustomerID = customerID;
        CompanyID = companyID;
        UserID = userID;
        UpdateDate = updateDate;
        Spare1 = spare1;
        Spare2 = spare2;
        Spare3 = spare3;
        UserData = userData;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(String companyID) {
        CompanyID = companyID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(String updateDate) {
        UpdateDate = updateDate;
    }

    public String getSpare1() {
        return Spare1;
    }

    public void setSpare1(String spare1) {
        Spare1 = spare1;
    }

    public String getSpare2() {
        return Spare2;
    }

    public void setSpare2(String spare2) {
        Spare2 = spare2;
    }

    public String getSpare3() {
        return Spare3;
    }

    public void setSpare3(String spare3) {
        Spare3 = spare3;
    }

    public UserProfile getUserData() {
        return UserData;
    }

    public void setUserData(UserProfile userData) {
        UserData = userData;
    }
}
