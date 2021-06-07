
package com.example.koohestantest1.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;
public class ProfileModel {

    @SerializedName("Bio")
    private String mBio;
    @SerializedName("CompanyId")
    private String mCompanyId;
    @SerializedName("CompanyName")
    private String mCompanyName;
    @SerializedName("Item")
    private List<Item> mItem;
    @SerializedName("UserID")
    private String mUserID;
    private String OnlineTime;
    private String BusnisePhone;
    private String SaleCount;
    private String FollowingCount;
    private String ProductCount;
    private String Addres;
    private int Rate;
    private boolean isMain;

    public String getOnlineTime() {
        return OnlineTime;
    }

    public void setOnlineTime(String onlineTime) {
        OnlineTime = onlineTime;
    }

    public String getBusnisePhone() {
        return BusnisePhone;
    }

    public void setBusnisePhone(String busnisePhone) {
        BusnisePhone = busnisePhone;
    }

    public String getSaleCount() {
        return SaleCount;
    }

    public void setSaleCount(String saleCount) {
        SaleCount = saleCount;
    }

    public String getFollowingCount() {
        return FollowingCount;
    }

    public void setFollowingCount(String followingCount) {
        FollowingCount = followingCount;
    }

    public String getProductCount() {
        return ProductCount;
    }

    public void setProductCount(String productCount) {
        ProductCount = productCount;
    }

    public String getAddres() {
        return Addres;
    }

    public void setAddres(String addres) {
        Addres = addres;
    }

    public int getRate() {
        return Rate;
    }

    public void setRate(int rate) {
        Rate = rate;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public String getBio() {
        return mBio;
    }

    public void setBio(String bio) {
        mBio = bio;
    }

    public String getCompanyId() {
        return mCompanyId;
    }

    public void setCompanyId(String companyId) {
        mCompanyId = companyId;
    }

    public String getCompanyName() {
        return mCompanyName;
    }

    public void setCompanyName(String companyName) {
        mCompanyName = companyName;
    }

    public List<Item> getItem() {
        return mItem;
    }

    public void setItem(List<Item> item) {
        mItem = item;
    }

    public String getUserID() {
        return mUserID;
    }

    public void setUserID(String userID) {
        mUserID = userID;
    }

}
