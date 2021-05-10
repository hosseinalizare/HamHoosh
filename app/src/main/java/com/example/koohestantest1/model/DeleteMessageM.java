package com.example.koohestantest1.model;

import java.util.List;

public class DeleteMessageM {

    private String Token;
    private String UserID;
    private String RecipientUser;
    private List<Integer> id;

    public DeleteMessageM(String token, String userID, String recipientUser, List<Integer> id) {
        Token = token;
        UserID = userID;
        RecipientUser = recipientUser;
        this.id = id;
    }
}
