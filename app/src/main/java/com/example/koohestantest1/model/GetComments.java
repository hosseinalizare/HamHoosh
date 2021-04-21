package com.example.koohestantest1.model;

public class GetComments {
    private String ObjectId;
    private String SenderId;
    private String SenderName;
    private String data;
    private String Date;
    private Boolean EditAble;
    private int CommentID;
    private Boolean IsPublised;
    private Boolean IsEmployee;
    private String UID;

    public Boolean getEmployee() {
        return IsEmployee;
    }

    public String getObjectId() {
        return ObjectId;
    }

    public String getSenderId() {
        return SenderId;
    }

    public String getSenderName() {
        return SenderName;
    }

    public String getData() {
        return data;
    }

    public String getDate() {
        return Date;
    }

    public Boolean getEditAble() {
        return EditAble;
    }

    public int getCommentID() {
        return CommentID;
    }

    public Boolean getPublised() {
        return IsPublised;
    }

    public String getUID() {
        return UID;
    }
}
