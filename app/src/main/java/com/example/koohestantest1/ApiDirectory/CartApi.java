package com.example.koohestantest1.ApiDirectory;

import java.util.List;

import com.example.koohestantest1.ViewModels.UpdateOrderClass;
import com.example.koohestantest1.ViewModels.UserReportViewModel;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.SendOrderClass;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CartApi {

    @POST("Order/Order")
    Call<GetResualt> sendOrder(@Body SendOrderClass sendOrderClass);
    void onResponseSendOrder(GetResualt getResualt);

    @POST("Order/downloadCompanyOrders")
    Call<List<SendOrderClass>> getCompanyOrder(@Query("CompanyID") String companyID);
    void onResponseGetCompanyOrder(List<SendOrderClass> sendOrderClasses);

    @POST("Order/UpdateOrder")
    Call<GetResualt> updateOrder(@Body UpdateOrderClass updateOrderClass);

    @POST("Order/downloadUserOrders")
    Call<List<SendOrderClass>> getUserOrder(@Body UserReportViewModel userReportViewModel);
    void onResponseUserOrder(List<SendOrderClass> sendOrderClasses);

}
