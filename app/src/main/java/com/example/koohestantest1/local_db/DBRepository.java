package com.example.koohestantest1.local_db;

import android.app.Application;

import com.example.koohestantest1.local_db.entity.NewsLetter;
import com.example.koohestantest1.local_db.entity.Product;
import com.example.koohestantest1.local_db.entity.Properties;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DBRepository {
    private LocalApi localApi;
    private Flowable<List<Product>> products;
    private Flowable<List<Properties>> propertiesList;
    private Flowable<Product> product;

    public DBRepository(Application application) {
        LocalDatabase localDatabase = LocalDatabase.getInstance(application);
        localApi = localDatabase.localApi();
        products = localApi.getProducts();
        propertiesList = localApi.getAllProperties();
    }

    public void updateProduct(Product product){
        localApi.updateProduct(product)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void updateProperties(Properties properties){
        localApi.updateProperties(properties)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void deleteAllProducts(){
        localApi.deleteAllProducts()
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void deleteAllProperties(){
        localApi.deleteAllProperties()
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void deleteOneProduct(Product product){
        localApi.deleteProduct(product)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void deleteOneProperty(Properties properties){
        localApi.deleteProperties(properties)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public Single<Long> insertProduct(Product product){
        return localApi.insertProduct(product)
                .subscribeOn(Schedulers.io());
    }

    public Single<Long> insertProperties(Properties properties){
        return localApi.insertProperties(properties)
                .subscribeOn(Schedulers.io());
    }

    public Flowable<List<Product>> getAllProducts(){
        return products;
    }

    public Flowable<List<Properties>> getAllProperties(){
        return propertiesList;
    }

    public Flowable<Integer> getProduct(String productId){
        return localApi.getProduct(productId);
    }

    public Flowable<List<Properties>> getSpecificProperties(String productId){
        return localApi.getPropertiesOfProduct(productId);
    }

    public Single<Long> insertNewsLetter(NewsLetter newsLetter){

        return localApi.insertNewsLetter(newsLetter)
                .subscribeOn(Schedulers.io());
    }

    public void deleteAllNews(){
        localApi.deleteAllNewsLetter()
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public Flowable<List<NewsLetter>> getAllNews(){
        return localApi.getAllNews();
    }
}
