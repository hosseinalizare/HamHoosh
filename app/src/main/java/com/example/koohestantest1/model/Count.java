package com.example.koohestantest1.model;

import com.google.gson.annotations.SerializedName;

public class Count {
    @SerializedName("ComapnyOrderCounter")
    private CountsDetail companyDetails;

    @SerializedName("UserOrderCounter")
    private CountsDetail userDetails;


    public Count(CountsDetail userDetails, CountsDetail companyDetails) {
        this.userDetails = userDetails;
        this.companyDetails = companyDetails;
    }

    public CountsDetail getUserDetails() {
        return userDetails;
    }

    public CountsDetail getCompanyDetails() {
        return companyDetails;
    }
}
