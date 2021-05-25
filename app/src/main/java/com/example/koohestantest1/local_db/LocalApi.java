package com.example.koohestantest1.local_db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.koohestantest1.local_db.entity.NewsLetter;
import com.example.koohestantest1.local_db.entity.NewsLetterImage;
import com.example.koohestantest1.local_db.entity.Product;
import com.example.koohestantest1.local_db.entity.Properties;

import java.sql.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface LocalApi {
    @Query("SELECT * FROM products")
    Flowable<List<Product>> getProducts();

    @Query("SELECT * FROM products WHERE product_id=:product_id")
    Flowable<Product> getSpecificProduct(String product_id);

    @Query("SELECT * FROM properties WHERE product_id=:product_id")
    Flowable<List<Properties>> getPropertiesOfProduct(String product_id);

    @Query("SELECT * FROM properties")
    Flowable<List<Properties>> getAllProperties();

    @Query("SELECT COUNT(*) FROM products WHERE product_id=:product_id")
    Flowable<Integer> getProduct(String product_id);


    @Insert
    Single<Long> insertProduct(Product product);

    @Insert
    Single<Long> insertProperties(Properties properties);

    @Update
    Completable updateProduct(Product product);

    @Update
    Completable updateProperties(Properties properties);

    @Delete
    Completable deleteProduct(Product product);

    @Delete
    Completable deleteProperties(Properties properties);

    @Query("DELETE FROM products")
    Completable deleteAllProducts();

    @Query("DELETE FROM properties")
    Completable deleteAllProperties();

    @Query("SELECT * FROM news_letter")
    Flowable<List<NewsLetter>> getAllNews();

    @Insert
    Single<Long> insertNewsLetter(NewsLetter newsLetter);

    /*@Insert
    Single<Long> insertImageNews(NewsLetterImage newsLetterImage);*/

    @Query("DELETE FROM news_letter")
    Completable deleteAllNewsLetter();

    @Query("SELECT MAX(update_date) FROM news_letter")
    Flowable<String> getLastUpdate();

    @Query("SELECT src FROM news_letter_image WHERE news_id LIKE :newsId")
    Flowable<List<String>> getNewsImage(String newsId);

    @Query("UPDATE news_letter SET like_it=:likeIt WHERE news_id LIKE :newsId")
    Completable updateNewsLikeValue(boolean likeIt,String newsId);

    @Update
    Completable updateNews(NewsLetter newsLetter);

    @Delete
    Completable deleteNewsLetter(NewsLetter newsLetter);
}
