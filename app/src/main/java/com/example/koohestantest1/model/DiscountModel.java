package com.example.koohestantest1.model;

import com.example.koohestantest1.classDirectory.SendOrderClass;
import com.google.gson.annotations.SerializedName;

public class DiscountModel {
    @SerializedName("order")
    private SendOrderClass sendOrderClass;

    @SerializedName("DiscountCode")
    private String discountCode;

    public SendOrderClass getSendOrderClass() {
        return sendOrderClass;
    }

    public void setSendOrderClass(SendOrderClass sendOrderClass) {
        this.sendOrderClass = sendOrderClass;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }
}
