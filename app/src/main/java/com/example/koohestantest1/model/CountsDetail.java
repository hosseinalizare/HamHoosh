package com.example.koohestantest1.model;

import com.google.gson.annotations.SerializedName;

public class CountsDetail {
    @SerializedName("NewMsgCounter")
    private int newMsg;
    @SerializedName("NewOrderCounter")
    private int newOrder;
    @SerializedName("ProsessOrderCounter")
    private int processOrder;
    @SerializedName("PendingCounter")
    private int pending;
    @SerializedName("sendingCounter")
    private int sending;
    @SerializedName("Ready2DeliveryCounter")
    private int readyDeliver;
    @SerializedName("DeliveredCounter")
    private int delivered;
    @SerializedName("CanceledCounter")
    private int canceled;
    @SerializedName("UnvisibleProductCounter")
    private int invisibleProducts;
    @SerializedName("NonExistProductCounter")
    private int notInStockProducts;


    public CountsDetail(int newMsg, int newOrder, int processOrder, int pending, int sending, int readyDeliver,
                        int delivered, int canceled, int invisibleProducts, int notInStockProducts) {
        this.newMsg = newMsg;
        this.newOrder = newOrder;
        this.processOrder = processOrder;
        this.pending = pending;
        this.sending = sending;
        this.readyDeliver = readyDeliver;
        this.delivered = delivered;
        this.canceled = canceled;
        this.invisibleProducts = invisibleProducts;
        this.notInStockProducts = notInStockProducts;
    }

    public int getNewMsg() {
        return newMsg;
    }

    public int getNewOrder() {
        return newOrder;
    }

    public int getProcessOrder() {
        return processOrder;
    }

    public int getPending() {
        return pending;
    }

    public int getSending() {
        return sending;
    }

    public int getReadyDeliver() {
        return readyDeliver;
    }

    public int getDelivered() {
        return delivered;
    }

    public int getCanceled() {
        return canceled;
    }

    public int getInvisibleProducts() {
        return invisibleProducts;
    }

    public int getNotInStockProducts() {
        return notInStockProducts;
    }

    @Override
    public String toString() {
        return "CountsDetail{" +
                "newMsg=" + newMsg +
                ", newOrder=" + newOrder +
                ", processOrder=" + processOrder +
                ", pending=" + pending +
                ", sending=" + sending +
                ", readyDeliver=" + readyDeliver +
                ", delivered=" + delivered +
                ", canceled=" + canceled +
                '}';
    }
}
