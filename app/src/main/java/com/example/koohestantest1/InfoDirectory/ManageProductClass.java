package com.example.koohestantest1.InfoDirectory;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.koohestantest1.local_db.DBViewModel;
import com.example.koohestantest1.local_db.entity.Product;
import com.example.koohestantest1.local_db.entity.Properties;
import com.example.koohestantest1.model.UpdatedProductBody;
import com.example.koohestantest1.model.network.RetrofitInstance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.koohestantest1.ApiDirectory.LoadProductApi;
import com.example.koohestantest1.DB.MyDataBase;
import com.example.koohestantest1.classDirectory.AllProductData;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.ProductPropertisClass;
import com.example.koohestantest1.classDirectory.SendProductClass;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.bulletinProduct;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.particularProduct;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.productDataList;
import static com.nostra13.universalimageloader.utils.StorageUtils.getCacheDirectory;

public class ManageProductClass extends AsyncTask<String, Integer, String> {

    private String TAG = ManageProductClass.class.getSimpleName();

    private boolean isLoadCompleted = false;
    //    ProgressDialog pdLoading;
    static Context mContext;
    static Retrofit retrofit;
    static BaseCodeClass baseCodeClass = new BaseCodeClass();
    static LoadProductApi productApi;
    static LoadProductApi callBackproductApi;
    static final List<String> ProductIDList = new ArrayList<>();
    static String root = Environment.getExternalStorageDirectory().toString();
    static File myDir;
    static MyDataBase mydb;
    final String CompanyID;
    //    static Boolean EndOfList = false;
    private String Error;

    public ManageProductClass(Context context, LoadProductApi api, String CompanyId) {
        mContext = context;

        root = getCacheDirectory(context).getPath();
        myDir = new File(root + "/Dehkade/File");
        myDir.mkdirs();
        baseCodeClass = new BaseCodeClass();

        mydb = new MyDataBase(context);
        CompanyID = CompanyId;

        callBackproductApi = api;
        retrofit = RetrofitInstance.getRetrofit();


        productApi = retrofit.create(LoadProductApi.class);
    }

    List<AllProductData> allProductDataList;

    public ManageProductClass(Context context, LoadProductApi api, String CompanyId, List<AllProductData> dataList) {
        mContext = context;
        allProductDataList = dataList;
        root = getCacheDirectory(context).getPath();
        myDir = new File(root + "/Dehkade/File");
        myDir.mkdirs();
        baseCodeClass = new BaseCodeClass();

        mydb = new MyDataBase(context);
        CompanyID = CompanyId;

//        pdLoading = new ProgressDialog(mContext);
        callBackproductApi = api;
        retrofit = RetrofitInstance.getRetrofit();
        productApi = retrofit.create(LoadProductApi.class);
    }

    public void LoadAllProduct(String CompanyId) {
        try {
            Call<List<SendProductClass>> call = productApi.loadProduct(CompanyId, baseCodeClass.getUserID());
            call.enqueue(new Callback<List<SendProductClass>>() {
                @Override
                public void onResponse(Call<List<SendProductClass>> call, Response<List<SendProductClass>> response) {
                    try {
                        Log.d(TAG, "onResponse: ****size all list****" + response.body().size());
                        /*synchronizeProductTable(response.body(), false).subscribe(new SingleObserver<Boolean>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onSuccess(@NonNull Boolean aBoolean) {
                                if (aBoolean) {
                                    callBackproductApi.onResponseLoadProduct(null);
                                    isLoadCompleted = true;
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d(TAG + "SyncTables", "onError: " + e.getMessage());
                            }
                        });*/
                        synchronizeProductTableVer2(response.body(), false);

                        //  Download();
                    } catch (Exception e) {
                        // toastMessage(e.getMessage(),8);
                    }
                }

                @Override
                public void onFailure(Call<List<SendProductClass>> call, Throwable t) {

                }
            });
        } catch (Exception ex) {
            baseCodeClass.logMessage(" loadProduct Error " + ex.getMessage(), mContext);
        }
    }

