package com.example.koohestantest1.classDirectory;

import com.example.koohestantest1.local_db.entity.Product;
import com.example.koohestantest1.local_db.entity.Properties;

import java.util.ArrayList;
import java.util.List;

public class SendProductClass {
    private String Token;
    private String UserID;
    private String CompanyID;
    private String SupplierID;
    private String ProductID;
    private String ProductName;
    private String Description;
    private String StandardCost;
    private String ListPrice; //Kharid Kolli
    private String ReorderLevel; //Star of product number 0 - 255
    private String TargetLevel; // 0 - ~
    private String Unit; //kg || m || ...
    private String QuantityPerUnit; //
    private int Discontinued; //No. of Mojodi
    private String MinimumReorderQuantity; //?
    private String Category; // no
    private String Show; //
    private String UpdateDate; //?
    private String Deleted; //?
    private String ViewedCount;
    private String LikeCount;
    private String SaveCount;
    private String Saveit;
    private int SellCount;
    private List<ProductPropertisClass> productPropertis;

    private String Likeit;
    public SendProductClass(Product product, List<Properties> properties){
        CompanyID = product.CompanyID;
        SupplierID = product.SupplierID;
        ProductID = product.ProductID;
        ProductName = product.ProductName;
        Description = product.Description;
        StandardCost = product.StandardCost;
        ListPrice = product.ListPrice;
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
        ViewedCount = product.ViewedCount;
        LikeCount = product.LikeCount;
        SaveCount = product.SaveCount;
        Likeit = product.Likeit;
        Saveit = product.Saveit;
        productPropertis = new ArrayList<>();
        for(Properties p:properties){
            ProductPropertisClass productPropertis = new ProductPropertisClass(p.ProductID,p.PropertiesGroup,p.PropertiesName,p.PropertiesValue,p.UpdateTime);
            this.productPropertis.add(productPropertis);
        }
    }

    public SendProductClass(String token, String userID, String companyID, String supplierID, String productID,
                            String productName, String description, String standardCost, String listPrice, String reorderLevel,
                            String targetLevel, String unit, String quantityPerUnit, int discontinued, String minimumReorderQuantity,
                            String category, String show, String updateDate, String deleted, String viewedCount, String likeCount,
                            String saveCount, String likeit, String saveit, List<ProductPropertisClass> productPropertis) {
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
        UpdateDate = updateDate;
        Deleted = deleted;
        ViewedCount = viewedCount;
        LikeCount = likeCount;
        SaveCount = saveCount;
        Likeit = likeit;
        Saveit = saveit;
        this.productPropertis = productPropertis;
    }

    public void setDeleted(String deleted) {
        Deleted = deleted;
    }

    public String getToken() {
        return Token;
    }

    public String getUserID() {
        return UserID;
    }

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

    public String getStandardCost() {
        return StandardCost;
    }

    public String getListPrice() {
        return ListPrice;
    }

    public String getReorderLevel() {
        return ReorderLevel;
    }

    public String getTargetLevel() {
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

    public String getMinimumReorderQuantity() {
        return MinimumReorderQuantity;
    }

    public String getCategory() {
        return Category;
    }

    public String getShow() {
        return Show;
    }

    public String getUpdateDate() {
        return UpdateDate;
    }

    public String getDeleted() {
        return Deleted;
    }

    public String getViewedCount() {
        return ViewedCount;
    }

    public String getLikeCount() {
        return LikeCount;
    }

    public String getSaveCount() {
        return SaveCount;
    }

    public String getLikeit() {
        return Likeit;
    }

    public void setLikeit(String likeit) {
        Likeit = likeit;
    }

    public void setLikeCount(String likeCount) {
        LikeCount = likeCount;
    }

    public void setStandardCost(String standardCost) {
        StandardCost = standardCost;
    }
    public  void  setListPrice(String listPrice)
    {
        ListPrice = listPrice;
    }

    public String getSaveit() {
        return Saveit;
    }

    public void setSaveit(String saveit) {
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
