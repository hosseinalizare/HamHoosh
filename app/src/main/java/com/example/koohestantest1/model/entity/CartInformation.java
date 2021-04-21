package com.example.koohestantest1.model.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "cart")
public class CartInformation {
    @PrimaryKey(autoGenerate = true)
    private long cartId;

    private boolean closed;
    private String orderID;
    private String token;
    private String userID;
    private String employeeID;
    private String customerID;
    private String companyID;
    private String orderDate;
    private String shippedDate;
    private String shipperID;
    private String shipName;
    private String shipAddress;
    private String shipCity;
    private String shipStateProvince;
    private String shipZIPPostalCode;
    private String shipCountryRegion;
    private String shippingFee;
    private String taxes;
    private String paymentType;
    private String paidDate;
    private String notes;
    private String taxRate;
    private String taxStatus;
    private String statusID;
    private String updateDate;
    private String deleted;
    private String spare1;
    private String spare2;
    private String spare3;
    private String sumPrice;

    public CartInformation(long cartId, boolean closed, String orderID, String token, String userID, String employeeID, String customerID, String companyID, String orderDate, String shippedDate, String shipperID, String shipName, String shipAddress, String shipCity, String shipStateProvince, String shipZIPPostalCode, String shipCountryRegion, String shippingFee, String taxes, String paymentType,
                           String paidDate, String notes, String taxRate, String taxStatus, String statusID, String updateDate, String deleted, String spare1, String spare2, String spare3, String sumPrice) {
        this.cartId = cartId;
        this.closed = closed;
        this.orderID = orderID;
        this.token = token;
        this.userID = userID;
        this.employeeID = employeeID;
        this.customerID = customerID;
        this.companyID = companyID;
        this.orderDate = orderDate;
        this.shippedDate = shippedDate;
        this.shipperID = shipperID;
        this.shipName = shipName;
        this.shipAddress = shipAddress;
        this.shipCity = shipCity;
        this.shipStateProvince = shipStateProvince;
        this.shipZIPPostalCode = shipZIPPostalCode;
        this.shipCountryRegion = shipCountryRegion;
        this.shippingFee = shippingFee;
        this.taxes = taxes;
        this.paymentType = paymentType;
        this.paidDate = paidDate;
        this.notes = notes;
        this.taxRate = taxRate;
        this.taxStatus = taxStatus;
        this.statusID = statusID;
        this.updateDate = updateDate;
        this.deleted = deleted;
        this.spare1 = spare1;
        this.spare2 = spare2;
        this.spare3 = spare3;
        this.sumPrice = sumPrice;
    }

    public long getCartId() {
        return cartId;
    }

    public boolean isClosed() {
        return closed;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getToken() {
        return token;
    }

    public String getUserID() {
        return userID;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getCompanyID() {
        return companyID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getShippedDate() {
        return shippedDate;
    }

    public String getShipperID() {
        return shipperID;
    }

    public String getShipName() {
        return shipName;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public String getShipCity() {
        return shipCity;
    }

    public String getShipStateProvince() {
        return shipStateProvince;
    }

    public String getShipZIPPostalCode() {
        return shipZIPPostalCode;
    }

    public String getShipCountryRegion() {
        return shipCountryRegion;
    }

    public String getShippingFee() {
        return shippingFee;
    }

    public String getTaxes() {
        return taxes;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public String getNotes() {
        return notes;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public String getTaxStatus() {
        return taxStatus;
    }

    public String getStatusID() {
        return statusID;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public String getDeleted() {
        return deleted;
    }

    public String getSpare1() {
        return spare1;
    }

    public String getSpare2() {
        return spare2;
    }

    public String getSpare3() {
        return spare3;
    }

    public String getSumPrice() {
        return sumPrice;
    }
}