    private boolean ISParticular(String reOrder) {
        if (reOrder.equals("100") || reOrder.equals("200")) {
            return true;
        }
        return false;
    }

    private boolean ISBulletin(String reOrder) {
        if (reOrder.equals("150") || reOrder.equals("200")) {
            return true;
        }
        return false;
    }

    List<ProductPropertisClass> loadProperty(String Pid) {
        try {
            List<ProductPropertisClass> lppc = new ArrayList<>();

            Cursor cursor = mydb.GetProductProperties(mContext, Pid);
            List<String> s = new ArrayList<>();
            s.add(Pid);
            if (cursor.moveToFirst()) {
                do {
                    ProductPropertisClass ppc = new ProductPropertisClass(cursor.getString(cursor.getColumnIndex(mydb.ProductID)), cursor.getString(cursor.getColumnIndex(mydb.PropertisGroup)),
                            cursor.getString(cursor.getColumnIndex(mydb.PropertisName)), cursor.getString(cursor.getColumnIndex(mydb.PropertisValue)),
                            cursor.getString(cursor.getColumnIndex(mydb.UpdateTime)));
                    s.add(cursor.getString(cursor.getColumnIndex(mydb.PropertisName)));
                    lppc.add(ppc);
                } while (cursor.moveToNext());
                return lppc;
            }
        } catch (Exception e) {
            // toastMessage(e.getMessage(), 3000);
            return null;
        }
        return null;
    }

