package com.example.koohestantest1.classDirectory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koohestantest1.model.DeleteProduct;
import com.example.koohestantest1.model.UpdatedProductBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.koohestantest1.ApiDirectory.AddressApi;
import com.example.koohestantest1.ApiDirectory.LoadProductApi;
import com.example.koohestantest1.DB.MyDataBase;
import com.example.koohestantest1.ViewModels.BookMarkViewModel;
import com.example.koohestantest1.ViewModels.PostLikeViewModel;
import com.example.koohestantest1.ViewModels.PostViewViewModel;
import com.example.koohestantest1.model.network.RetrofitInstance;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.PageShow;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.addressSaved;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.addressViewModel;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.allAddress;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.deletedAddress;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.loadAddress;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;
import static com.nostra13.universalimageloader.utils.StorageUtils.getCacheDirectory;

public class DownloadPrudoctThread extends Thread {

    private final String TAG = DownloadPrudoctThread.class.getSimpleName();

    LoadProductApi callBack;
    LoadProductApi loadProductApi;
    BaseCodeClass baseCodeClass;
    final List<String> ProductIDList = new ArrayList<>();
    String productID = "";
    boolean inDownloadingImage = false;
    TextView edtxt;
    boolean LoadProductComplet = false;
    int indexDownloading = 0;
    private android.os.Handler mHandler = new Handler();
    Context context;
    MyDataBase mydb;

    AddressApi addressApi;
    AddressApi callBackAddress;

    String root = Environment.getExternalStorageDirectory().toString();
    File myDir;//= new File(root + "/Dehkade/File");

    int treahdTimer = 0;
    int SavedProduct = 0;
    List<ReceiveProductClass> LocalProduct;
    List<ReceiveProductClass> NetProduct = new ArrayList<>();

