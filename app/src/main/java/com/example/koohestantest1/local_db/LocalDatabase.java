package com.example.koohestantest1.local_db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.koohestantest1.local_db.entity.NewsLetter;
import com.example.koohestantest1.local_db.entity.Product;
import com.example.koohestantest1.local_db.entity.Properties;

@Database(entities = {Product.class, Properties.class, NewsLetter.class},version = 1)
public abstract class LocalDatabase extends RoomDatabase {
    private static final String DB_NAME = "hamyar_db";
    private static  LocalDatabase instance;
    private static Object LOCK = new Object();
    public static synchronized LocalDatabase getInstance(Context context){
        if(instance == null){
            synchronized (LOCK){
                instance = Room.databaseBuilder(context.getApplicationContext(),LocalDatabase.class,DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }

        return instance;
    }

    public abstract LocalApi localApi();
}