    private Date convertStrToDate(String dateTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date d = sdf.parse(dateTime);
            return d;
        } catch (ParseException ex) {
//            baseCodeClass.logMessage("ParseException : " + ex.getMessage(), mContext);
        } catch (Exception e) {
//            baseCodeClass.logMessage("ConvertDate Error : " + e.getMessage(), mContext);
        }
        return null;
    }

    Date lastUpdateTime = new Date();
    String updateTime;
    //DatabaseService db;
    DBViewModel dbViewModel;
    Date temp;

    private boolean loadProductFromRoomDB(String companyID) {
        lastUpdateTime = convertStrToDate(updateTime);
        //db = new DatabaseService(mContext);

        dbViewModel = new ViewModelProvider((AppCompatActivity) mContext).get(DBViewModel.class);

        dbViewModel.getAllProducts().observe((AppCompatActivity) mContext, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                if (products == null || products.size() == 0)
                    Log.d("Error","false");
                else {
                    productDataList.clear();
                    particularProduct.clear();
                    bulletinProduct.clear();
                    for (Product product : products) {
                        if (product.Deleted ||
                                (!product.Show) ||
                                (!product.CompanyID.equals(companyID))
                        ) {
                            continue;
                        }
                        String pid = product.ProductID;
                        SendProductClass spc = new SendProductClass(product, null);


                        List<String> category = new ArrayList<>();
                        String cat = product.Category;
                        if (cat != null && !cat.isEmpty()) {
                            String[] aCategory = cat.split("\\.");
                            for (String s : aCategory) {
                                if (s != null && !s.isEmpty() && !s.equals("null")) {
                                    category.add(s);
                                }
                            }
                        }
                        NumberFormat formatter = new DecimalFormat("#,###");
                        String nPrice = product.StandardCost;
                        if (nPrice != null && !nPrice.isEmpty()) {
                            String[] aPrice = nPrice.split("\\.");
                            nPrice = (aPrice[0]);
                            nPrice = formatter.format(Integer.parseInt(nPrice));
                        }

                        String LPrice = product.ListPrice;
                        if (LPrice != null && !LPrice.isEmpty()) {
                            String[] aPrice = LPrice.split("\\.");
                            LPrice = (aPrice[0]);

                        }
                        AllProductData allProductData = new AllProductData(mContext, null, false, product.Likeit,
                                null, product.Saveit, product.LikeCount, product.ViewedCount, category, spc);
                        if (ISParticular(String.valueOf(product.ReorderLevel))) {
                            particularProduct.add(allProductData);
                        }
                        if (ISBulletin(String.valueOf(product.ReorderLevel))) {
                            bulletinProduct.add((allProductData));
                        }
                        productDataList.add(allProductData);
                        temp = convertStrToDate(String.valueOf(product.UpdateDate));
                        if (temp.after(lastUpdateTime)) {
                            lastUpdateTime = temp;
                            updateTime = String.valueOf(product.UpdateDate);
                        }
                        //  String[] like = cursor.getString(cursor.getColumnIndex(MyDataBase.Spare2)).split("&");
                        // String[] bookmark = cursor.getString(cursor.getColumnIndex(MyDataBase.Spare3)).split("&");
                    }
                }
            }
        });


        return true;

    }

    private boolean loadProductFromDB() {
        Log.d(TAG, "loadProductFromDB: ##thread " + Thread.currentThread().getName());
        int count = 0;
        updateTime = "2005-01-01T00:00:00.000";
        lastUpdateTime = convertStrToDate(updateTime);
        Log.d(TAG, "loadProductFromDB: ");

        Date temp;
        try {
            Cursor cursor = mydb.GetAllRawOfTable(mContext, "Product");

            if (cursor.moveToFirst()) {
                productDataList.clear();
                particularProduct.clear();
                bulletinProduct.clear();

                do {
                    if (cursor.getString(cursor.getColumnIndex(MyDataBase.Deleted)).equals("true") ||
                            cursor.getString(cursor.getColumnIndex(MyDataBase.Deleted)).equals("True"))
                        continue;

                    List<String> category = new ArrayList<>();
                    String cat = cursor.getString(cursor.getColumnIndex(MyDataBase.Category));
                    if (cat != null && !cat.isEmpty()) {
                        String[] aCategory = cat.split("\\.");
                        for (String s : aCategory) {
                            if (s != null && !s.isEmpty() && !s.equals("null")) {
                                category.add(s);
                            }
                        }
                    }
                    NumberFormat formatter = new DecimalFormat("#,###");
                    String nPrice = cursor.getString(cursor.getColumnIndex(MyDataBase.StandardCost));
                    if (nPrice != null && !nPrice.isEmpty()) {
                        String[] aPrice = nPrice.split("\\.");
                        nPrice = (aPrice[0]);
                        nPrice = formatter.format(Integer.parseInt(nPrice));
                    }

                    String LPrice = cursor.getString(cursor.getColumnIndex(MyDataBase.ListPrice));
                    if (LPrice != null && !LPrice.isEmpty()) {
                        String[] aPrice = LPrice.split("\\.");
                        LPrice = (aPrice[0]);

                    }

                    String[] like = cursor.getString(cursor.getColumnIndex(MyDataBase.Spare2)).split("&");
                    String[] bookmark = cursor.getString(cursor.getColumnIndex(MyDataBase.Spare3)).split("&");

                    String pid = cursor.getString(cursor.getColumnIndex(MyDataBase.ProductID));
                    /*SendProductClass spc = new SendProductClass(baseCodeClass.getToken(), baseCodeClass.getUserID(), cursor.getString(cursor.getColumnIndex(MyDataBase.SupplierID)),
                            cursor.getString(cursor.getColumnIndex(MyDataBase.SupplierID)), pid,
                            cursor.getString(cursor.getColumnIndex(MyDataBase.ProductName)), cursor.getString(cursor.getColumnIndex(MyDataBase.Description)),
                            nPrice, cursor.getString(cursor.getColumnIndex(MyDataBase.ListPrice)),
                            cursor.getInt(cursor.getColumnIndex(MyDataBase.ReorderLevel)), cursor.getInt(cursor.getColumnIndex(MyDataBase.TargetLevel)),
                            cursor.getString(cursor.getColumnIndex(MyDataBase.Unit)), cursor.getString(cursor.getColumnIndex(MyDataBase.QuantityPerUnit)),
                            cursor.getInt(cursor.getColumnIndex(MyDataBase.Discontinued)), cursor.getInt(cursor.getColumnIndex(MyDataBase.MinimumReorderQuantity)),
                            cursor.getString(cursor.getColumnIndex(MyDataBase.Category)), Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(MyDataBase.Show))),
                            cursor.getInt(cursor.getColumnIndex(MyDataBase.UpdateDate)), Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(MyDataBase.Deleted))),
                            cursor.getString(cursor.getColumnIndex(MyDataBase.Spare1)), like[0], bookmark[0],
                            like[1], bookmark[1], loadProperty(pid));*/
                    //TODO After save Sellcount to db can use line below to get sell count
                    //spc.setSellCount(cursor.getInt(cursor.getColumnIndex(MyDataBase.SellCount)));
                    AllProductData allProductData = new AllProductData(mContext, null, false, Boolean.parseBoolean(like[1]),
                            null, Boolean.parseBoolean(bookmark[1]), 0, 0, category,null /*spc*/);
                    if (ISParticular(cursor.getString(cursor.getColumnIndex(MyDataBase.ReorderLevel)))) {
                        particularProduct.add(allProductData);
                    }
                    if (ISBulletin(cursor.getString(cursor.getColumnIndex(MyDataBase.ReorderLevel)))) {
                        bulletinProduct.add((allProductData));
                    }
                    productDataList.add(allProductData);
                    temp = convertStrToDate(cursor.getString(cursor.getColumnIndex(MyDataBase.UpdateDate)));
                    if (temp.after(lastUpdateTime)) {
                        lastUpdateTime = temp;
                        updateTime = cursor.getString(cursor.getColumnIndex(MyDataBase.UpdateDate));
                    }
                    count++;
                } while (cursor.moveToNext());

                Log.d(TAG, "loadProductFromDB: end time " + lastUpdateTime.getTime());
                Log.d(TAG, "loadProductFromDB: " + lastUpdateTime.toString());
                Log.d(TAG, "loadProductFromDB: update : " + updateTime);


                Log.d(TAG, "****loadProductFromDB****: Bulletin " + bulletinProduct.size());
                Log.d(TAG, "****loadProductFromDB****: Particular " + particularProduct.size());

            } else {
                Error = "Return False in else";
                return false;
            }

        } catch (Exception ex) {
            Error = ex.getMessage();
            return false;
        }
        if (count != 0)
            return true;

        Error = "Return False in else2";
        return false;
    }


    private Boolean synchronizeProductTableVer2(List<SendProductClass> NetProduct, boolean updateMode) {
        boolean result = true;
        //DatabaseService databaseService = new DatabaseService(mContext);
        long itemId;

        if (!updateMode) {
            dbViewModel.deleteAllProducts();
            dbViewModel.deleteAllProperties();
        }
        for (int i = 0; i < NetProduct.size(); i++) {
            final boolean[] mustUpdate = {false};
            if (updateMode) {
                //Product product = databaseService.getOneProduct(NetProduct.get(i).getProductID());
                dbViewModel.getOneProduct(NetProduct.get(i).getProductID()).observe((AppCompatActivity) mContext, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer productCount) {
                        if (productCount > 0)
                            mustUpdate[0] = true;
                    }
                });


            }
            if (mustUpdate[0])
                updateProduct(NetProduct.get(i), dbViewModel);
            else
                insertIntoDB(NetProduct.get(i), dbViewModel);

        }

        isLoadCompleted = true;
        return result;

    }

    private void insertIntoDB(SendProductClass NetProduct, DBViewModel dbViewModel) {

        Product product = new Product();
        product.ViewedCount = NetProduct.getViewedCount();
        product.UserID = NetProduct.getUserID();
        product.UpdateDate = NetProduct.getUpdateDate();
        product.Unit = NetProduct.getUnit();
        product.Token = NetProduct.getToken();
        product.TargetLevel = NetProduct.getTargetLevel();
        product.SupplierID = NetProduct.getSupplierID();
        product.StandardCost = NetProduct.getStandardCost();
        product.Show = NetProduct.getShow();
        product.SellCount = NetProduct.getSellCount();
        product.Saveit = NetProduct.getSaveit();
        product.SaveCount = NetProduct.getSaveCount();
        product.ReorderLevel = NetProduct.getReorderLevel();
        product.QuantityPerUnit = NetProduct.getQuantityPerUnit();
        product.ProductName = NetProduct.getProductName();
        product.ProductID = NetProduct.getProductID();
        product.MinimumReorderQuantity = NetProduct.getMinimumReorderQuantity();
        product.ListPrice = NetProduct.getListPrice();
        product.Likeit = NetProduct.isLikeit();
        product.LikeCount = NetProduct.getLikeCount();
        product.Discontinued = NetProduct.getDiscontinued();
        product.Description = NetProduct.getDescription();
        product.Deleted = NetProduct.getDeleted();
        product.CompanyID = NetProduct.getCompanyID();
        product.Category = NetProduct.getCategory();
        dbViewModel.insertProduct(product);

        for (ProductPropertisClass productPropertis : NetProduct.getProductPropertis()) {
            Properties properties = new Properties();
            properties.ProductID = productPropertis.getProductID();
            properties.PropertiesGroup = productPropertis.getPropertisGroup();
            properties.PropertiesName = productPropertis.getPropertisName();
            properties.PropertiesValue = productPropertis.getPropertisValue();
            properties.UpdateTime = productPropertis.getUpdatTime();
            dbViewModel.insertProperties(properties);
        }

    }

    private void updateProduct(SendProductClass productClass, DBViewModel dbViewModel) {
        Product product = new Product();
        product.Deleted = productClass.getDeleted();
        product.Category = productClass.getCategory();
        product.CompanyID = productClass.getCompanyID();
        product.Description = productClass.getDescription();
        product.Discontinued = productClass.getDiscontinued();
        product.LikeCount = productClass.getLikeCount();
        product.Likeit = productClass.isLikeit();
        product.ListPrice = productClass.getListPrice();
        product.MinimumReorderQuantity = productClass.getMinimumReorderQuantity();
        product.ProductID = productClass.getProductID();
        product.ProductName = productClass.getProductName();
        product.QuantityPerUnit = productClass.getQuantityPerUnit();
        product.ReorderLevel = productClass.getReorderLevel();
        product.SaveCount = productClass.getSaveCount();
        product.Saveit = productClass.getSaveit();
        product.SellCount = productClass.getSellCount();
        product.Show = productClass.getShow();
        product.StandardCost = productClass.getStandardCost();
        product.SupplierID = productClass.getSupplierID();
        product.TargetLevel = productClass.getTargetLevel();
        product.Token = productClass.getToken();
        product.Unit = productClass.getUnit();
        product.UpdateDate = productClass.getUpdateDate();
        product.UserID = productClass.getUserID();
        product.ViewedCount = productClass.getViewedCount();
        dbViewModel.updateProduct(product);
    }

    private void updateProperties(List<ProductPropertisClass> propertisList, DBViewModel dbViewModel) {
        for (ProductPropertisClass propertisClass : propertisList) {
            Properties properties = new Properties();
            properties.UpdateTime = propertisClass.getUpdatTime();
            properties.PropertiesValue = propertisClass.getPropertisValue();
            properties.PropertiesName = propertisClass.getPropertisName();
            properties.PropertiesGroup = propertisClass.getPropertisGroup();
            properties.ProductID = propertisClass.getProductID();
            dbViewModel.updateProperty(properties);
        }
    }

    private Single<Boolean> synchronizeProductTable(List<SendProductClass> NetProduct, boolean updateMode) {
        if (!updateMode) {
            Log.d(TAG, "synchronizeProductTable: " + Thread.currentThread().getName());
            ProductIDList.clear();
            return mydb.setAllProductEnabledAsync(baseCodeClass.getCompanyID())
                    .flatMap(integer -> insertDataToDb(NetProduct));
        }

        return insertDataToDb(NetProduct);
    }

    private Single<Boolean> insertDataToDb(List<SendProductClass> netProduct) {
        return Single.create((SingleOnSubscribe<Boolean>) emitter -> {
            for (SendProductClass productClass : netProduct
            ) {
                /*if (mydb.insertProduct(mContext, productClass.getCategory(),
                        productClass.getDeleted(), "false",
                        productClass.getDescription(), String.valueOf(productClass.getDiscontinued()),
                        "", productClass.getListPrice(), productClass.getMinimumReorderQuantity(),
                        productClass.getProductID(),
                        productClass.getProductName(), productClass.getQuantityPerUnit(),
                        productClass.getReorderLevel(), productClass.getShow(), productClass.getLikeCount(),
                        productClass.getViewedCount(), productClass.getSaveCount(), productClass.getStandardCost(),
                        productClass.getSupplierID(), productClass.getTargetLevel(), productClass.getUnit(),
                        productClass.getUpdateDate(), productClass.isLikeit(), productClass.getSaveit(), productClass.getSellCount()) != -2) {
                    ProductIDList.add(productClass.getProductID());
                } else {
                    String fname = myDir + "/" + productClass.getProductID() + ".jpg";
                    File file = new File(fname);
                    if (!file.exists()) {
                        ProductIDList.add(productClass.getProductID());
                    }
                }*/

                for (ProductPropertisClass propertisClass : productClass.getProductPropertis()
                ) {
                    mydb.insertProductProperties(mContext, "false", "", propertisClass.getProductID(),
                            propertisClass.getPropertisGroup(), propertisClass.getPropertisName(),
                            propertisClass.getPropertisValue(), "", "", "", propertisClass.getUpdatTime());
                }

            }
            mydb.DeletNotExsistProduct(mContext);
            //            this.EndOfList = true;
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * !
     * <p>
     * initial version of syncing tabales in main threas
     */
//    private void SycronizProductTable(List<SendProductClass> NetProduct, boolean updateMode) {
//        try {
//            if (!updateMode) {
//                ProductIDList.clear();
//                mydb.SetAllProductEnable(mContext, baseCodeClass.getCompanyID());
//            }
//            for (SendProductClass productClass : NetProduct
//            ) {
//                if (mydb.insertProduct(mContext, productClass.getCategory(),
//                        productClass.getDeleted(), "false",
//                        productClass.getDescription(), String.valueOf(productClass.getDiscontinued()),
//                        "", productClass.getListPrice(), productClass.getMinimumReorderQuantity(),
//                        productClass.getProductID(),
//                        productClass.getProductName(), productClass.getQuantityPerUnit(),
//                        productClass.getReorderLevel(), productClass.getShow(), productClass.getLikeCount(),
//                        productClass.getViewedCount(), productClass.getSaveCount(), productClass.getStandardCost(),
//                        productClass.getSupplierID(), productClass.getTargetLevel(), productClass.getUnit(),
//                        productClass.getUpdateDate(), productClass.getLikeit(), productClass.getSaveit(), productClass.getSellCount()) != -2) {
//                    ProductIDList.add(productClass.getProductID());
//                } else {
//                    String fname = myDir + "/" + productClass.getProductID() + ".jpg";
//                    File file = new File(fname);
//                    if (!file.exists()) {
//                        ProductIDList.add(productClass.getProductID());
//                    }
//                }
//                for (ProductPropertisClass propertisClass : productClass.getProductPropertis()
//                ) {
//                    Single<Long> single = mydb.insertProductProperties(mContext, "false", "", propertisClass.getProductID(),
//                            propertisClass.getPropertisGroup(), propertisClass.getPropertisName(),
//                            propertisClass.getPropertisValue(), "", "", "", propertisClass.getUpdatTime());
//                    single.subscribe(new SingleObserver<Long>() {
//                        @Override
//                        public void onSubscribe(@NonNull Disposable d) {
//
//                        }
//
//                        @Override
//                        public void onSuccess(@NonNull Long aLong) {
//                            if (aLong == -1)
//                                Toast.makeText(mContext, "خطایی رخ داد در وارد کردن ویژگی به دیتابیس", Toast.LENGTH_SHORT).show();
//                            Log.d(TAG, "onSuccess: " + aLong);
//                        }
//
//                        @Override
//                        public void onError(@NonNull Throwable e) {
//                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//
//            }
//            mydb.DeletNotExsistProduct(mContext);
////            this.EndOfList = true;
//        } catch (Exception ex) {
//
//        }
//
//    }
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

    private void updateData(String companyID) {
        try {
            UpdatedProductBody updatedProductBody = new UpdatedProductBody(companyID, baseCodeClass.getUserID(), updateTime);
            Call<List<SendProductClass>> call = productApi.getUpdatedData(updatedProductBody);
            call.enqueue(new Callback<List<SendProductClass>>() {
                @Override
                public void onResponse(Call<List<SendProductClass>> call, Response<List<SendProductClass>> response) {
                    synchronizeProductTableVer2(response.body(), true);
                   /* synchronizeProductTable(response.body(), true).subscribe(new SingleObserver<Boolean>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@NonNull Boolean aBoolean) {
                            if (aBoolean)
                                isLoadCompleted = true;
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.d(TAG + "synctables", "onError: ");
                        }
                    });*/
                    Log.d(TAG, "onResponse: ******update*******" + response.body().size());


                }

                @Override
                public void onFailure(Call<List<SendProductClass>> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });
        } catch (Exception ex) {
            baseCodeClass.logMessage(" loadProduct Error " + ex.getMessage(), mContext);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        // get the string from params, which is an array

        Log.d(TAG, "doInBackground: before try");
        try {
            Log.d(TAG, "doInBackground: isLoadCompleted " + isLoadCompleted);
            isLoadCompleted = false;
            Log.d(TAG, "doInBackground: isLoadCompleted" + isLoadCompleted);
            String myString = strings[0];
            Log.d(TAG, "doInBackground: myString" + myString);
            String MOD = strings[1];
            Log.d(TAG, "doInBackground: MOD " + MOD);
            Log.d(TAG, "doInBackground: before if");
            if (MOD.equals(BaseCodeClass.DownloadParam)) {
                Log.d(TAG, "doInBackground: ");
                ProductIDList.clear();
                if (loadProductFromRoomDB(myString)) {
                    updateData(myString);
                } else {
                    //mydb.deleteAllProduct(mContext);
                    LoadAllProduct(myString);
                }

                int m = 0;
//                while (ProductIDList.size() == 0)
                while (!isLoadCompleted) {
                    try {
                        m++;
                        Thread.sleep(200);
//                        if (EndOfList) break;
                        if (m > 30) break;
                    } catch (InterruptedException e) {
                        Log.d("Error", e.toString());
                        // We were cancelled; stop sleeping!
                    }

                    loadProductFromRoomDB(myString);
//                    callBackproductApi.onResponseLoadProduct(null);

                }
//                productDataList.clear();
//                LoadAllProduct(myString);
//                int m = 0;
//                while (productDataList.size() == 0) {
//                    try {
//                        m++;
//                        Thread.sleep(200);
//                        if (EndOfList) break;
//                        if (m > 30) break;
//                    } catch (InterruptedException e) {
//                        // We were cancelled; stop sleeping!
//                    }
//                }
//
//                isLoadCompleted = false;
//
//                loadProductFromDB();


                return MOD;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        //callBackproductApi.onResponseDownloadImage(null, null);
        // Do things like update the progress bar
    }

    // This runs in UI when background thread finishes
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            if (result.equals(baseCodeClass.DownloadParam)) {
                callBackproductApi.recyclerViewCanUpdating();
            } else {
                callBackproductApi.imageAdapterCanUpdating(null);
            }
            cancel(true);
        } catch (Exception ex) {
            Log.d("Error", ex.toString());
        }
        // Do things like hide the progress bar or change a TextView
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Do something like display a progress bar
    }


}
