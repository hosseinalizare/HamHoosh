package com.example.koohestantest1.classDirectory;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SendProduct implements Parcelable{
    private String Token;
    private String UserID;
    private String CompanyID;
    private String SupplierID;
    private String ProductID;
    private String ProductName;
    private String Description;
    private int StandardCost;
    private int ListPrice;
    private int ReorderLevel;
    private int TargetLevel;
    private String Unit;
    private String QuantityPerUnit;
    private int Discontinued;
    private int MinimumReorderQuantity;
    private String Category;
    private boolean Show;
    private boolean Deleted;
    private String Spare1;
    private String Spare2;
    private String Spare3;
    private int ProductType;
    private boolean ActiveLike;
    private boolean ActiveComment;
    private boolean ActiveSave;
    private String CreatorUserID;
    private String LinkOut;
    private String LinkToInstagram;
    private boolean ChatWhitCreator;
    private List<ProductPropertisClass> productPropertis;

    public SendProduct(String token, String userID, String companyID, String supplierID,
                       String productID, String productName, String description, int standardCost,
                       int listPrice, int reorderLevel, int targetLevel, String unit,
                       String quantityPerUnit, int discontinued, int minimumReorderQuantity,
                       String category, boolean show, boolean deleted, String spare1, String spare2,
                       String spare3, int productType, boolean activeLike, boolean activeComment,
                       boolean activeSave, String creatorUserID, String linkOut,
                       String linkToInstagram, boolean chatWhitCreator,
                       List<ProductPropertisClass> productPropertis) {
        Token = token;
        UserID = userID;
        CompanyID = companyID;
        SupplierID = supplierID;
        ProductID = productID;
        ProductName = productName;
        Description = description;
        StandardCost = standardCost;
        ListPrice = listPrice;
        ReorderLevel = reorderLevel;
        TargetLevel = targetLevel;
        Unit = unit;
        QuantityPerUnit = quantityPerUnit;
        Discontinued = discontinued;
        MinimumReorderQuantity = minimumReorderQuantity;
        Category = category;
        Show = show;
        Deleted = deleted;
        Spare1 = spare1;
        Spare2 = spare2;
        Spare3 = spare3;
        ProductType = productType;
        ActiveLike = activeLike;
        ActiveComment = activeComment;
        ActiveSave = activeSave;
        CreatorUserID = creatorUserID;
        LinkOut = linkOut;
        LinkToInstagram = linkToInstagram;
        ChatWhitCreator = chatWhitCreator;
        this.productPropertis = productPropertis;
    }

    protected SendProduct(Parcel in) {
        Token = in.readString();
        UserID = in.readString();
        CompanyID = in.readString();
        SupplierID = in.readString();
        ProductID = in.readString();
        ProductName = in.readString();
        Description = in.readString();
        StandardCost = in.readInt();
        ListPrice = in.readInt();
        ReorderLevel = in.readInt();
        TargetLevel = in.readInt();
        Unit = in.readString();
        QuantityPerUnit = in.readString();
        Discontinued = in.readInt();
        MinimumReorderQuantity = in.readInt();
        Category = in.readString();
        Show = in.readByte() != 0;
        Deleted = in.readByte() != 0;
        Spare1 = in.readString();
        Spare2 = in.readString();
        Spare3 = in.readString();
        ProductType = in.readInt();
        ActiveLike = in.readByte() != 0;
        ActiveComment = in.readByte() != 0;
        ActiveSave = in.readByte() != 0;
        CreatorUserID = in.readString();
        LinkOut = in.readString();
        LinkToInstagram = in.readString();
        ChatWhitCreator = in.readByte() != 0;
    }

    public static final Creator<SendProduct> CREATOR = new Creator<SendProduct>() {
        @Override
        public SendProduct createFromParcel(Parcel in) {
            return new SendProduct(in);
        }

        @Override
        public SendProduct[] newArray(int size) {
            return new SendProduct[size];
        }
    };

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(String companyID) {
        CompanyID = companyID;
    }

    public String getSupplierID() {
        return SupplierID;
    }

    public void setSupplierID(String supplierID) {
        SupplierID = supplierID;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getStandardCost() {
        return StandardCost;
    }

    public void setStandardCost(int standardCost) {
        StandardCost = standardCost;
    }

    public int getListPrice() {
        return ListPrice;
    }

    public void setListPrice(int listPrice) {
        ListPrice = listPrice;
    }

    public int getReorderLevel() {
        return ReorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        ReorderLevel = reorderLevel;
    }

    public int getTargetLevel() {
        return TargetLevel;
    }

    public void setTargetLevel(int targetLevel) {
        TargetLevel = targetLevel;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getQuantityPerUnit() {
        return QuantityPerUnit;
    }

    public void setQuantityPerUnit(String quantityPerUnit) {
        QuantityPerUnit = quantityPerUnit;
    }

    public int getDiscontinued() {
        return Discontinued;
    }

    public void setDiscontinued(int discontinued) {
        Discontinued = discontinued;
    }

    public int getMinimumReorderQuantity() {
        return MinimumReorderQuantity;
    }

    public void setMinimumReorderQuantity(int minimumReorderQuantity) {
        MinimumReorderQuantity = minimumReorderQuantity;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public boolean isShow() {
        return Show;
    }

    public void setShow(boolean show) {
        Show = show;
    }

    public boolean isDeleted() {
        return Deleted;
    }

    public void setDeleted(boolean deleted) {
        Deleted = deleted;
    }

    public String getSpare1() {
        return Spare1;
    }

    public void setSpare1(String spare1) {
        Spare1 = spare1;
    }

    public String getSpare2() {
        return Spare2;
    }

    public void setSpare2(String spare2) {
        Spare2 = spare2;
    }

    public String getSpare3() {
        return Spare3;
    }

    public void setSpare3(String spare3) {
        Spare3 = spare3;
    }

    public int getProductType() {
        return ProductType;
    }

    public void setProductType(int productType) {
        ProductType = productType;
    }

    public boolean isActiveLike() {
        return ActiveLike;
    }

    public void setActiveLike(boolean activeLike) {
        ActiveLike = activeLike;
    }

    public boolean isActiveComment() {
        return ActiveComment;
    }

    public void setActiveComment(boolean activeComment) {
        ActiveComment = activeComment;
    }

    public boolean isActiveSave() {
        return ActiveSave;
    }

    public void setActiveSave(boolean activeSave) {
        ActiveSave = activeSave;
    }

    public String getCreatorUserID() {
        return CreatorUserID;
    }

    public void setCreatorUserID(String creatorUserID) {
        CreatorUserID = creatorUserID;
    }

    public String getLinkOut() {
        return LinkOut;
    }

    public void setLinkOut(String linkOut) {
        LinkOut = linkOut;
    }

    public String getLinkToInstagram() {
        return LinkToInstagram;
    }

    public void setLinkToInstagram(String linkToInstagram) {
        LinkToInstagram = linkToInstagram;
    }

    public boolean isChatWhitCreator() {
        return ChatWhitCreator;
    }

    public void setChatWhitCreator(boolean chatWhitCreator) {
        ChatWhitCreator = chatWhitCreator;
    }

    public List<ProductPropertisClass> getProductPropertis() {
        return productPropertis;
    }

    public void setProductPropertis(List<ProductPropertisClass> productPropertis) {
        this.productPropertis = productPropertis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Token);
        dest.writeString(UserID);
        dest.writeString(CompanyID);
        dest.writeString(SupplierID);
        dest.writeString(ProductID);
        dest.writeString(ProductName);
        dest.writeString(Description);
        dest.writeInt(StandardCost);
        dest.writeInt(ListPrice);
        dest.writeInt(ReorderLevel);
        dest.writeInt(TargetLevel);
        dest.writeString(Unit);
        dest.writeString(QuantityPerUnit);
        dest.writeInt(Discontinued);
        dest.writeInt(MinimumReorderQuantity);
        dest.writeString(Category);
        dest.writeByte((byte) (Show ? 1 : 0));
        dest.writeByte((byte) (Deleted ? 1 : 0));
        dest.writeString(Spare1);
        dest.writeString(Spare2);
        dest.writeString(Spare3);
        dest.writeInt(ProductType);
        dest.writeByte((byte) (ActiveLike ? 1 : 0));
        dest.writeByte((byte) (ActiveComment ? 1 : 0));
        dest.writeByte((byte) (ActiveSave ? 1 : 0));
        dest.writeString(CreatorUserID);
        dest.writeString(LinkOut);
        dest.writeString(LinkToInstagram);
        dest.writeByte((byte) (ChatWhitCreator ? 1 : 0));
    }
}
