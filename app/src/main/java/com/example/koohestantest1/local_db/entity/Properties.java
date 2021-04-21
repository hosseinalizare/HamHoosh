package com.example.koohestantest1.local_db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "properties")
public class Properties {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "product_id")
    public String ProductID;

    @ColumnInfo(name = "properties_group")
    public String PropertiesGroup;

    @ColumnInfo(name = "properties_name")
    public String PropertiesName;

    @ColumnInfo(name = "properties_value")
    public String PropertiesValue;

    @ColumnInfo(name = "update_time")
    public String UpdateTime;
}
