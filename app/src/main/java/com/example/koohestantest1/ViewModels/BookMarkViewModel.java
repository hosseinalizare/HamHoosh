package com.example.koohestantest1.ViewModels;

public class BookMarkViewModel {
    private  String  UserID;
    private  String  ProductID;
    private  String  Saved;

    public BookMarkViewModel(String userID, String productID, String saved) {
        UserID = userID;
        ProductID = productID;
        Saved = saved;
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

    public String getSaved() {
        return Saved;
    }

    public void setSaved(String saved) {
        Saved = saved;
    }
}
