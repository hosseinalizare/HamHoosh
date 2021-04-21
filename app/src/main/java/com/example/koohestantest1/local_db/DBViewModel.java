package com.example.koohestantest1.local_db;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;

import com.example.koohestantest1.local_db.entity.Product;
import com.example.koohestantest1.local_db.entity.Properties;

import java.util.List;

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
}
