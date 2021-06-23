package com.example.koohestantest1.classDirectory;

import com.example.koohestantest1.local_db.entity.Product;
import com.example.koohestantest1.local_db.entity.Properties;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ReceiveProductClass {
    //private String Token;
    //private String UserID;
    private String CompanyID;
    private String SupplierID;
    private String ProductID;
    private String ProductName;
    private String Description;
    @SerializedName("StandardPric")
    private StandardPrice StandardCost;
    //private String ListPrice; //Kharid Kolli
    private int ReorderLevel; //Star of product number 0 - 255
    private int TargetLevel; // 0 - ~
    private String Unit; //kg || m || ...
    private String QuantityPerUnit; //
    private int Discontinued; //No. of Mojodi
    private int MinimumReorderQuantity; //?
    private String Category; // no
    private boolean Show; //
    private long UpdateDate; //?
    private boolean Deleted; //?
    private boolean Deleted1;
    private int ViewedCount;
    private int LikeCount;
    private int SaveCount;
    private boolean Saveit;
    private int SellCount;
    private String Spare1;
    private String Spare2;
    private String Spare3;
    private int id;
    private int ProductType;
    private boolean ActiveLike;
    private boolean ActiveComment;
    private boolean ActiveSave;
    private String CreatorUserID;
    private String LinkOut;
    private String LinkToInstagram;
    private boolean ChatWhitCreator;
    private String SubCat1;
    private String SubCat2;
    private boolean IsParticular;
    private boolean IsBulletin;
    private boolean AddToCard;
    private String Brand;
    private String MainCategory;
    private String companyName;

    private List<ProductPropertisClass> productPropertis;

    private boolean Likeit;
    public ReceiveProductClass(Product product, List<Properties> properties, StandardPrice standardPrice){
        CompanyID = product.CompanyID;
        SupplierID = product.SupplierID;
        ProductID = product.ProductID;
        ProductName = product.ProductName;
        Description = product.Description;
        StandardCost = standardPrice;
        //ListPrice = product.ListPrice;
        ReorderLevel = product.ReorderLevel;
        TargetLevel = product.TargetLevel;
        Unit = product.Unit;
        QuantityPerUnit = product.QuantityPerUnit;
        Discontinued = product.Discontinued;
        MinimumReorderQuantity = product.MinimumReorderQuantity;
        Category = product.Category;
        Show = product.Show;
        UpdateDate = product.UpdateDate;
        Deleted = product.Deleted;
        Deleted1 = product.Deleted1;
        ViewedCount = product.ViewedCount;
        LikeCount = product.LikeCount;
        SaveCount = product.SaveCount;
        Likeit = product.Likeit;
        Saveit = product.Saveit;
        Spare1 = product.Spare1;
        Spare2 = product.Spare2;
        Spare3 = product.Spare3;
        id = product.id;
        ActiveLike = product.ActiveLike;
        ProductType = product.ProductType;
        ActiveComment = product.ActiveComment;
        ActiveSave = product.ActiveSave;
        CreatorUserID = product.CreatorUserID;
        LinkOut = product.LinkOut;
        LinkToInstagram = product.LinkToInstagram;
        ChatWhitCreator = product.ChatWhitCreator;
        productPropertis = new ArrayList<>();
        for(Properties p:properties){
            ProductPropertisClass productPropertis = new ProductPropertisClass(p.ProductID,p.PropertiesGroup,p.PropertiesName,p.PropertiesValue,p.UpdateTime);
            this.productPropertis.add(productPropertis);
        }
    }

    public ReceiveProductClass(String companyID, String supplierID, String productID,
                               String productName, String description, StandardPrice standardCost, int reorderLevel,
                               int targetLevel, String unit, String quantityPerUnit, int discontinued, int minimumReorderQuantity,
                               String category, boolean show, long updateDate, boolean deleted, boolean deleted1, int viewedCount, int likeCount,
                               int saveCount, boolean likeit, boolean saveit, String spare1, String spare2, String spare3, int id,
                               int productType, boolean activeLike, boolean activeComment, boolean activeSave,
                               String creatorUserID, String linkOut, String linkToInstagram, boolean chatWhitCreator, List<ProductPropertisClass> productPropertis) {
//        Token = token;
//        UserID = userID;
        CompanyID = companyID;
        SupplierID = supplierID;
        ProductID = productID;
        ProductName = productName;
        Description = description;
        StandardCost = standardCost;
        //ListPrice = listPrice;
        ReorderLevel = reorderLevel;
        TargetLevel = targetLevel;
        Unit = unit;
        QuantityPerUnit = quantityPerUnit;
        Discontinued = discontinued;
        MinimumReorderQuantity = minimumReorderQuantity;
        Category = category;
        Show = show;
        UpdateDate = updateDate;
        Deleted = deleted;
        Deleted1 = deleted1;
        ViewedCount = viewedCount;
        LikeCount = likeCount;
        SaveCount = saveCount;
        Likeit = likeit;
        Saveit = saveit;
        Spare1 = spare1;
        Spare2 = spare2;
        Spare3 = spare3;
        LinkOut = linkOut;
        LinkToInstagram = linkToInstagram;
        this.id = id;
        ProductType = productType;
        ActiveLike = activeLike;
        ActiveComment = activeComment;
        ActiveSave = activeSave;
        CreatorUserID = creatorUserID;
        ChatWhitCreator = chatWhitCreator;
        this.productPropertis = productPropertis;
    }

    public String getSubCat1() {
        return SubCat1;
    }

    public void setSubCat1(String subCat1) {
        SubCat1 = subCat1;
    }

    public String getSubCat2() {
        return SubCat2;
    }

    public void setSubCat2(String subCat2) {
        SubCat2 = subCat2;
    }

    public boolean isParticular() {
        return IsParticular;
    }

    public void setParticular(boolean particular) {
        IsParticular = particular;
    }

    public boolean isBulletin() {
        return IsBulletin;
    }

    public void setBulletin(boolean bulletin) {
        IsBulletin = bulletin;
    }

    public boolean isAddToCard() {
        return AddToCard;
    }

    public void setAddToCard(boolean addToCard) {
        AddToCard = addToCard;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getMainCategory() {
        return MainCategory;
    }

    public void setMainCategory(String mainCategory) {
        MainCategory = mainCategory;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public StandardPrice getStandardCost() {
        return StandardCost;
    }

    public void setStandardCost(StandardPrice standardCost) {
        StandardCost = standardCost;
    }

    public boolean isChatWhitCreator() {
        return ChatWhitCreator;
    }

    public void setChatWhitCreator(boolean chatWhitCreator) {
        ChatWhitCreator = chatWhitCreator;
    }

    public String getLinkToInstagram() {
        return LinkToInstagram;
    }

    public void setLinkToInstagram(String linkToInstagram) {
        LinkToInstagram = linkToInstagram;
    }

    public String getLinkOut() {
        return LinkOut;
    }

    public void setLinkOut(String linkOut) {
        LinkOut = linkOut;
    }

    public String getCreatorUserID() {
        return CreatorUserID;
    }

    public void setCreatorUserID(String creatorUserID) {
        CreatorUserID = creatorUserID;
    }

    public boolean isActiveSave() {
        return ActiveSave;
    }

    public void setActiveSave(boolean activeSave) {
        ActiveSave = activeSave;
    }

    public boolean isActiveComment() {
        return ActiveComment;
    }

    public void setActiveComment(boolean activeComment) {
        ActiveComment = activeComment;
    }

    public boolean isActiveLike() {
        return ActiveLike;
    }

    public void setActiveLike(boolean activeLike) {
        ActiveLike = activeLike;
    }

    public int getProductType() {
        return ProductType;
    }

    public void setProductType(int productType) {
        ProductType = productType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpare3() {
        return Spare3;
    }

    public void setSpare3(String spare3) {
        Spare3 = spare3;
    }

    public String getSpare2() {
        return Spare2;
    }

    public void setSpare2(String spare2) {
        Spare2 = spare2;
    }

    public String getSpare1() {
        return Spare1;
    }

    public void setSpare1(String spare1) {
        Spare1 = spare1;
    }

    public boolean isDeleted1() {
        return Deleted1;
    }

    public void setDeleted1(boolean deleted1) {
        Deleted1 = deleted1;
    }

    public void setDeleted(boolean deleted) {
        Deleted = deleted;
    }

//    public String getToken() {
//        return Token;
//    }
//
//    public String getUserID() {
//        return UserID;
//    }

    public String getCompanyID() {
        return CompanyID;
    }

    public String getSupplierID() {
        return SupplierID;
    }

    public String getProductID() {
        return ProductID;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getDescription() {
        return Description;
    }



    /*public String getListPrice() {
        return ListPrice;
    }
*/
    public int getReorderLevel() {
        return ReorderLevel;
    }

    public int getTargetLevel() {
        return TargetLevel;
    }

    public String getUnit() {
        return Unit;
    }

    public String getQuantityPerUnit() {
        return QuantityPerUnit;
    }

    public int getDiscontinued() {
        return Discontinued;
    }

    public int getMinimumReorderQuantity() {
        return MinimumReorderQuantity;
    }

    public String getCategory() {
        return Category;
    }

    public boolean getShow() {
        return Show;
    }

    public long getUpdateDate() {
        return UpdateDate;
    }

    public boolean getDeleted() {
        return Deleted;
    }

    public int getViewedCount() {
        return ViewedCount;
    }

    public int getLikeCount() {
        return LikeCount;
    }

    public int getSaveCount() {
        return SaveCount;
    }

    public boolean isLikeit() {
        return Likeit;
    }

    public void setLikeit(boolean likeit) {
        Likeit = likeit;
    }

    public void setLikeCount(int likeCount) {
        LikeCount = likeCount;
    }


    /*public  void  setListPrice(String listPrice)
    {
        ListPrice = listPrice;
    }*/

    public boolean getSaveit() {
        return Saveit;
    }

    public void setSaveit(boolean saveit) {
        Saveit = saveit;
    }

    public List<ProductPropertisClass> getProductPropertis() {
        return productPropertis;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public int getSellCount() {
        return SellCount;
    }

    public void setSellCount(int sellCount) {
        SellCount = sellCount;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public void setDiscontinued(int discontinued) {
        Discontinued = discontinued;
    }
}
