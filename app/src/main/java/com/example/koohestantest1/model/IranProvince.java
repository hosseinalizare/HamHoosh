package com.example.koohestantest1.model;

import com.google.gson.annotations.SerializedName;

public class IranProvince {
    private String title;
    private int id;

    public IranProvince(String title, int id) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return title;
    }
}
