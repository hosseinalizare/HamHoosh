package com.example.koohestantest1.model;

public class UpdateModeModel {
    private String Title;
    private String Msge;
    private int EventId;
    private String Url;
    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMsge() {
        return Msge;
    }

    public void setMsge(String msge) {
        Msge = msge;
    }

    public int getEventId() {
        return EventId;
    }

    public void setEventId(int eventId) {
        EventId = eventId;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
