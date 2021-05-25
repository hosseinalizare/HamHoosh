package com.example.koohestantest1.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JsonSrc {

    @SerializedName("Jsonsrc")
    private List<String> jsonSrc;

    public List<String> getJsonSrc() {
        return jsonSrc;
    }

    public void setJsonSrc(List<String> jsonSrc) {
        this.jsonSrc = jsonSrc;
    }
}
