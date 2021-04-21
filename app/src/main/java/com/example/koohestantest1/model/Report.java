package com.example.koohestantest1.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Report {
    private String UserId;
    private String Token;
    private int id;
    private int ErrorType;
    private String ErrorDescription;
    private Date SendDate;
    private int Status;
    private String ReporterUserID;
    private String ErrorTitle;
    private String UserName;
    private String UserEmaile;
    private String MobileNumber;
    private String OrderID;
    private int MessageStatus;
    private String errorReportMessag;

    public Report(String userId, String token, int id, int errorType, String errorDescription, String reporterUserID, String errorTitle, String userEmaile) {
        UserId = userId;
        Token = token;
        this.id = id;
        ErrorType = errorType;
        ErrorDescription = errorDescription;
        ReporterUserID = reporterUserID;
        ErrorTitle = errorTitle;
        UserEmaile = userEmaile;
    }

    public Report(String userId, String token, int id, int errorType, String errorDescription, Date sendDate, int status, String reporterUserID, String errorTitle, String userName, String userEmaile, String mobileNumber, String orderID, int messageStatus, String errorReportMessag) {
        UserId = userId;
        Token = token;
        this.id = id;
        ErrorType = errorType;
        ErrorDescription = errorDescription;
        SendDate = sendDate;
        Status = status;
        ReporterUserID = reporterUserID;
        ErrorTitle = errorTitle;
        UserName = userName;
        UserEmaile = userEmaile;
        MobileNumber = mobileNumber;
        OrderID = orderID;
        MessageStatus = messageStatus;
        this.errorReportMessag = errorReportMessag;
    }
}
