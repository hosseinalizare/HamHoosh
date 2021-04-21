package com.example.koohestantest1.model;

import com.google.gson.annotations.SerializedName;

public class Permission {
    @SerializedName("Name")
    private String title;
    @SerializedName("Description")
    private String description;
    @SerializedName("Pos")
    private int pos;

    public void setState(boolean state) {
        this.state = state;
    }

    @SerializedName("State")
    private boolean state;

    public Permission(String title, String description, int pos, boolean state) {
        this.title = title;
        this.description = description;
        this.pos = pos;
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPos() {
        return pos;
    }

    public boolean isState() {
        return state;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", pos=" + pos +
                ", state=" + state +
                '}';
    }
}
