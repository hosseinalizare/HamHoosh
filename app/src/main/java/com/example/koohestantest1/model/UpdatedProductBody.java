package com.example.koohestantest1.model;

public class UpdatedProductBody {
    String CompanyID;
    String UserID;
    long UpdateDate;

    public UpdatedProductBody(String companyID, String userID, long updateDate) {
        CompanyID = companyID;
        UserID = userID;
        UpdateDate = updateDate;
    }
}
