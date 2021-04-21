package com.example.koohestantest1.model;

public class IranCity {
    private int province_id;
    private String title;

    public IranCity(int province_id, String title) {
        this.province_id = province_id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getProvince_id() {
        return province_id;
    }

    @Override
    public String toString() {
        return title;
    }
}
