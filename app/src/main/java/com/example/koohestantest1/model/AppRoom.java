package com.example.koohestantest1.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.koohestantest1.model.dao.CartDao;
import com.example.koohestantest1.model.entity.CartInformation;
import com.example.koohestantest1.model.entity.CartProduct;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {CartInformation.class , CartProduct.class} , version = 1 , exportSchema = false)
public abstract class AppRoom extends RoomDatabase {
    public abstract CartDao cartDao();

    private static volatile AppRoom INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    public static AppRoom getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppRoom.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppRoom.class, "cart_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
