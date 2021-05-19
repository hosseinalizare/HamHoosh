package com.example.koohestantest1.local_db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "news_letter_image")
public class NewsLetterImage {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "news_id")
    public String newsId;

    @ColumnInfo(name = "src")
    public String src;

}