    public DownloadPrudoctThread(TextView edtxt_, Context context_) {
        try {

            context = context_;
            edtxt = edtxt_;
            setText("Thread Start");

            root = getCacheDirectory(context).getPath();
            myDir = new File(root + "/Dehkade/File");
            myDir.mkdirs();
            baseCodeClass = new BaseCodeClass();

            mydb = new MyDataBase(context);

            final Retrofit retrofit = RetrofitInstance.getRetrofit();
                    /* new Retrofit.Builder()
                    .baseUrl(baseCodeClass.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();*/

            loadProductApi = retrofit.create(LoadProductApi.class);

            addressApi = retrofit.create(AddressApi.class);

            callBackAddress = new AddressApi() {
                @Override
                public Call<List<String>> listState() {
                    return null;
                }

                @Override
                public void onResponseListState(List<String> stateList) {
                    baseCodeClass.statList = stateList;
                }

                @Override
                public Call<List<String>> listCity(String state) {
                    return null;
                }

                @Override
                public void onResponseListCity(List<String> cityList) {
                    baseCodeClass.currentCityList = cityList;
                }

                @Override
                public Call<GetResualt> setAddress(AddressViewModel addressViewModel) {

                    return null;
                }

                @Override
                public void onResponseSetAddress(GetResualt getResualt) {
                    if (getResualt.getResualt().equals("100")) {
                        addressSaved = true;
                    } else {
                        Toast.makeText(context_, getResualt.getMsg(), Toast.LENGTH_SHORT).show();
                        addressSaved = false;
                    }
                }

                @Override
                public Call<List<AddressViewModel>> getAddress(AddressViewModel addressViewModel) {
                    return null;
                }

                @Override
                public void onResponseGetMyAddress(List<AddressViewModel> addressViewModel) {

                }

                @Override
                public Call<GetResualt> delAddress(AddressViewModel addressViewModel) {
                    return null;
                }

                @Override
                public void onResponseDelAddress(GetResualt getResualt) {
                    if (getResualt.getResualt().equals("100")) {
                        loadAddress();
                        deletedAddress = true;
                    }
                }

                @Override
                public Call<AddressViewModel> getAddress(String addressID) {
                    return null;
                }

                @Override
                public void onResponseGetAddress(AddressViewModel addressViewModel) {

                }

                @Override
                public void AddressOnCliclckListener(AddressViewModel address) {

                }

                @Override
                public void CartRecyclerViewClickListener(View v, boolean b) {

                }

                @Override
                public void CartPaymentClickListener(int id) {

                }
            };

            callBack = new LoadProductApi() {


                @Override
                public Call<GetResualt> sendProductDetail(SendProduct sendProductClass) {
                    return null;
                }

                @Override
                public void onResponseSendProduct(GetResualt getResualt) {

                }

                @Override
                public Call<GetResualt> uploadProductImage(String prId, String coId, String uID, String token, MultipartBody.Part file) {
                    return null;
                }

                @Override
                public Call<GetResualt> uploadMultiProductImage(String prId, String coId, String uID, String token, List<MultipartBody.Part> file) {
                    return null;
                }

                @Override
                public Call<List<ReceiveProductClass>> loadProduct(String companyId) {
                    return null;
                }

                @Override
                public void onResponseLoadProduct(List<ReceiveProductClass> receiveProductClasses) {
                    NetProduct.clear();
                    NetProduct = receiveProductClasses;
                    SycronizProductTable_();
                    // ProductIDList.clear();
//                    for (SendProductClass productClass : sendProductClasses
//                    ) {
//                        ProductIDList.add(productClass.getProductID());
//                        LoadProductComplet = true;
//                        String f = String.valueOf(LoadProductComplet);
//                        setText("AllDataREsiv=>" + f);
//                    }
                }

                @Override
                public Call<List<ReceiveProductClass>> loadProduct(String companyId, String userID) {
                    return null;
                }

                @Override
                public Call<GetResualt> deleteProduct(SendDeleteProduct sendDeleteProduct) {
                    return null;
                }

                @Override
                public void onResponseDeleteProduct(GetResualt getResualt) {

                }

                @Override
                public Call<ResponseBody> downloadImage(String PId, String fileNumber) {
                    return null;
                }

                @Override
                public void onResponseDownloadImage(ResponseBody responseBody, String pid) {
                    SaveProductImage(responseBody, pid);
                    inDownloadingImage = false;
                }

                @Override
                public Call<List<String>> getCategory(int companyType) {
                    return null;
                }

                @Override
                public void onResponseGetCategory(List<String> cat) {

                }

                @Override
                public Call<List<String>> getSubCatOne(int companytype, String mainCat) {
                    return null;
                }

                @Override
                public void onResponseGetSubCatOne(List<String> subCat1) {

                }

                @Override
                public Call<List<GetPropertisOfCompanyProducts>> getProperty(int companytype) {
                    return null;
                }

                @Override
                public void onResponseGetProperty(List<GetPropertisOfCompanyProducts> propertisOfCompanyProducts) {

                }

                @Override
                public Call<GetResualt> viewProduct(PostViewViewModel postViewViewModel) {
                    return null;
                }

                @Override
                public Call<GetResualt> likeProduct(PostLikeViewModel postViewViewModel) {
                    return null;
                }

                @Override
                public Call<GetResualt> saveProduct(BookMarkViewModel postViewViewModel) {
                    return null;
                }

                @Override
                public Call<GetResualt> editProductDetail(SendProduct receiveProductClass) {
                    return null;
                }



                @Override
                public Call<List<ReceiveProductClass>> getUpdatedData(UpdatedProductBody updatedProductBody) {
                    return null;
                }

                @Override
                public void recyclerViewListClicked(View v, String value, boolean notify) {

                }

                @Override
                public void brandRecyclerViewListClicked(View v, String value, boolean notify) {

                }

                @Override
                public void recyclerViewCanUpdating() {

                }

                @Override
                public void imageAdapterCanUpdating(String imagePID) {

                }

                @Override
                public Call<GetResualt> deleteProduct(DeleteProduct deleteProduct) {
                    return null;
                }

            };
            setText("Thread End");
        } catch (Exception e) {
            setText("t " + e.getMessage());
        }
    }

//    private void loadState(){
//        Call<List<String>> call = addressApi.listState();
//        call.enqueue(new Callback<List<String>>() {
//            @Override
//            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
//                callBackAddress.onResponseListState(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<List<String>> call, Throwable t) {
//
//            }
//        });
//    }

//    private void loadCity(String state){
//
//        Call<List<String>> call = addressApi.listCity(state);
//        call.enqueue(new Callback<List<String>>() {
//            @Override
//            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
//                callBackAddress.onResponseListCity(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<List<String>> call, Throwable t) {
//
//            }
//        });
//    }

