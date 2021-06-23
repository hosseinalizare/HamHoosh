package com.example.koohestantest1.ViewModels;

import java.util.ArrayList;
import java.util.List;

import com.example.koohestantest1.classDirectory.ProductPropertisClass;

public class Order_DetailsViewModels {
    private String ID;
    private String OrderID;
    private String ProductID;
    private int Quantity;
    private String UnitPrice;
    private String Discount;
    private String StatusID;
    private String DateAllocated;
    private String PurchaseOrderID;
    private String InventoryID;
    private String ProductName;
    private int SumPrice;
    private int SumOff;
    /*private String CompanyID;
    private String CompanyName;
    private String SupplierID;
    private String Description;
    private int StandardCost;
    private int ReorderLevel;
    private int TargetLevel;
    private String Unit;
    private String QuantityPerUnit;
    private int Discontinued;
    private int MinimumReorderQuantity;
    private String Category;
    private boolean Show;
    private long UpdateDate;
    private boolean Deleted;
    private boolean Deleted1;
    private int ViewedCount;
    private int LikeCount;
    private int SaveCount;
    private boolean Likeit;
    private boolean Saveit;
    private int SellCount;
    private String Spare1;
    private String Spare2;
    private String Spare3;
    private int productId;
    private int ProductType;
    private boolean ActiveLike;
    private boolean ActiveComment;
    private boolean ActiveSave;
    private String CreatorUserID;
    private String LinkOut;
    private String LinkToInstagram;
    private boolean ChatWhitCreator;
    private int offPrice;
    private int Price;
    private String ShowStandardCost;
    private String ShowoffPrice;
    private String ShowPrice;
    private boolean IsParticular;
    private boolean IsBulletin;
    private boolean AddToCard;
    private String Brand;
    private String MainCategory;
    private String SubCat1;
    private String SubCat2;
    private int CartItemCount;
*/

    private List<ProductPropertisClass> PropertisViewModels;

    public Order_DetailsViewModels(String ID, String orderID, String productID, int quantity, String unitPrice, String discount, String statusID, String dateAllocated, String purchaseOrderID, String inventoryID, String productName, List<ProductPropertisClass> propertisViewModels) {
        this.ID = ID;
        OrderID = orderID;
        ProductID = productID;
        Quantity = quantity;
        UnitPrice = unitPrice;
        Discount = discount;
        StatusID = statusID;
        DateAllocated = dateAllocated;
        PurchaseOrderID = purchaseOrderID;
        InventoryID = inventoryID;
        ProductName = productName;
        PropertisViewModels = propertisViewModels;
    }

    public Order_DetailsViewModels(){
        PropertisViewModels = new ArrayList<>();
    }

/*    public String getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(String companyID) {
        CompanyID = companyID;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getSupplierID() {
        return SupplierID;
    }

    public void setSupplierID(String supplierID) {
        SupplierID = supplierID;
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

    public long getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(long updateDate) {
        UpdateDate = updateDate;
    }

    public boolean isDeleted() {
        return Deleted;
    }

    public void setDeleted(boolean deleted) {
        Deleted = deleted;
    }

    public boolean isDeleted1() {
        return Deleted1;
    }

    public void setDeleted1(boolean deleted1) {
        Deleted1 = deleted1;
    }

    public int getViewedCount() {
        return ViewedCount;
    }

    public void setViewedCount(int viewedCount) {
        ViewedCount = viewedCount;
    }

    public int getLikeCount() {
        return LikeCount;
    }

    public void setLikeCount(int likeCount) {
        LikeCount = likeCount;
    }

    public int getSaveCount() {
        return SaveCount;
    }

    public void setSaveCount(int saveCount) {
        SaveCount = saveCount;
    }

    public boolean isLikeit() {
        return Likeit;
    }

    public void setLikeit(boolean likeit) {
        Likeit = likeit;
    }

    public boolean isSaveit() {
        return Saveit;
    }

    public void setSaveit(boolean saveit) {
        Saveit = saveit;
    }

    public int getSellCount() {
        return SellCount;
    }

    public void setSellCount(int sellCount) {
        SellCount = sellCount;
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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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

    public int getOffPrice() {
        return offPrice;
    }

    public void setOffPrice(int offPrice) {
        this.offPrice = offPrice;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getShowStandardCost() {
        return ShowStandardCost;
    }

    public void setShowStandardCost(String showStandardCost) {
        ShowStandardCost = showStandardCost;
    }

    public String getShowoffPrice() {
        return ShowoffPrice;
    }

    public void setShowoffPrice(String showoffPrice) {
        ShowoffPrice = showoffPrice;
    }

    public String getShowPrice() {
        return ShowPrice;
    }

    public void setShowPrice(String showPrice) {
        ShowPrice = showPrice;
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

    public int getCartItemCount() {
        return CartItemCount;
    }

    public void setCartItemCount(int cartItemCount) {
        CartItemCount = cartItemCount;
    }*/

    public String getID() {
        return ID;
    }

    public String getOrderID() {
        return OrderID;
    }

    public String getProductID() {
        return ProductID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public String getUnitPrice() {
        return UnitPrice;
    }

    public String getDiscount() {
        return Discount;
    }

    public String getStatusID() {
        return StatusID;
    }

    public String getDateAllocated() {
        return DateAllocated;
    }

    public String getPurchaseOrderID() {
        return PurchaseOrderID;
    }

    public String getInventoryID() {
        return InventoryID;
    }

    public String getProductName() {
        return ProductName;
    }

    public List<ProductPropertisClass> getPropertisViewModels() {
        return PropertisViewModels;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public void setUnitPrice(String unitPrice) {
        UnitPrice = unitPrice;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public void setStatusID(String statusID) {
        StatusID = statusID;
    }

    public void setDateAllocated(String dateAllocated) {
        DateAllocated = dateAllocated;
    }

    public void setPurchaseOrderID(String purchaseOrderID) {
        PurchaseOrderID = purchaseOrderID;
    }

    public void setInventoryID(String inventoryID) {
        InventoryID = inventoryID;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public void setPropertisViewModels(List<ProductPropertisClass> propertisViewModels) {
        PropertisViewModels = propertisViewModels;
    }

    public int getSumPrice() {
        return SumPrice;
    }

    public void setSumPrice(int sumPrice) {
        SumPrice = sumPrice;
    }

    public int getSumOff() {
        return SumOff;
    }

    public void setSumOff(int sumOff) {
        SumOff = sumOff;
    }

    @Override
    public String toString() {
        return "Order_DetailsViewModels{" +"\n"+
                "ID='" + ID + '\'' +"\n"+
                ", OrderID='" + OrderID + '\'' +"\n"+
                ", ProductID='" + ProductID + '\'' +"\n"+
                ", Quantity='" + Quantity + '\'' +"\n"+
                ", UnitPrice='" + UnitPrice + '\'' +"\n"+
                ", Discount='" + Discount + '\'' +"\n"+
                ", StatusID='" + StatusID + '\'' +"\n"+
                ", DateAllocated='" + DateAllocated + '\'' +"\n"+
                ", PurchaseOrderID='" + PurchaseOrderID + '\'' +"\n"+
                ", InventoryID='" + InventoryID + '\'' +"\n"+
                ", ProductName='" + ProductName + '\'' +"\n"+
                ", SumPrice=" + SumPrice +"\n"+
                ", SumOff=" + SumOff +"\n"+
                '}';
    }
}
