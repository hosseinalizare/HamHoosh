package com.example.koohestantest1.ViewModels;

public class PostViewViewModel {
    private  String UserID;
    private String ProductID;
    private String ViweCount;

    public PostViewViewModel(String userID, String productID, String viweCount) {
        UserID = userID;
        ProductID = productID;
        ViweCount = viweCount;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getViweCount() {
        return ViweCount;
    }

    public void setViweCount(String viweCount) {
        ViweCount = viweCount;
    }
}