    private void setText(String Msg) {
        try {
            if (edtxt != null) {
                edtxt.setText(Msg);
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void run() {
        //  loadProduct();
        int timer = 0;
        while (true) {

            try {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                switch (PageShow) {
                    case none:
                        break;
                    case cartpage:
                        loadAddress();
                        PageShow = BaseCodeClass.myAppPage.none;
                        break;
                    case Search:
                        PageShow = BaseCodeClass.myAppPage.none;
                        break;
                    case myProfile:
                        loadAddress();
                        PageShow = BaseCodeClass.myAppPage.none;
                        break;
                    case ShoppCenter:

                        break;
                    case myStor:
                        PageShow = BaseCodeClass.myAppPage.none;
                        break;
                    case addressPage:
//                        loadState();
//                        loadCity(selectedState);
                        PageShow = BaseCodeClass.myAppPage.none;
                        break;
                    case saveAddress:
                        saveAddress(addressViewModel);
                        loadAddress();
                        PageShow = BaseCodeClass.myAppPage.none;
                        break;
                    case DeleteAddressPg:
                        // DeleteAddress(addressViewModel);
                        logMessage("آدرس پاک شد", context);
                        PageShow = BaseCodeClass.myAppPage.none;
                        break;
                    case ExitThread:
                        return;

                }

//                if (timer > 4) {
//                    if (productRecyclerViewAdapter != null) {
//                        productRecyclerViewAdapter.notifyDataSetChanged();
//                    }
//                    timer = 0;
//
//                }
            } catch (Exception ex) {

            }
        }
//        // Moves the current Thread into the background
////        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
////
////        setText("Thread ->" );
////        loadProduct();
////      //  while (true)
////        {
//////            try {
//////                Thread.sleep(200);
//////              //  edtxt.setText("Thread ->" + String.valueOf(indexDownloading++) );
//////            } catch (InterruptedException e) {
//////                e.printStackTrace();
//////            }
////            mHandler.post(new Runnable() {
////                @Override
////                public void run() {
////                   // edtxt.setText("Threading Start->" + String.valueOf(indexDownloading) );
////
////                    while (true) {
////                        try {
////
////                            Working();
////                            Thread.sleep(500);
////                            //  edtxt.setText("Thread ->" + String.valueOf(indexDownloading++) );
////                        } catch (InterruptedException e) {
////                            e.printStackTrace();
////                        }
////                    }
////                }
////            });
////        }
    }

    private void DeleteAddress(AddressViewModel addressViewModel) {
        deletedAddress = false;
        Call<GetResualt> call = addressApi.delAddress(addressViewModel);
        call.enqueue(new Callback<GetResualt>() {
            @Override
            public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                GetResualt getResualt = new GetResualt(response.body().getResualt(), response.body().getMsg());
                callBackAddress.onResponseDelAddress(getResualt);
            }

            @Override
            public void onFailure(Call<GetResualt> call, Throwable t) {

            }
        });
    }


    public void SycronizProductTable_() {
        try {
            setText("SycronizProductTable_" + NetProduct.size());
            SavedProduct = 0;
            ProductIDList.clear();
            mydb.SetAllProductEnable(context, baseCodeClass.getCompanyID());
            for (ReceiveProductClass productClass : NetProduct
            ) {
                if (mydb.insertProduct(context, productClass.getCategory(),
                        productClass.getDeleted(), "false",
                        productClass.getDescription(), String.valueOf(productClass.getDiscontinued()),
                        "", productClass.getStandardCost().getShowoffPrice(), productClass.getMinimumReorderQuantity(),
                        productClass.getProductID(),
                        productClass.getProductName(), productClass.getQuantityPerUnit(),
                        productClass.getReorderLevel(), productClass.getShow(), productClass.getSpare1(),
                        productClass.getSpare2(), productClass.getSpare3(), productClass.getStandardCost().getShowPrice(),
                        productClass.getSupplierID(), productClass.getTargetLevel(), productClass.getUnit(),
                        productClass.getUpdateDate(), productClass.isLikeit(), productClass.getSaveit(), productClass.getSellCount()) != -2) {
                    ProductIDList.add(productClass.getProductID());
                } else {
                    String fname = myDir + "/" + productClass.getProductID() + ".jpg";
                    File file = new File(fname);
                    if (!file.exists()) {
                        ProductIDList.add(productClass.getProductID());
                    }
                }
                for (ProductPropertisClass propertisClass : productClass.getProductPropertis()
                ) {

                    Single<Long> single = mydb.insertProductPropertiesAsync(context, "false", "", propertisClass.getProductID(),
                            propertisClass.getPropertisGroup(), propertisClass.getPropertisName(),
                            propertisClass.getPropertisValue(), "", "", "", propertisClass.getUpdatTime());
                    single.subscribe(new SingleObserver<Long>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@NonNull Long aLong) {
                            if (aLong == -1)
                                Toast.makeText(context, "خطایی رخ داد هنگام وارد کردن دیتا", Toast.LENGTH_SHORT).show();

                            Log.d(TAG, "onSuccess: " + aLong);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
            mydb.DeletNotExsistProduct(context);
        } catch (Exception ex) {

        }
        LoadProductComplet = true;
    }

//    public void SycronizProductTable()
//    {
//        Cursor cursor = mydb.GetAllRawOfTable(context, "Product");
//        String Viewpost="0",LikePost="0",SavePost="0",ViewUpdatdate;
//
//        if (cursor.moveToFirst()) {
//            do {
//               String ProductID = cursor.getString(cursor.getColumnIndex("ProductID"));
//                Cursor cursorview = mydb.GetProductViwe(context, ProductID);
//                if(cursorview != null) {
//                    cursorview.moveToFirst();
//                    Viewpost = cursorview.getString(cursor.getColumnIndex("ViweCount"));
//                    LikePost = cursorview.getString(cursor.getColumnIndex("likCount"));
//                    SavePost = cursorview.getString(cursor.getColumnIndex("saveCount"));
//                }
//                else
//                {
//                    Viewpost="0";LikePost="0";SavePost="0";ViewUpdatdate="";
//                }
//                SendProductClass productClass = new SendProductClass("","",cursor.getString(cursor.getColumnIndex("SupplierID")),
//                        cursor.getString(cursor.getColumnIndex("SupplierID")),
//                        ProductID,
//                        cursor.getString(cursor.getColumnIndex("ProductName")),
//                        cursor.getString(cursor.getColumnIndex("description")),
//                        cursor.getString(cursor.getColumnIndex("standardCost")) ,
//                        cursor.getString(cursor.getColumnIndex("listPrice")),
//                        cursor.getString(cursor.getColumnIndex("reorderLevel")),
//                        cursor.getString(cursor.getColumnIndex("targetLevel")),
//                        cursor.getString(cursor.getColumnIndex("unit")),
//                        cursor.getString(cursor.getColumnIndex("quantityPerUnit")),
//                        cursor.getString(cursor.getColumnIndex("discontinued")),
//                        cursor.getString(cursor.getColumnIndex("MinimumReorderQuantity")),
//                        cursor.getString(cursor.getColumnIndex("Category")),
//                        cursor.getString(cursor.getColumnIndex("show")),
//                        cursor.getString(cursor.getColumnIndex("updateDate")),
//                        cursor.getString(cursor.getColumnIndex("Deleted")),
//                        Viewpost,
//                        LikePost,
//                        SavePost,
//                        null);
//                LocalProduct.add(productClass);
//
//            } while (cursor.moveToNext());
//
//        }
//
//    }


    public void Working() {
        try {
//         ((com.example.koohestantest1.classDirectory.BaseCodeClass) context.getApplicationContext()).setGlobalTest("test" + index++);

//         staticTest = "t" + index++;

            if (!LoadProductComplet) {
                treahdTimer++;
                if (treahdTimer >= 40) {
                    treahdTimer = 0;
                    loadProduct();
                }
            }
            if (LoadProductComplet) {
                treahdTimer = 0;
                if (!inDownloadingImage) {

                    productID = ProductIDList.get(indexDownloading);
                    String fname = myDir + "/" + productID + ".jpg";
                    File file = new File(fname);
                    file.deleteOnExit();
                    inDownloadingImage = true;
                    loadProductImage();
                    indexDownloading++;
                    if (indexDownloading >= ProductIDList.size()) {
                        indexDownloading = 0;
                        LoadProductComplet = false;
                        PageShow = BaseCodeClass.myAppPage.none;
                        loadProduct();
                    }
                }
                setText("Downloading Pruduct Image...(" + SavedProduct + " )=>" + indexDownloading);
            }
        } catch (Exception e) {
        }
    }

    public void saveAddress(AddressViewModel addressViewModel) {
        addressSaved = false;
        Call<GetResualt> call = addressApi.setAddress(addressViewModel);
        call.enqueue(new Callback<GetResualt>() {
            @Override
            public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                GetResualt getResualt = new GetResualt(response.body().getResualt(), response.body().getMsg());
                callBackAddress.onResponseSetAddress(getResualt);
            }

            @Override
            public void onFailure(Call<GetResualt> call, Throwable t) {

            }
        });
    }

    public void loadAddress() {
        loadAddress = false;
        AddressViewModel addressViewModel = new AddressViewModel("", baseCodeClass.getUserID(), baseCodeClass.getToken(), baseCodeClass.getUserID(),
                "", "", "", "", "", "", "", "", "", "", "");
        Call<List<AddressViewModel>> call = addressApi.getAddress(addressViewModel);
        call.enqueue(new Callback<List<AddressViewModel>>() {
            @Override
            public void onResponse(Call<List<AddressViewModel>> call, Response<List<AddressViewModel>> response) {
                loadAddress = true;
                allAddress = response.body();
            }

            @Override
            public void onFailure(Call<List<AddressViewModel>> call, Throwable t) {
                logMessage(t.getMessage(), context);
            }
        });
    }

    public void loadProduct() {
        setText(" loadProduct Start");
        try {
            Call<List<ReceiveProductClass>> call = loadProductApi.loadProduct(baseCodeClass.getCompanyID(), baseCodeClass.getUserID());
            call.enqueue(new Callback<List<ReceiveProductClass>>() {
                @Override
                public void onResponse(Call<List<ReceiveProductClass>> call, Response<List<ReceiveProductClass>> response) {
                    try {
                        callBack.onResponseLoadProduct(response.body());
                    } catch (Exception e) {
                        // toastMessage(e.getMessage(),8);
                    }
                }

                @Override
                public void onFailure(Call<List<ReceiveProductClass>> call, Throwable t) {

                }
            });
        } catch (Exception ex) {
            setText(" loadProduct Error " + ex.getMessage());
        }
    }

    public void loadProductImage() {
        try {
            Call<ResponseBody> call = loadProductApi.downloadImage(productID, "1");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    callBack.onResponseDownloadImage(response.body(), productID);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    inDownloadingImage = false;
                    // toastMessage("7" + t.getMessage(),7);
                }
            });
        } catch (Exception e) {
            // toastMessage("6" + e.getMessage(),6);
            inDownloadingImage = false;
        }
    }

    public boolean SaveProductImage(ResponseBody body, String name) {
        try {
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {

                Bitmap bm = null;
                if (body != null) {
                    inputStream = body.byteStream();
                    bm = BitmapFactory.decodeStream(inputStream);
                } else {
                    return false;
                }


//
                String fname = name + ".jpg";
                File file = new File(myDir, fname);
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                }
            } catch (Exception e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }


}
