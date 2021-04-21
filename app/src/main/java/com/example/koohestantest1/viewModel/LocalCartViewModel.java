package com.example.koohestantest1.viewModel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;

import com.example.koohestantest1.model.entity.CartInformation;
import com.example.koohestantest1.model.entity.CartProduct;
import com.example.koohestantest1.model.entity.CartWithProduct;
import com.example.koohestantest1.model.repository.LocalCartRepository;

import java.util.List;

import com.example.koohestantest1.classDirectory.SendOrderClass;

public class LocalCartViewModel extends AndroidViewModel {

    private final String TAG = LocalCartViewModel.class.getSimpleName();
    private LocalCartRepository localCartRepository;
    private MutableLiveData<Long> insertedCartId = new MutableLiveData<>();

    public LocalCartViewModel(@NonNull Application application) {
        super(application);
        localCartRepository = new LocalCartRepository(application);
    }

    public void insertProductToCart(CartProduct cartProduct) {
        localCartRepository.insertProduct(cartProduct).subscribe();
    }

    public void updateProductInCart(CartProduct cartProduct) {
        localCartRepository.updateProductInCart(cartProduct);
    }

    public void deleteProductInCart(CartProduct cartProduct) {
        localCartRepository.deleteProductInCart(cartProduct);
    }

    @SuppressLint("CheckResult")
    public void insertCart(CartInformation cartInformation) {
        localCartRepository.insertCartInfo(cartInformation)
                .subscribe(aLong -> insertedCartId.postValue(aLong), throwable -> Log.d(TAG, "insertCart:" + throwable.getMessage()));
    }

    public LiveData<List<CartWithProduct>> getCartData() {
        return LiveDataReactiveStreams.fromPublisher(localCartRepository.getData());
    }

    public LiveData<Integer> getCartCount() {
        return LiveDataReactiveStreams.fromPublisher(localCartRepository.getCartSize());
    }

    public LiveData<Integer> getProductCount() {
        return LiveDataReactiveStreams.fromPublisher(localCartRepository.getProductsSize());
    }

    public LiveData<Long> getInsertedCartId() {
        return insertedCartId;
    }

    public LiveData<CartProduct> getSpecificProduct(String pid) {
        return LiveDataReactiveStreams.fromPublisher(localCartRepository.getSpecificProduct(pid));
    }

    public void updateCartInfo(SendOrderClass sendOrderClass) {
        localCartRepository.updateCartInformation(sendOrderClass);
    }

    public void deleteAllData() {
        localCartRepository.deleteAllData();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared: ");
//        updateCartInfo(BaseCodeClass.sendOrderClass);
    }
}
