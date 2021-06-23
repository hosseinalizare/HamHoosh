package com.example.koohestantest1.model;

import java.util.List;

public class ForwardMsgM {
    private String Token;
    private String UserID;
    private String id;
    private String UserForwarder;
    private List<String> UsergetMage;

    public ForwardMsgM(String token, String userID, String id, String userForwarder, List<String> usergetMage) {
        Token = token;
        UserID = userID;
        this.id = id;
        UserForwarder = userForwarder;
        UsergetMage = usergetMage;
    }
}
