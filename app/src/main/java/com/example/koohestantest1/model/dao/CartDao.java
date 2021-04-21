package com.example.koohestantest1.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.koohestantest1.model.entity.CartInformation;
import com.example.koohestantest1.model.entity.CartProduct;
import com.example.koohestantest1.model.entity.CartWithProduct;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface CartDao {
    @Insert
    Single<Long> insertCart(CartInformation cartInformation);

    @Insert
    Single<Long> insertProductToCart(CartProduct cartProduct);

    @Insert
    Single<Long[]> insertAllProductToCart(List<CartProduct> cartProducts);

    @Update
    Completable updateProductInCart(CartProduct cartProduct);

    @Delete
    Completable deleteProductInCart(CartProduct cartProduct);

    @Update
    Completable updateCart(CartInformation cartInformation);

    @Transaction
    @Query("SELECT * FROM cart")
    Flowable<List<CartWithProduct>> DATA();

    @Query("DELETE FROM cart")
    Completable deleteAllCart();

    @Query("SELECT COUNT(*) FROM cart")
    Flowable<Integer> getCartCount();

    @Query("SELECT COUNT(*) FROM CartProduct")
    Flowable<Integer> getProductsSize();

    @Query("SELECT * FROM CartProduct WHERE productIDString LIKE :pid")
    Flowable<CartProduct> getSpecificProduct(String pid);

    @Query("DELETE FROM CartProduct ")
    Completable deleteAllProducts();
}
