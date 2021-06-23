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
    @Query("SELECT * FROM products WHERE deleted <> 1 AND deleted1 <> 1 AND show <> 0")
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

    @Query("SELECT * FROM products WHERE is_bulletin=:isBulletin AND deleted <> 1 AND deleted1 <> 1 AND show <> 0")
    Flowable<List<Product>> getBulletinProduct(boolean isBulletin);

    @Update
    Completable addToCard(Product product);

    @Query("SELECT * FROM products WHERE is_particular=:isParticular AND deleted <> 1 AND deleted1 <> 1 AND show <> 0")
    Flowable<List<Product>> getParticularProduct(boolean isParticular);

    @Query("SELECT * FROM products WHERE card_item_count > 0 AND deleted <> 1 AND deleted1 <> 1 AND show <> 0")
    Flowable<List<Product>> getAddedToCard();

    @Query("SELECT * FROM products WHERE sub_cat1 LIKE :subCat1 AND deleted <> 1 AND deleted1 <> 1 AND show <> 0")
    Flowable<List<Product>> getSubCat1Product(String subCat1);

    @Query("SELECT * FROM products WHERE sub_cat2 LIKE :subCat2 AND deleted <> 1 AND deleted1 <> 1 AND show <> 0")
    Flowable<List<Product>> getSubCat2Product(String subCat2);

    @Query("SELECT * FROM products WHERE brand LIKE :brand AND deleted <> 1 AND deleted1 <> 1 AND show <> 0")
    Flowable<List<Product>> getBrandProduct(String brand);

    @Query("SELECT * FROM products WHERE deleted <> 1 AND deleted1 <> 1 AND show <> 0 ORDER BY sell_count DESC")
    Flowable<List<Product>> getProductOrderBySell();

    @Query("SELECT * FROM products WHERE deleted <> 1 AND deleted1 <> 1 AND show <> 0 ORDER BY viewed_count DESC")
    Flowable<List<Product>> getProductOrderByViewCount();

    @Query("SELECT * FROM products WHERE deleted <> 1 AND deleted1 <> 1 AND show <> 0 ORDER BY price DESC")
    Flowable<List<Product>> getProductOrderByPriceDESC();

    @Query("SELECT * FROM products WHERE deleted <> 1 AND deleted1 <> 1 AND show <> 0 ORDER BY price ASC")
    Flowable<List<Product>> getProductOrderByPriceASC();

    @Query("SELECT * FROM products WHERE deleted <> 1 AND deleted1 <> 1 AND show <> 0 ORDER BY update_date DESC")
    Flowable<List<Product>> getProductOrderByNewest();

    @Query("SELECT * FROM products WHERE deleted <> 1 AND deleted1 <> 1 AND show <> 0 AND save_it <> 0")
    Flowable<List<Product>> getBookmarkedProduct();

    @Query("SELECT * FROM products WHERE deleted <> 1 AND deleted1 <> 1 AND show <> 0 AND off_price <> 0")
    Flowable<List<Product>> getOffProduct();




    @Query("SELECT * FROM products WHERE deleted <> 1 AND deleted1 <> 1 AND show <> 0 AND sub_cat2 LIKE :value ORDER BY sell_count DESC")
    Flowable<List<Product>> getProductOrderBySell(String value);

    @Query("SELECT * FROM products WHERE deleted <> 1 AND deleted1 <> 1 AND show <> 0 AND sub_cat2 LIKE :value ORDER BY viewed_count DESC")
    Flowable<List<Product>> getProductOrderByViewCount(String value);

    @Query("SELECT * FROM products WHERE deleted <> 1 AND deleted1 <> 1 AND show <> 0 AND sub_cat2 LIKE :value ORDER BY price DESC")
    Flowable<List<Product>> getProductOrderByPriceDESC(String value);

    @Query("SELECT * FROM products WHERE deleted <> 1 AND deleted1 <> 1 AND show <> 0 AND sub_cat2 LIKE :value ORDER BY price ASC")
    Flowable<List<Product>> getProductOrderByPriceASC(String value);

    @Query("SELECT * FROM products WHERE deleted <> 1 AND deleted1 <> 1 AND show <> 0 AND sub_cat2 LIKE :value ORDER BY update_date DESC")
    Flowable<List<Product>> getProductOrderByNewest(String value);

    @Query("SELECT * FROM products WHERE deleted <> 1 AND deleted1 <> 1 AND show <> 0 AND sub_cat2 LIKE :value AND save_it <> 0")
    Flowable<List<Product>> getBookmarkedProduct(String value);

    @Query("SELECT * FROM products WHERE deleted <> 1 AND deleted1 <> 1 AND show <> 0 AND sub_cat2 LIKE :value AND off_price <> 0")
    Flowable<List<Product>> getOffProduct(String value);

    @Query("SELECT MAX(update_date) from products")
    Flowable<Long> getProductUpdateDate();

    @Query("SELECT COUNT(*) FROM products WHERE card_item_count > 0 AND deleted <> 1 AND deleted1 <> 1 AND show <> 0")
        Flowable<Integer> getCardItemCount();

    @Query("UPDATE products SET card_item_count=0,add_to_card=0 WHERE product_id=:pid")
    Completable updateCardItem(String pid);

    @Query("UPDATE products SET card_item_count=:qy WHERE product_id=:pid")
    Completable updateCardItemCount(int qy,String pid);

    @Query("SELECT card_item_count FROM products WHERE product_id=:pid")
    Flowable<Integer> getSpecificCardItemCount(String pid);

    @Query("UPDATE products SET discontinued=:qy WHERE product_id=:pid")
    Completable updateProductDiscontinued(int qy,String pid);

    @Query("UPDATE products SET product_name=:pname WHERE product_id=:pid")
    Completable updateProductName(String pname,String pid);

    @Query("UPDATE products SET standard_cost = :standardCost,price=:standardCost,show_standard_cost=:showStandardCost" +
            ",show_price=:showStandardCost WHERE product_id=:pid")
    Completable updateProductPrice(int standardCost,String showStandardCost,String pid);

    @Query("DELETE FROM products WHERE product_id=:pid")
    Completable deleteProduct(String pid);

    @Query("SELECT * FROM products WHERE discontinued=0")
    Flowable<List<Product>> getDiscontinuedProduct();
}
