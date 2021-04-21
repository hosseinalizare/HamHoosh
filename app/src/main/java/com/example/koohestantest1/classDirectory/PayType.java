package com.example.koohestantest1.classDirectory;

public class PayType {
    private int id;
    private String title;
    private String imageAddress;
    private int action;
    private int changPay;

    public int getChangPay() {
        return changPay;
    }

    public void setChangPay(int changPay) {
        this.changPay = changPay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
