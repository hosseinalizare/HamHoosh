package com.example.koohestantest1.local_db;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;

import com.example.koohestantest1.local_db.entity.NewsLetter;
import com.example.koohestantest1.local_db.entity.NewsLetterImage;
import com.example.koohestantest1.local_db.entity.Product;
import com.example.koohestantest1.local_db.entity.Properties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DBViewModel extends AndroidViewModel {
    private final String TAG = DBViewModel.class.getSimpleName();
    private DBRepository repository;
    private MutableLiveData<Long> insertedProduct = new MutableLiveData<>();
    private MutableLiveData<Long> insertedProperties = new MutableLiveData<>();
    public DBViewModel(@NonNull Application application) {
        super(application);
        repository = new DBRepository(application);
    }

    public void insertProduct(Product product){
        repository.insertProduct(product).subscribe();
    }

    public void insertProperties(Properties properties){
        repository.insertProperties(properties).subscribe();
    }


    public void deleteAllProducts(){
        repository.deleteAllProducts();
    }

    public void deleteAllProperties(){
        repository.deleteAllProperties();
    }

    public void deleteOneProduct(Product product){
        repository.deleteOneProduct(product);
    }

    public void deleteOneProperty(Properties properties){
        repository.deleteOneProperty(properties);
    }

    public LiveData<List<Product>> getAllProducts(){
        return LiveDataReactiveStreams.fromPublisher(repository.getAllProducts());
    }

    public LiveData<String> getLastUpdate(){
        return LiveDataReactiveStreams.fromPublisher(repository.getLastUpdate());
    }

    public LiveData<Product> getSpecificProduct(String productId){
        return LiveDataReactiveStreams.fromPublisher(repository.getSpecificProduct(productId));
    }

    public LiveData<List<Properties>> getAllProperties(){
        return LiveDataReactiveStreams.fromPublisher(repository.getAllProperties());
    }

    public LiveData<Integer> getOneProduct(String productId){
        return LiveDataReactiveStreams.fromPublisher(repository.getProduct(productId));
    }

    public LiveData<List<Properties>> getSpecificProperties(String productId){
        return LiveDataReactiveStreams.fromPublisher(repository.getSpecificProperties(productId));
    }

    public void updateProduct(Product product){
        repository.updateProduct(product);
    }

    public void updateProperty(Properties properties){
        repository.updateProperties(properties);
    }

    @SuppressLint("CheckResult")
    public void insertNewsLetter(NewsLetter newsLetter) {
        Single<Long> a = repository.insertNewsLetter(newsLetter);
        a.observeOn(AndroidSchedulers.mainThread()).subscribeWith(new SingleObserver<Long>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@io.reactivex.annotations.NonNull Long aLong) {
                Toast.makeText(getApplication(), aLong + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                Log.d("Error", e.getMessage());
            }
        });


        /*if (newsLetter.src.size() == 0) {
            Log.d("TAG",newsLetter.Title);
        } else {
            NewsLetterImage newsLetterImage = new NewsLetterImage();
            newsLetterImage.newsId = newsLetter.NewsID;
            for (int i = 0; newsLetter.src.size() > i; i++) {

                switch (i) {
                    case 0:
                        newsLetterImage.src1 = newsLetter.src.get(i);
                        break;
                    case 1:
                        newsLetterImage.src2 = newsLetter.src.get(i);
                        break;
                    case 2:
                        newsLetterImage.src3 = newsLetter.src.get(i);
                        break;
                    case 3:
                        newsLetterImage.src4 = newsLetter.src.get(i);
                        break;
                    case 4:
                        newsLetterImage.src5 = newsLetter.src.get(i);
                        break;
                    case 5:
                        newsLetterImage.src6 = newsLetter.src.get(i);
                        break;
                    case 6:
                        newsLetterImage.src7 = newsLetter.src.get(i);
                        break;
                    case 7:
                        newsLetterImage.src8 = newsLetter.src.get(i);
                        break;
                    case 8:
                        newsLetterImage.src9 = newsLetter.src.get(i);
                        break;
                    case 9:
                        newsLetterImage.src10 = newsLetter.src.get(i);
                        break;
                }
            }

            Single<Long> result = repository.insertNewsImage(newsLetterImage);
            result.observeOn(AndroidSchedulers.mainThread()).subscribeWith(new SingleObserver<Long>() {
                @Override
                public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                }

                @Override
                public void onSuccess(@io.reactivex.annotations.NonNull Long aLong) {
                    Log.d("success",aLong+"");
                }

                @Override
                public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                    Log.d("Error",e.getMessage());
                }
            });
        }*/
    }

    public void deleteOneNewsLetter(NewsLetter newsLetter){
        repository.deleteOneNews(newsLetter);
    }

    /*public void insertNewsImage(NewsLetter newsLetter){
        NewsLetterImage newsLetterImage = new NewsLetterImage();
        newsLetterImage.newsId = newsLetter.NewsID;
        for(String src:newsLetter.src){
            newsLetterImage.src = src;
            repository.insertNewsImage(newsLetterImage);
        }


    }*/

    public void updateNewsLikeValue(boolean likeIt,String newsId){
        repository.updateNewsLikeValue(likeIt, newsId);
    }

    public void updateNewsLetter(NewsLetter newsLetter){
        repository.updateNewsLetter(newsLetter);
    }

    public LiveData<List<NewsLetter>> getAllNews(){
        return  LiveDataReactiveStreams.fromPublisher(repository.getAllNews());
    }

    public LiveData<List<String>> getNewsImage(String newsId){
        return LiveDataReactiveStreams.fromPublisher(repository.getNewsImage(newsId));
    }

    public void deleteAllNews(){
        repository.deleteAllNews();
    }
}
