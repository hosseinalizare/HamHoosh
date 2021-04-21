package com.example.koohestantest1.classDirectory;

public class SendHashtagClass {

    private String Value;
    private String OrderType;
    private String CompanyId;

    public SendHashtagClass(String value, String orderType) {
        Value = value;
        OrderType = orderType;
    }

    public SendHashtagClass(String value, String orderType, String companyId) {
        Value = value;
        OrderType = orderType;
        CompanyId = companyId;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getCompanyId() {
        return CompanyId;
    }
}
