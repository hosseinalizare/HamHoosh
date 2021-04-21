package com.example.koohestantest1.ViewModels;

public class ContactListViewModel {
    private String ObjectID;
    private String Message1;
    private String Status;
    private String CountNewMsg;

    private String objectName;

    private String lastMsgTime;

    private String lastOnlineTime;

    public ContactListViewModel(String objectID, String message1, String status, String countNewMsg) {
        ObjectID = objectID;
        Message1 = message1;
        Status = status;
        CountNewMsg = countNewMsg;
    }

    public ContactListViewModel(String objectID, String message1, String status, String countNewMsg, String objectName, String lastMsgTime, String lastOnlineTime) {
        ObjectID = objectID;
        Message1 = message1;
        Status = status;
        CountNewMsg = countNewMsg;
        this.objectName = objectName;
        this.lastMsgTime = lastMsgTime;
        this.lastOnlineTime = lastOnlineTime;
    }

    public ContactListViewModel() {
    }

    public String getObjectID() {
        return ObjectID;
    }

    public void setObjectID(String objectID) {
        ObjectID = objectID;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getLastMsgTime() {
        return lastMsgTime;
    }

    public String getLastOnlineTime() {
        return lastOnlineTime;
    }

    public String getMessage1() {
        return Message1;
    }

    public void setMessage1(String message1) {
        Message1 = message1;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCountNewMsg() {
        return CountNewMsg;
    }

    public void setCountNewMsg(String countNewMsg) {
        CountNewMsg = countNewMsg;
    }
}
