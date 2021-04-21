package com.example.koohestantest1.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koohestantest1.model.EditBodyRequest;
import com.example.koohestantest1.model.repository.ProductRepository;
import com.example.koohestantest1.model.repository.callback.InvisibleProductCallBack;
import com.example.koohestantest1.model.repository.callback.NotInStockCallBack;
import com.example.koohestantest1.model.repository.callback.ProductEditCallBack;

import java.util.List;

import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.SendProductClass;

public class ProductViewModel extends ViewModel implements ProductEditCallBack, InvisibleProductCallBack, NotInStockCallBack {
    private ProductRepository productRepository;

    private final MutableLiveData<GetResualt> liveResultEditProduct = new MutableLiveData<>();
    private final MutableLiveData<String> liveErrorEditProduct = new MutableLiveData<>();


    private final MutableLiveData<String> liveEditedValue = new MutableLiveData<>();


    private final MutableLiveData<List<SendProductClass>> liveInvisibleProducts = new MutableLiveData<>();
    private final MutableLiveData<String> liveErrorInvisibleProducts = new MutableLiveData<>();

    public LiveData<List<SendProductClass>> getLiveStockProducts() {
        return liveStockProducts;
    }

    public LiveData<String> getLiveErrorStockProducts() {
        return liveErrorStockProducts;
    }

    private final MutableLiveData<List<SendProductClass>> liveStockProducts = new MutableLiveData<>();
    private final MutableLiveData<String> liveErrorStockProducts = new MutableLiveData<>();

    public ProductViewModel() {
        productRepository = new ProductRepository(this, this, this);

    }

    public void callForNotInStockProducts(String companyId, String userId, String token) {
        productRepository.callForNotInStockProducts(companyId, userId, token);
    }

    public void callForEditingProduct(EditBodyRequest bodyRequest) {
        productRepository.callForEditingProduct(bodyRequest);
    }

    public void callForInvisibleProducts(String userId, String companyId, String token) {
        productRepository.callForInvisibleProducts(companyId, userId, token);
    }

    public void setEditedValue(String value) {
        liveEditedValue.setValue(value);
    }

    public LiveData<String> getLiveEditedValue() {
        return liveEditedValue;
    }

    public LiveData<GetResualt> getLiveResultEditProduct() {
        return liveResultEditProduct;
    }

    public LiveData<String> getLiveErrorEditProduct() {
        return liveErrorEditProduct;
    }

    @Override
    public void onSuccessEdit(GetResualt resualt) {
        liveResultEditProduct.setValue(resualt);
    }

    @Override
    public void onSuccessInvisible(List<SendProductClass> results) {
        liveInvisibleProducts.setValue(results);
    }

    @Override
    public void onErrorInvisible(String error) {
        liveErrorInvisibleProducts.setValue(error);
    }

    @Override
    public void onErrorEdit(String error) {
        liveErrorEditProduct.setValue(error);
    }

    public LiveData<List<SendProductClass>> getLiveInvisibleProducts() {
        return liveInvisibleProducts;
    }

    public LiveData<String> getLiveErrorInvisibleProducts() {
        return liveErrorInvisibleProducts;
    }

    @Override
    public void onSuccessStock(List<SendProductClass> res) {
        liveStockProducts.setValue(res);
    }

    @Override
    public void onErrorStock(String error) {
        liveErrorStockProducts.setValue(error);
    }
}
