package com.example.koohestantest1.model;

import java.util.List;

public class FilledNewsImage {
    private String newsId;
    private List<String> src;

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public List<String> getSrc() {
        return src;
    }

    public void setSrc(List<String> src) {
        this.src = src;
    }
}
