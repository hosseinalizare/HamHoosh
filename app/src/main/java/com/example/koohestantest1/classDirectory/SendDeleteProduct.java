package com.example.koohestantest1.classDirectory;

public class SendDeleteProduct {
    private String Token;
    private String UserID;
    private String CompanyID;
    private String ProductID;

    public SendDeleteProduct(String token, String userID, String companyID, String productID) {
        Token = token;
        UserID = userID;
        CompanyID = companyID;
        ProductID = productID;
    }
}
