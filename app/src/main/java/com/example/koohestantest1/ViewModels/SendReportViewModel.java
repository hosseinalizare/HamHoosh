package com.example.koohestantest1.ViewModels;

public class SendReportViewModel {
    private String UserId;
    private String Token;
    private String id;
    private String ErrorType;
    private String ErrorDescription;
    private String SendDate;
    private String Status;
    private String ReporterUserID;
    private String ErrorTitle;
    private String UserName;
    private String UserEmaile;
    private String MobileNumber;
    private String OrderID;
    private String MessageStatus;
    private String errorReportMessag;
    private String Spare1;

//    {
//        "id": 1,
//            "MessageStatus": 2,
//            "ErrorMessagID": 1,
//            "SenderId": "sample string 3",
//            "ReciverId": "sample string 4",
//            "Answer": "sample string 5",
//            "AnswerDate": "2020-08-21T18:53:45.7653995+04:30",
//            "Deleted": true
//    }

    public SendReportViewModel(String userId, String token, String errorType, String errorDescription, String status, String reporterUserID, String errorTitle, String userName, String userEmaile, String mobileNumber, String orderID, String messageStatus) {
        UserId = userId;
        Token = token;
        ErrorType = errorType;
        ErrorDescription = errorDescription;
        Status = status;
        ReporterUserID = reporterUserID;
        ErrorTitle = errorTitle;
        UserName = userName;
        UserEmaile = userEmaile;
        MobileNumber = mobileNumber;
        OrderID = orderID;
        MessageStatus = messageStatus;
    }
    public SendReportViewModel(String userId, String token, String errorType, String errorDescription, String status, String reporterUserID, String errorTitle, String userName, String userEmaile, String mobileNumber, String orderID, String messageStatus,String spare1) {

        UserId = userId;
        Token = token;
        ErrorType = errorType;
        ErrorDescription = errorDescription;
        Status = status;
        ReporterUserID = reporterUserID;
        ErrorTitle = errorTitle;
        UserName = userName;
        UserEmaile = userEmaile;
        MobileNumber = mobileNumber;
        OrderID = orderID;
        MessageStatus = messageStatus;
        this.Spare1 =spare1;
    }


    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getErrorType() {
        return ErrorType;
    }

    public void setErrorType(String errorType) {
        ErrorType = errorType;
    }

    public String getErrorDescription() {
        return ErrorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        ErrorDescription = errorDescription;
    }

    public String getSendDate() {
        return SendDate;
    }

    public void setSendDate(String sendDate) {
        SendDate = sendDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getReporterUserID() {
        return ReporterUserID;
    }

    public void setReporterUserID(String reporterUserID) {
        ReporterUserID = reporterUserID;
    }

    public String getErrorTitle() {
        return ErrorTitle;
    }

    public void setErrorTitle(String errorTitle) {
        ErrorTitle = errorTitle;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserEmaile() {
        return UserEmaile;
    }

    public void setUserEmaile(String userEmaile) {
        UserEmaile = userEmaile;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getMessageStatus() {
        return MessageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        MessageStatus = messageStatus;
    }

    public String getErrorReportMessag() {
        return errorReportMessag;
    }

    public void setErrorReportMessag(String errorReportMessag) {
        this.errorReportMessag = errorReportMessag;
    }
}
