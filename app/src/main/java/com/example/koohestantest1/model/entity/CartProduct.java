package com.example.koohestantest1.model.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class CartProduct {

    @PrimaryKey(autoGenerate = true)
    private long productId;

    private long cartId;

    private String id;
    private String orderID;
    private String productIDString;
    private int quantity;
    private String unitPrice;
    private String discount;
    private String statusID;
    private String dateAllocated;
    private String purchaseOrderID;
    private String inventoryID;
    private String productName;
    private int sumPrice;
    private int sumOff;

    public CartProduct(long productId, long cartId, String id, String orderID, String productIDString, int quantity, String unitPrice, String discount, String statusID, String dateAllocated, String purchaseOrderID, String inventoryID, String productName, int sumPrice, int sumOff) {
        this.productId = productId;
        this.cartId = cartId;
        this.id = id;
        this.orderID = orderID;
        this.productIDString = productIDString;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discount = discount;
        this.statusID = statusID;
        this.dateAllocated = dateAllocated;
        this.purchaseOrderID = purchaseOrderID;
        this.inventoryID = inventoryID;
        this.productName = productName;
        this.sumPrice = sumPrice;
        this.sumOff = sumOff;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getProductId() {
        return productId;
    }

    public long getCartId() {
        return cartId;
    }

    public String getId() {
        return id;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getProductIDString() {
        return productIDString;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public String getDiscount() {
        return discount;
    }

    public String getStatusID() {
        return statusID;
    }

    public String getDateAllocated() {
        return dateAllocated;
    }

    public String getPurchaseOrderID() {
        return purchaseOrderID;
    }

    public String getInventoryID() {
        return inventoryID;
    }

    public String getProductName() {
        return productName;
    }

    public int getSumPrice() {
        return sumPrice;
    }

    public int getSumOff() {
        return sumOff;
    }
}
