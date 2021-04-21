package com.example.koohestantest1.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MyTime {
    @SerializedName("CurrentTime")
    private String currentTime;
    @SerializedName("CurrentDate")
    private String currentDate;

    public MyTime(String currentTime, String currentDate) {
        this.currentTime = currentTime;
        this.currentDate = currentDate;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }
}
