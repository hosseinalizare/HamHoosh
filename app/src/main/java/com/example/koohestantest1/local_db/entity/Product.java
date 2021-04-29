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





    @ColumnInfo(name = "company_id")
    public String CompanyID;

    @ColumnInfo(name = "supplier_id")
    public String SupplierID;


    @ColumnInfo(name = "product_name")
    public String ProductName;

    @ColumnInfo(name = "description")
    public String Description;

    @ColumnInfo(name = "standard_cost")
    public int StandardCost;



    @ColumnInfo(name = "reorder_level")
    public int ReorderLevel;

    @ColumnInfo(name = "target_level")
    public int TargetLevel;

    @ColumnInfo(name = "unit")
    public String Unit;

    @ColumnInfo(name = "quantity_per_unit")
    public String QuantityPerUnit;

    @ColumnInfo(name = "discontinued")
    public int Discontinued;

    @ColumnInfo(name = "minimum_reorder_quantity")
    public int MinimumReorderQuantity;

    @ColumnInfo(name = "category")
    public String Category;

    @ColumnInfo(name = "show")
    public boolean Show;

    @ColumnInfo(name = "update_date")
    public long UpdateDate;

    @ColumnInfo(name = "deleted")
    public boolean Deleted;

    @ColumnInfo(name = "deleted1")
    public boolean Deleted1;

    @ColumnInfo(name = "viewed_count")
    public int ViewedCount;

    @ColumnInfo(name = "like_count")
    public int LikeCount;

    @ColumnInfo(name = "save_count")
    public int SaveCount;

    @ColumnInfo(name = "like_it")
    public boolean Likeit;

    @ColumnInfo(name = "save_it")
    public boolean Saveit;

    @ColumnInfo(name = "sell_count")
    public int SellCount;

    @ColumnInfo(name = "spare1")
    public String Spare1;

    @ColumnInfo(name = "spare2")
    public String Spare2;

    @ColumnInfo(name = "spare3")
    public String Spare3;

    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "product_type")
    public int ProductType;

    @ColumnInfo(name = "active_like")
    public boolean ActiveLike;

    @ColumnInfo(name = "active_comment")
    public boolean ActiveComment;

    @ColumnInfo(name = "active_save")
    public boolean ActiveSave;

    @ColumnInfo(name = "creator_user_id")
    public String CreatorUserID;

    @ColumnInfo(name = "link_out")
    public String LinkOut;

    @ColumnInfo(name = "link_to_iInstagram")
    public String LinkToInstagram;

    @ColumnInfo(name = "chat_whit_creator")
    public boolean ChatWhitCreator;

    @ColumnInfo(name = "off_price")
    public int offPrice;

    @ColumnInfo(name = "price")
    public int Price;

    @ColumnInfo(name = "show_standard_cost")
    public String ShowStandardCost;

    @ColumnInfo(name = "show_off_price")
    public String ShowoffPrice;

    @ColumnInfo(name = "show_price")
    public String ShowPrice;

}
