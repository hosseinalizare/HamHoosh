package com.example.koohestantest1.local_db;

import android.app.Application;

import com.example.koohestantest1.local_db.entity.NewsLetter;
import com.example.koohestantest1.local_db.entity.NewsLetterImage;
import com.example.koohestantest1.local_db.entity.Product;
import com.example.koohestantest1.local_db.entity.Properties;

import java.util.Date;
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

    public void updateProduct(Product product) {
        localApi.updateProduct(product)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void updateProperties(Properties properties) {
        localApi.updateProperties(properties)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void deleteAllProducts() {
        localApi.deleteAllProducts()
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void deleteAllProperties() {
        localApi.deleteAllProperties()
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void deleteOneProduct(Product product) {
        localApi.deleteProduct(product)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void deleteOneProperty(Properties properties) {
        localApi.deleteProperties(properties)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public Single<Long> insertProduct(Product product) {
        return localApi.insertProduct(product)
                .subscribeOn(Schedulers.io());
    }

    public Single<Long> insertProperties(Properties properties) {
        return localApi.insertProperties(properties)
                .subscribeOn(Schedulers.io());
    }

    public Flowable<List<Product>> getAllProducts() {
        return products;
    }

    public Flowable<List<Properties>> getAllProperties() {
        return propertiesList;
    }

    public Flowable<Integer> getProduct(String productId) {
        return localApi.getProduct(productId);
    }

    public Flowable<Product> getSpecificProduct(String productId) {
        Flowable<Product> product = localApi.getSpecificProduct(productId);
        product.blockingFirst().propertiesList = localApi.getPropertiesOfProduct(productId).blockingFirst();
        return product;
    }

    public Flowable<List<Properties>> getSpecificProperties(String productId) {
        return localApi.getPropertiesOfProduct(productId);
    }

    public Flowable<String> getLastUpdate() {
        return localApi.getLastUpdate();
    }

    public Single<Long> insertNewsLetter(NewsLetter newsLetter) {

        return localApi.insertNewsLetter(newsLetter)
                .subscribeOn(Schedulers.io());
    }

    /*public Single<Long> insertNewsImage(NewsLetterImage newsLetterImage){
        return localApi.insertImageNews(newsLetterImage)
                .subscribeOn(Schedulers.io());
    }*/

    public void updateNewsLikeValue(boolean likeIt, String newsId) {
        localApi.updateNewsLikeValue(likeIt, newsId);
    }

    public void updateNewsLetter(NewsLetter newsLetter) {
        localApi.updateNews(newsLetter)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void deleteAllNews() {
        localApi.deleteAllNewsLetter()
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public Flowable<List<NewsLetter>> getAllNews() {
        return localApi.getAllNews();
    }

    public Flowable<List<String>> getNewsImage(String newsId) {
        return localApi.getNewsImage(newsId);
    }

    public void deleteOneNews(NewsLetter newsLetter) {
        localApi.deleteNewsLetter(newsLetter)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public Flowable<List<Product>> getBulletinProduct(boolean isBulletin) {
        return localApi.getBulletinProduct(isBulletin);
    }

    public void addToCard(Product product) {
        localApi.addToCard(product)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public Flowable<List<Product>> getParticularProduct(boolean isParticular) {
        return localApi.getParticularProduct(isParticular);
    }

    public Flowable<List<Product>> getAddedToCard() {
        return localApi.getAddedToCard();
    }

    public Flowable<List<Product>> getSubCat1Product(String cat1) {
        return localApi.getSubCat1Product(cat1);
    }

    public Flowable<List<Product>> getSubCat2Product(String cat2) {
        return localApi.getSubCat2Product(cat2);
    }

    public Flowable<List<Product>> getBrandProduct(String brand) {
        return localApi.getBrandProduct(brand);
    }

    public Flowable<List<Product>> getProductOrderBySell(String value) {
        if (value.equals("همه"))
            return localApi.getProductOrderBySell();
        return localApi.getProductOrderBySell(value);


    }

    public Flowable<List<Product>> getProductOrderByViewCount(String value) {
        if (value.equals("همه"))
            return localApi.getProductOrderByViewCount();
        return localApi.getProductOrderByViewCount(value);
    }

    public Flowable<List<Product>> getProductOrderByPriceDESC(String value) {
        if (value.equals("همه"))
            return localApi.getProductOrderByPriceDESC();
        return localApi.getProductOrderByPriceDESC(value);
    }

    public Flowable<List<Product>> getProductOrderByPriceASC(String value) {
        if (value.equals("همه"))
            return localApi.getProductOrderByPriceASC();
        return localApi.getProductOrderByPriceASC(value);
    }

    public Flowable<List<Product>> getProductOrderByNewest(String value) {
        if (value.equals("همه"))
            return localApi.getProductOrderByNewest();
        return localApi.getProductOrderByNewest(value);
    }

    public Flowable<List<Product>> getBookmarkedProduct() {
        return localApi.getBookmarkedProduct();
    }

    public Flowable<List<Product>> getOffProduct(String value) {
        if (value.equals("همه"))
            return localApi.getOffProduct();
        return localApi.getOffProduct(value);
    }

    public Flowable<Long> getProductUpdateDate() {
        return localApi.getProductUpdateDate();
    }

    public Flowable<Integer> getCardItemCount() {
        return localApi.getCardItemCount();
    }

    public void updateCardItem(String pid) {
        localApi.updateCardItem(pid)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void updateCardItemCount(int qy, String pid) {
        localApi.updateCardItemCount(qy, pid)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public Flowable<Integer> getSpecificCardItemCount(String pid) {
        return localApi.getSpecificCardItemCount(pid);
    }

    public void updateProductDiscontinued(int qy,String pid){
        localApi.updateProductDiscontinued(qy, pid)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void updateProductName(String pname,String pid){
        localApi.updateProductName(pname, pid)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void updateProductPrice(int standardCost,String showStandardCost,String pid){
        localApi.updateProductPrice(standardCost,showStandardCost,pid)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void deleteProduct(String pid){
        localApi.deleteProduct(pid)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public Flowable<List<Product>> getDiscontinuedProduct(){
        return localApi.getDiscontinuedProduct();
    }

    public Flowable<List<Product>> getBulletinProduct(){
        return localApi.getBulletinProduct();
    }

    public Flowable<List<Product>> getLikeProduct(){
        return localApi.getLikeProduct();
    }

    public void deletePropertiesOneProduct(String pid){
        localApi.deletePropertiesOneProduct(pid)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void updateLocalProduct(String productName,String description,int standardCost,int orderLevel,
                                   int targetLevel,String unit,String quantityPerUnit,int discontinued,
                                   int mrq,String category,boolean show,long updateDate,String spare1,
                                   String spare2,String spare3,int offPrice,int price,String ssc,
                                   String sop,String sp,boolean ip,boolean ib,boolean atc,String brand,
                                   String mainCat,String subCat1,String subCat2,String pid){
        localApi.updateLocalProduct(productName, description, standardCost, orderLevel, targetLevel,
                unit, quantityPerUnit, discontinued, mrq, category, show, updateDate, spare1, spare2,
                spare3, offPrice, price, ssc, sop, sp, ip, ib, atc, brand, mainCat, subCat1, subCat2,
                pid)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public Flowable<String> getProductImages(String pid){
        return localApi.getProductImages(pid);
    }
}
