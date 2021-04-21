package com.example.koohestantest1.model;

public class PublishComment {
    private String ObjectId;
    private String UserId;
    private String Token;
    private String data;

    public PublishComment(String objectId, String userId, String token, String data) {
        ObjectId = objectId;
        UserId = userId;
        Token = token;
        this.data = data;
    }
}
