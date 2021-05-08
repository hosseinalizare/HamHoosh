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
import com.example.koohestantest1.local_db.entity.Product;
import com.example.koohestantest1.local_db.entity.Properties;

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
    public void insertNewsLetter(NewsLetter newsLetter){
        Single<Long> a = repository.insertNewsLetter(newsLetter);
        a.observeOn(AndroidSchedulers.mainThread()).subscribeWith(new SingleObserver<Long>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@io.reactivex.annotations.NonNull Long aLong) {
                Toast.makeText(getApplication(), aLong+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                Log.d("Error",e.getMessage());
            }
        });
    }

    public LiveData<List<NewsLetter>> getAllNews(){
        return  LiveDataReactiveStreams.fromPublisher(repository.getAllNews());
    }

    public void deleteAllNews(){
        repository.deleteAllNews();
    }
}
