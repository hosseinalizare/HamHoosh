package com.example.koohestantest1.classDirectory;

import java.util.List;

public class HowToPay {
    private String title;
    private List<PayType> payType;
    private int carierPay;


    public int getCarierPay() {
        return carierPay;
    }

    public void setCarierPay(int carierPay) {
        this.carierPay = carierPay;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<PayType> getPayType() {
        return payType;
    }

    public void setPayType(List<PayType> payType) {
        this.payType = payType;
    }
}
