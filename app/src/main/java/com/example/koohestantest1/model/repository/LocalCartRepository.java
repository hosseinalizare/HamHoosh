package com.example.koohestantest1.model.repository;

import android.app.Application;
import android.util.Log;

import com.example.koohestantest1.constants.CurrentCartId;
import com.example.koohestantest1.model.AppRoom;
import com.example.koohestantest1.model.dao.CartDao;
import com.example.koohestantest1.model.entity.CartInformation;
import com.example.koohestantest1.model.entity.CartProduct;
import com.example.koohestantest1.model.entity.CartWithProduct;

import java.util.List;

import com.example.koohestantest1.ViewModels.Order_DetailsViewModels;
import com.example.koohestantest1.classDirectory.SendOrderClass;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class LocalCartRepository {
    private Flowable<List<CartWithProduct>> data;
    private Flowable<Integer> cartSize;
    private Flowable<Integer> productSize;
    private CartDao cartDao;

    public LocalCartRepository(Application application) {
        AppRoom appRoom = AppRoom.getDatabase(application);
        cartDao = appRoom.cartDao();
        data = cartDao.DATA();
        cartSize = cartDao.getCartCount();
        productSize = cartDao.getProductsSize();
    }

    public void deleteProductInCart(CartProduct cartProduct) {
        cartDao.deleteProductInCart(cartProduct)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void updateProductInCart(CartProduct cartProduct) {
        cartDao.updateProductInCart(cartProduct)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public Single<Long> insertProduct(CartProduct cartProduct) {
        return cartDao.insertProductToCart(cartProduct)
                .subscribeOn(Schedulers.io());
    }

    public Flowable<List<CartWithProduct>> getData() {
        return data;
    }

    public Single<Long> insertCartInfo(CartInformation cartInformation) {
        return cartDao.insertCart(cartInformation)
                .subscribeOn(Schedulers.io());
    }

    public Flowable<Integer> getCartSize() {
        return cartSize;
    }

    public Flowable<Integer> getProductsSize() {
        return productSize;
    }

    public Flowable<CartProduct> getSpecificProduct(String pid) {
        return cartDao.getSpecificProduct(pid);
    }

    public void updateCartInformation(SendOrderClass sendOrderClass) {

        try {
            CartInformation cartInformation = new CartInformation(CurrentCartId.getId(), false, sendOrderClass.getOrderID(), sendOrderClass.getToken(), sendOrderClass.getUserID(),
                    sendOrderClass.getEmployeeID(), sendOrderClass.getCustomerID(), sendOrderClass.getCompanyID(), sendOrderClass.getOrderDate(), sendOrderClass.getShippedDate(), sendOrderClass.getShipperID(), sendOrderClass.getShipName(), sendOrderClass.getShipAddress(), sendOrderClass.getShipCity(), sendOrderClass.getShipStateProvince(),
                    sendOrderClass.getShipZIPPostalCode(), sendOrderClass.getShipCountryRegion(), sendOrderClass.getShippingFee(), sendOrderClass.getTaxes(), sendOrderClass.getPaymentType(), sendOrderClass.getPaidDate(), sendOrderClass.getNotes(), sendOrderClass.getTaxRate(),
                    sendOrderClass.getTaxStatus(), sendOrderClass.getStatusID(), sendOrderClass.getUpdateDate(), sendOrderClass.getDeleted(), sendOrderClass.getSpare1(), sendOrderClass.getSpare2(), sendOrderClass.getSpare3(),
                    sendOrderClass.getSumPrice());

            cartDao.deleteAllProducts().subscribeOn(Schedulers.io())
                    .subscribe();

            for (Order_DetailsViewModels models :
                    sendOrderClass.getOrder_Details()) {
                cartDao.insertProductToCart(new CartProduct(0, CurrentCartId.getId(), models.getID(), models.getOrderID(), models.getProductID(),
                        models.getQuantity(), models.getUnitPrice(), models.getDiscount(), models.getStatusID(), models.getDateAllocated(), models.getPurchaseOrderID(), models.getInventoryID(), models.getProductName(), models.getSumPrice(), models.getSumOff()))
                        .subscribeOn(Schedulers.io())
                        .subscribe();
            }


            cartDao.updateCart(cartInformation)
                    .subscribeOn(Schedulers.io())
                    .subscribe();
        } catch (Exception e) {
            Log.d(LocalCartRepository.class.getSimpleName(), "onError: " + e.getMessage());
        }

    }

    public void deleteAllData() {
        cartDao.deleteAllCart()
                .subscribeOn(Schedulers.io())
                .subscribe();
        cartDao.deleteAllProducts()
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

}
