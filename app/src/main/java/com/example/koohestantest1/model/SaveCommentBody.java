package com.example.koohestantest1.model;

public class SaveCommentBody {
    private String ObjectId;
    private String UserId;
    private String Token;
    private String data;

    public SaveCommentBody(String objectId, String userId, String token, String data) {
        ObjectId = objectId;
        UserId = userId;
        Token = token;
        this.data = data;
    }
}
