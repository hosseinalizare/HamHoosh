package com.example.koohestantest1.ViewModels;

public class PostLikeViewModel {

    private  String UserID;
    private String ProductID;
    private String Liked;

    public PostLikeViewModel(String userID, String productID, String liked) {
        UserID = userID;
        ProductID = productID;
        Liked = liked;
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

    public String getLiked() {
        return Liked;
    }

    public void setLiked(String liked) {
        Liked = liked;
    }


}
