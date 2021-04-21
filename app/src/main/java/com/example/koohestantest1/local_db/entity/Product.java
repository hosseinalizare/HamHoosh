package com.example.koohestantest1.local_db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class Product {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "product_id")
    public String ProductID;

    @ColumnInfo(name = "token")
    public String Token;

    @ColumnInfo(name = "user_id")
    public String UserID;

    @ColumnInfo(name = "company_id")
    public String CompanyID;

    @ColumnInfo(name = "supplier_id")
    public String SupplierID;



    @ColumnInfo(name = "product_name")
    public String ProductName;

    @ColumnInfo(name = "description")
    public String Description;

    @ColumnInfo(name = "standard_cost")
    public String StandardCost;

    @ColumnInfo(name = "list_price")
    public String ListPrice;

    @ColumnInfo(name = "reorder_level")
    public String ReorderLevel;

    @ColumnInfo(name = "target_level")
    public String TargetLevel;

    @ColumnInfo(name = "unit")
    public String Unit;

    @ColumnInfo(name = "quantity_per_unit")
    public String QuantityPerUnit;

    @ColumnInfo(name = "discontinued")
    public int Discontinued;

    @ColumnInfo(name = "minimum_reorder_quantity")
    public String MinimumReorderQuantity;

    @ColumnInfo(name = "category")
    public String Category;

    @ColumnInfo(name = "show")
    public String Show;

    @ColumnInfo(name = "update_date")
    public String UpdateDate;

    @ColumnInfo(name = "deleted")
    public String Deleted;

    @ColumnInfo(name = "viewed_count")
    public String ViewedCount;

    @ColumnInfo(name = "like_count")
    public String LikeCount;

    @ColumnInfo(name = "save_count")
    public String SaveCount;

    @ColumnInfo(name = "like_it")
    public String Likeit;

    @ColumnInfo(name = "save_it")
    public String Saveit;

    @ColumnInfo(name = "sell_count")
    public int SellCount;

}
