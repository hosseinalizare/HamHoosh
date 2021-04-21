package com.example.koohestantest1.classDirectory;

public class GetPropertisOfCompanyProducts {
    private String id;
    private  String CompanyType;
    private String PropertisGroup;
    private  String PropertisName;
    private  String PropertisValue;

    public GetPropertisOfCompanyProducts(String id, String companyType, String propertisGroup, String propertisName, String propertisValue) {
        this.id = id;
        CompanyType = companyType;
        PropertisGroup = propertisGroup;
        PropertisName = propertisName;
        PropertisValue = propertisValue;
    }

    public String getId() {
        return id;
    }

    public String getCompanyType() {
        return CompanyType;
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
}
