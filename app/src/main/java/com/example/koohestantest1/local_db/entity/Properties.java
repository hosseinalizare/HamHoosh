package com.example.koohestantest1.local_db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "properties",
foreignKeys  ={@ForeignKey(entity = Product.class,
        parentColumns = "product_id",childColumns = "p_id",
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE)})
public class Properties {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "p_id")
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
