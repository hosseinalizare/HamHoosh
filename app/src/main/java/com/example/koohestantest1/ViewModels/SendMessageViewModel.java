package com.example.koohestantest1.ViewModels;

public class SendMessageViewModel {
    private String Token;
    private String UserID;
    private String id;
    private String UserSender;
    private String RecipientUser;
    private String Message1;
    private String DateSend;
    private String Status;
    private String UpdateDate;
    private String SenderDeleted;
    private String RecipientDeleted;
    private String ReplyMsg;
    private int MsgType ;
    private String AttachObjectID ;
    private int Page ;
    private int Len ;


    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }



    public SendMessageViewModel(String userSender, int msgType) {
        UserSender = userSender;
        MsgType = msgType;
    }

    public int getMsgType() {
        return MsgType;
    }

    public void setMsgType(int msgType) {
        MsgType = msgType;
    }

    public String getAttachObjectID() {
        return AttachObjectID;
    }

    public void setAttachObjectID(String attachObjectID) {
        AttachObjectID = attachObjectID;
    }

    public int getPage() {
        return Page;
    }

    public void setPage(int page) {
        Page = page;
    }

    public int getLen() {
        return Len;
    }

    public void setLen(int len) {
        Len = len;
    }

    public SendMessageViewModel(String token, String UserID, String id, String userSender, String recipientUser,
                                String message1, String dateSend, String status, String replyMsg,
                                  int msgType ,String attachObjectID ,int page,int len ) {
        Token = token;
        this.UserID = UserID;
        this.id = id;
        UserSender = userSender;
        RecipientUser = recipientUser;
        Message1 = message1;
        DateSend = dateSend;
        Status = status;
        ReplyMsg = replyMsg;
        MsgType =msgType;
        AttachObjectID =attachObjectID;
        Page =page;
        Len =len;
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

    public String getUserSender() {
        return UserSender;
    }

    public void setUserSender(String userSender) {
        UserSender = userSender;
    }

    public String getRecipientUser() {
        return RecipientUser;
    }

    public void setRecipientUser(String recipientUser) {
        RecipientUser = recipientUser;
    }

    public String getMessage1() {
        return Message1;
    }

    public void setMessage1(String message1) {
        Message1 = message1;
    }

    public String getDateSend() {
        return DateSend;
    }

    public void setDateSend(String dateSend) {
        DateSend = dateSend;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(String updateDate) {
        UpdateDate = updateDate;
    }

    public String getSenderDeleted() {
        return SenderDeleted;
    }

    public void setSenderDeleted(String senderDeleted) {
        SenderDeleted = senderDeleted;
    }

    public String getRecipientDeleted() {
        return RecipientDeleted;
    }

    public void setRecipientDeleted(String recipientDeleted) {
        RecipientDeleted = recipientDeleted;
    }

    public String getReplyMsg() {
        return ReplyMsg;
    }

    public void setReplyMsg(String replyMsg) {
        ReplyMsg = replyMsg;
    }

    @Override
    public String toString() {
        return "SendMessageViewModel{" +
                "Token='" + Token + '\'' +
                ", UserID='" + UserID + '\'' +
                ", id='" + id + '\'' +
                ", UserSender='" + UserSender + '\'' +
                ", RecipientUser='" + RecipientUser + '\'' +
                ", Message1='" + Message1 + '\'' +
                ", DateSend='" + DateSend + '\'' +
                ", Status='" + Status + '\'' +
                ", UpdateDate='" + UpdateDate + '\'' +
                ", SenderDeleted='" + SenderDeleted + '\'' +
                ", RecipientDeleted='" + RecipientDeleted + '\'' +
                ", ReplyMsg='" + ReplyMsg + '\'' +
                '}';
    }
}
