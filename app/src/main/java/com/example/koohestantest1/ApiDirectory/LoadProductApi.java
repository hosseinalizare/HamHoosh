package com.example.koohestantest1.ApiDirectory;

import android.view.View;

import com.example.koohestantest1.classDirectory.SendProduct;
import com.example.koohestantest1.local_db.entity.Product;
import com.example.koohestantest1.model.DeleteProduct;
import com.example.koohestantest1.model.UpdatedProductBody;

import java.util.List;

import com.example.koohestantest1.ViewModels.BookMarkViewModel;
import com.example.koohestantest1.ViewModels.PostLikeViewModel;
import com.example.koohestantest1.ViewModels.PostViewViewModel;
import com.example.koohestantest1.classDirectory.GetPropertisOfCompanyProducts;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.SendDeleteProduct;
import com.example.koohestantest1.classDirectory.ReceiveProductClass;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface LoadProductApi {

    @POST("Products/product")
    Call<GetResualt> sendProductDetail(@Body SendProduct sendProductClass);
    void onResponseSendProduct(GetResualt getResualt);

    @Multipart
    @POST("Products/Post")
    Call<GetResualt> uploadProductImage(@Query("ProductID") String prId, @Query("CompanyID") String coId, @Query("UserID") String uID, @Query("Token") String token, @Part MultipartBody.Part file);

    @Multipart
    @POST("Products/Post")
    Call<GetResualt> uploadMultiProductImage(@Query("ProductID") String prId,
                                             @Query("CompanyID") String coId,
                                             @Query("UserID") String uID,
                                             @Query("Token") String token,
                                             @Part List<MultipartBody.Part> file);

    @POST("Products/LoadProduct")
    Call<List<ReceiveProductClass>> loadProduct(@Query("CompanyID")String companyId);
    void onResponseLoadProduct(List<ReceiveProductClass> receiveProductClasses);

    @POST("Products/LoadProduct")
    Call<List<ReceiveProductClass>> loadProduct(@Query("CompanyID")String companyId, @Query("UserID") String userID);

    @POST("Products/Deletproduct")
    Call<GetResualt> deleteProduct(@Body SendDeleteProduct sendDeleteProduct);
    void onResponseDeleteProduct(GetResualt getResualt);

    @GET("Products/DownloadFile")
    Call<ResponseBody> downloadImage(@Query("ProductID") String PId, @Query("fileNumber") String fileNumber);
    void onResponseDownloadImage(ResponseBody responseBody, String pid);

    @POST("Products/ProductMainCategories")
    Call<List<String>> getCategory(@Query("companytype") int companyType);
    void onResponseGetCategory(List<String> cat);

    @POST("Products/ProductSubCategories")
    Call<List<String>> getSubCatOne(@Query("companytype") int companytype, @Query("mainCat") String mainCat);
    void onResponseGetSubCatOne(List<String> subCat1);

    @POST("Products/PropertisOfCompanyProducts")
    Call<List<GetPropertisOfCompanyProducts>> getProperty(@Query("companytype") int companytype);
    void onResponseGetProperty(List<GetPropertisOfCompanyProducts> propertisOfCompanyProducts);

    @POST("Products/ViewPost")
    Call<GetResualt> viewProduct(@Body PostViewViewModel postViewViewModel);

    @POST("Products/LikePost")
    Call<GetResualt> likeProduct(@Body PostLikeViewModel postViewViewModel);

    @POST("Products/SavePost")
    Call<GetResualt> saveProduct(@Body BookMarkViewModel postViewViewModel);

    @POST("Products/Editproduct")
    Call<GetResualt> editProductDetail(@Body SendProduct receiveProductClass);

    @POST("Products/LoadUpdatedProduct")
    Call<List<ReceiveProductClass>> getUpdatedData(@Body UpdatedProductBody updatedProductBody);

     void recyclerViewListClicked(View v, String value, boolean notify);
     void brandRecyclerViewListClicked(View v, String value, boolean notify);
     void recyclerViewCanUpdating(List<Product> products);
     void imageAdapterCanUpdating(String imagePID);

     @POST("Products/Deletproduct")
    Call<GetResualt> deleteProduct(@Body DeleteProduct deleteProduct);
}
