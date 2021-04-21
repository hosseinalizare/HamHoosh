package com.example.koohestantest1.ViewModels;

import java.util.ArrayList;
import java.util.List;

import com.example.koohestantest1.classDirectory.ProductPropertisClass;

public class Order_DetailsViewModels {
    private String ID;
    private String OrderID;
    private String ProductID;
    private int Quantity;
    private String UnitPrice;
    private String Discount;
    private String StatusID;
    private String DateAllocated;
    private String PurchaseOrderID;
    private String InventoryID;
    private String ProductName;
    private int SumPrice;
    private int SumOff;

    private List<ProductPropertisClass> PropertisViewModels;

    public Order_DetailsViewModels(String ID, String orderID, String productID, int quantity, String unitPrice, String discount, String statusID, String dateAllocated, String purchaseOrderID, String inventoryID, String productName, List<ProductPropertisClass> propertisViewModels) {
        this.ID = ID;
        OrderID = orderID;
        ProductID = productID;
        Quantity = quantity;
        UnitPrice = unitPrice;
        Discount = discount;
        StatusID = statusID;
        DateAllocated = dateAllocated;
        PurchaseOrderID = purchaseOrderID;
        InventoryID = inventoryID;
        ProductName = productName;
        PropertisViewModels = propertisViewModels;
    }

    public Order_DetailsViewModels(){
        PropertisViewModels = new ArrayList<>();
    }

    public String getID() {
        return ID;
    }

    public String getOrderID() {
        return OrderID;
    }

    public String getProductID() {
        return ProductID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public String getUnitPrice() {
        return UnitPrice;
    }

    public String getDiscount() {
        return Discount;
    }

    public String getStatusID() {
        return StatusID;
    }

    public String getDateAllocated() {
        return DateAllocated;
    }

    public String getPurchaseOrderID() {
        return PurchaseOrderID;
    }

    public String getInventoryID() {
        return InventoryID;
    }

    public String getProductName() {
        return ProductName;
    }

    public List<ProductPropertisClass> getPropertisViewModels() {
        return PropertisViewModels;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public void setUnitPrice(String unitPrice) {
        UnitPrice = unitPrice;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public void setStatusID(String statusID) {
        StatusID = statusID;
    }

    public void setDateAllocated(String dateAllocated) {
        DateAllocated = dateAllocated;
    }

    public void setPurchaseOrderID(String purchaseOrderID) {
        PurchaseOrderID = purchaseOrderID;
    }

    public void setInventoryID(String inventoryID) {
        InventoryID = inventoryID;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public void setPropertisViewModels(List<ProductPropertisClass> propertisViewModels) {
        PropertisViewModels = propertisViewModels;
    }

    public int getSumPrice() {
        return SumPrice;
    }

    public void setSumPrice(int sumPrice) {
        SumPrice = sumPrice;
    }

    public int getSumOff() {
        return SumOff;
    }

    public void setSumOff(int sumOff) {
        SumOff = sumOff;
    }

    @Override
    public String toString() {
        return "Order_DetailsViewModels{" +"\n"+
                "ID='" + ID + '\'' +"\n"+
                ", OrderID='" + OrderID + '\'' +"\n"+
                ", ProductID='" + ProductID + '\'' +"\n"+
                ", Quantity='" + Quantity + '\'' +"\n"+
                ", UnitPrice='" + UnitPrice + '\'' +"\n"+
                ", Discount='" + Discount + '\'' +"\n"+
                ", StatusID='" + StatusID + '\'' +"\n"+
                ", DateAllocated='" + DateAllocated + '\'' +"\n"+
                ", PurchaseOrderID='" + PurchaseOrderID + '\'' +"\n"+
                ", InventoryID='" + InventoryID + '\'' +"\n"+
                ", ProductName='" + ProductName + '\'' +"\n"+
                ", SumPrice=" + SumPrice +"\n"+
                ", SumOff=" + SumOff +"\n"+
                '}';
    }
}
