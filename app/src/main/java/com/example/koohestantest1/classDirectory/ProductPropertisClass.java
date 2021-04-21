package com.example.koohestantest1.classDirectory;

public class ProductPropertisClass {
    private String ProductID;
    private String PropertisGroup;
    private String PropertisName;
    private String PropertisValue;
    private String UpdatTime;

    public ProductPropertisClass(String productID, String propertisGroup, String propertisName, String propertisValue, String updatTime) {
        ProductID = productID;
        PropertisGroup = propertisGroup;
        PropertisName = propertisName;
        PropertisValue = propertisValue;
        UpdatTime = updatTime;
    }

    public String getProductID() {
        return ProductID;
    }

    public String getPropertisGroup() {
        return PropertisGroup;
    }

    public String getPropertisName() {
        return PropertisName;
    }

    public String getPropertisValue() {
        return PropertisValue;
    }

    public String getUpdatTime() {
        return UpdatTime;
    }
}
