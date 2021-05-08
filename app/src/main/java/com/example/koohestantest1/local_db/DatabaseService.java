package com.example.koohestantest1.local_db;

import android.content.Context;

import com.example.koohestantest1.local_db.entity.NewsLetter;
import com.example.koohestantest1.local_db.entity.Product;
import com.example.koohestantest1.local_db.entity.Properties;

import java.util.List;

import com.example.koohestantest1.classDirectory.BaseCodeClass;

public class DatabaseService {
    private LocalDatabase localDatabase;
    private Context context;
    public DatabaseService(Context context) {
        this.context = context;
        localDatabase = LocalDatabase.getInstance(context);
    }

    public void insertNewsLetter(List<NewsLetter> newsLetters){
        for(NewsLetter newsLetter:newsLetters){
            DBExecutor.getInstance().diskIO().execute(() ->
            localDatabase.localApi().insertNewsLetter(newsLetter));
        }
    }

    public long insertOneProduct(Product product){

        DBExecutor.getInstance().diskIO().execute(() ->  localDatabase.localApi().insertProduct(product));

        return BaseCodeClass.insertedItemId;
    }

    public void deleteAllProducts(){
        DBExecutor.getInstance().diskIO().execute(() -> localDatabase.localApi().deleteAllProducts());
    }

    public void deleteAllProperties(){
        DBExecutor.getInstance().diskIO().execute(() -> localDatabase.localApi().deleteAllProperties());
    }

    public void insertPropertiesList(List<Properties> propertiesList){
        DBExecutor.getInstance().diskIO().execute(() -> {
            for(Properties properties:propertiesList){
                localDatabase.localApi().insertProperties(properties);
            }
        });
    }

    public void insertProducts(List<Product> products){
        DBExecutor.getInstance().diskIO().execute(() -> {
            for(Product product:products){
                localDatabase.localApi().insertProduct(product);
            }
        });
    }

    public List<Product> getProducts(){
        BaseCodeClass.localProducts.clear();
        DBExecutor.getInstance().diskIO().execute(() ->  localDatabase.localApi().getProducts());
        return BaseCodeClass.localProducts;
    }


    public Product getOneProduct(String productId){
        DBExecutor.getInstance().diskIO().execute(() ->  localDatabase.localApi().getProduct(productId));
        return BaseCodeClass.localProduct;
    }

    public List<Properties> getPropertiesOfProduct(String productId){
        BaseCodeClass.localPropertiesList.clear();
        DBExecutor.getInstance().diskIO().execute(() ->  localDatabase.localApi().getPropertiesOfProduct(productId));

        return BaseCodeClass.localPropertiesList;
    }

    public List<Properties> getAllProperties(){
        BaseCodeClass.localPropertiesList.clear();
        DBExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                 localDatabase.localApi().getAllProperties();
            }
        });

        return BaseCodeClass.localPropertiesList;
    }

    public void updateProduct(Product product){
        DBExecutor.getInstance().diskIO().execute(() -> localDatabase.localApi().updateProduct(product));
    }

    public void updateProperties(Properties properties){
        DBExecutor.getInstance().diskIO().execute(() -> localDatabase.localApi().updateProperties(properties));
    }
}
