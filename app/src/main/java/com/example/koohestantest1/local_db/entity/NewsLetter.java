package com.example.koohestantest1.local_db.entity;



import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.asura.library.posters.Poster;

import java.util.List;

@Entity(tableName = "news_letter")
public class NewsLetter {

    @PrimaryKey
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "title")
    public String Title;

    @ColumnInfo(name = "news_id")
    public String NewsID;

    @ColumnInfo(name = "supplier_id")
    public String SupplierID;

    @ColumnInfo(name = "description")
    public String Description;

    @ColumnInfo(name = "show")
    public boolean Show;

    @ColumnInfo(name = "deleted")
    public boolean Deleted;

    @ColumnInfo(name = "deleted1")
    public boolean Deleted1;

    @ColumnInfo(name = "update_date")
    public String UpdateDate;

    @ColumnInfo(name = "spare1")
    public String Spare1;

    @ColumnInfo(name = "spare2")
    public String Spare2;

    @ColumnInfo(name = "spare3")
    public String Spare3;


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

    @ColumnInfo(name = "link_to_instagram")
    public String LinkToInstagram;

    @Ignore
    public List<String> src;

    @ColumnInfo(name = "json_src")
    public String Jsonsrc;

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

}
