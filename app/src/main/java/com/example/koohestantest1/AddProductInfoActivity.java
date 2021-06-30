package com.example.koohestantest1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.koohestantest1.ApiDirectory.LoadProductApi;
import com.example.koohestantest1.Utils.Cache;
import com.example.koohestantest1.Utils.FileUtils;
import com.example.koohestantest1.Utils.StringUtils;
import com.example.koohestantest1.adapter.ProductImageRecyclerAdapter;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.ProductPropertiesRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.ProductPropertisClass;
import com.example.koohestantest1.classDirectory.SendProduct;
import com.example.koohestantest1.local_db.DBViewModel;
import com.example.koohestantest1.local_db.entity.Product;
import com.example.koohestantest1.local_db.entity.Properties;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.selectedProduct;

public class AddProductInfoActivity extends AppCompatActivity {

    private SendProduct sendProduct;
    private String productName;
    private boolean isShow,isParticular,isBulletin;
    private ArrayList<ProductPropertisClass> productPropertisClasses;
    private ArrayList<Uri> imageUriList;
    private ArrayList<String> productNames;
    private ImageButton imgbCancel,imgbDone,imgbAddProperty;
    private ProgressBar pbLoading;
    private RecyclerView rvImages,rvProperties;
    private TextInputEditText edtProductName,edtPrice,edtOff;
    private Spinner spCat1,spMainCat,spCat2,spUnit;
    private EditText edtCat1,edtMainCat,edtDescription,edtInventory;
    private SwitchMaterial smCategory;
    private CheckBox chkAddToStory,chkAddToBulletin,chkShowToCustomer;
    private AutoCompleteTextView actxtPropertyValue,actxtPropertyName;
    private Button btnAddProduct;
    private ProductImageRecyclerAdapter imageAdapter;
    private ProductPropertiesRecyclerViewAdapter propertiesAdapter;
    private ArrayList<String> propertyNames,propertyValues;
    private String[] unitList = {"کیلوگرم", "گرم", "متر", "لیتر", "بسته", "شانه", "دانه", "سایر"};
    private String unitValue,mainCat,subCat,reOrder = "0";

    private Retrofit retrofit;
    private LoadProductApi loadProductApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = RetrofitInstance.getRetrofit();
        loadProductApi = retrofit.create(LoadProductApi.class);
        //*****************************************Define Views************************************/
        setContentView(R.layout.activity_add_product_info);
        imgbCancel = findViewById(R.id.imgbtn_addProductInfoActivity_cancel);
        imgbDone = findViewById(R.id.imgbtn_addProductInfoActivity_done);
        imgbAddProperty = findViewById(R.id.btn_addProductInfoActivity_addProperty);
        rvImages = findViewById(R.id.rv_addProductInfoActivity_images);
        rvProperties = findViewById(R.id.rv_addProductInfoActivity_properties);
        edtProductName = findViewById(R.id.edt_addProductInfoActivity_productName);
        edtPrice = findViewById(R.id.edt_addProductInfoActivity_price);
        edtOff = findViewById(R.id.edt_addProductInfoActivity_productOff);
        spCat1 = findViewById(R.id.sp_addProductInfoActivity_cat1);
        spMainCat = findViewById(R.id.sp_addProductInfoActivity_mainCat);
        spCat2 = findViewById(R.id.sp_addProductInfoActivity_cat2);
        spUnit = findViewById(R.id.sp_addProductInfoActivity_unit);
        edtCat1 = findViewById(R.id.edt_addProductInfoActivity_cat1);
        edtMainCat = findViewById(R.id.edt_addProductInfoActivity_mainCat);
        edtDescription = findViewById(R.id.edt_addProductInfoActivity_description);
        edtInventory = findViewById(R.id.edt_addProductInfoActivity_inventory);
        smCategory = findViewById(R.id.sm_addProductInfoActivity_category);
        chkAddToStory = findViewById(R.id.chk_addProductInfoActivity_addToStory);
        chkAddToBulletin = findViewById(R.id.chk_addProductInfoActivity_addToBulletin);
        chkShowToCustomer = findViewById(R.id.chk_addProductInfoActivity_showToCustomers);
        actxtPropertyValue = findViewById(R.id.edt_addProductInfoActivity_propertyValue);
        actxtPropertyName = findViewById(R.id.edt_addProductInfoActivity_property);
        btnAddProduct = findViewById(R.id.btn_addProductInfoActivity_addProduct);
        pbLoading = findViewById(R.id.pb_addProductInfoActivity_loading);
        //*****************************************************************************************/

        //***************************************Get Data from other activity**********************/
        Bundle bundle = getIntent().getExtras();
        productPropertisClasses = new ArrayList<>();
        imageUriList = new ArrayList<>();
        productNames = new ArrayList<>();
        propertyNames = new ArrayList<>();
        propertyValues = new ArrayList<>();
        imageUriList = (ArrayList<Uri>) bundle.get("uriList");
        productPropertisClasses = (ArrayList<ProductPropertisClass>) bundle.get("propertiesList");
        productNames = (ArrayList<String>) bundle.get("productName");
        isShow = bundle.getBoolean("isShow");
        isBulletin = bundle.getBoolean("isBulletin");
        isParticular = bundle.getBoolean("isParticular");
        sendProduct = bundle.getParcelable("product");
        productName = sendProduct.getProductName();
        unitValue = sendProduct.getUnit();
        for(ProductPropertisClass property:productPropertisClasses){
            propertyNames.add(property.getPropertisName());
            propertyValues.add(property.getPropertisValue());
        }
        loadProductCategory();
        initPropertyRecyclerView();
        //***************************************Initialize RecyclerView***************************/
        imageAdapter = new ProductImageRecyclerAdapter(imageUriList,this);
        rvImages.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        rvImages.setAdapter(imageAdapter);
        //*****************************************************************************************/

        //***************************************Initialize other views****************************/
        edtProductName.setText(sendProduct.getProductName());
        edtInventory.setText(sendProduct.getDiscontinued()+"");
        edtDescription.setText(sendProduct.getDescription());
        edtOff.setText(sendProduct.getListPrice()+"");
        edtPrice.setText(sendProduct.getStandardCost()+"");
        String[] cat = sendProduct.getCategory().split("\\.");
        edtMainCat.setText(cat[0]);
        edtCat1.setText(cat[1]);
        chkShowToCustomer.setChecked(sendProduct.isShow());
        chkAddToBulletin.setChecked(isBulletin);
        chkShowToCustomer.setChecked(isShow);
        chkAddToStory.setChecked(isParticular);
        //*****************************************************************************************/

        //*********************************************Initialize unit spinner*********************/
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, unitList);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUnit.setAdapter(unitAdapter);
        int count = 0;
        for(int i = 0;i < unitList.length;i++){
            if(unitList[i].equals(sendProduct.getUnit()))
                count = i;
        }
        spUnit.setSelection(count);
        spUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unitValue = spUnit.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //*****************************************************************************************/

        spMainCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainCat = spMainCat.getItemAtPosition(position).toString();
                loadProductSubCategory(mainCat);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //*****************************************************************************************/

        //************************************SubCat spinner ItemSelectedListener******************/
        spCat1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subCat = spCat1.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //*****************************************************************************************/

        chkAddToStory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isParticular = isChecked;
            }
        });

        chkShowToCustomer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isShow = isChecked;
            }
        });

        chkAddToBulletin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isBulletin = isChecked;
            }
        });

        //*****************************************************************************************/

        imgbAddProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actxtPropertyName.getText().toString().equals("") ||
                        actxtPropertyValue.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "ویژگی محصول خود را وارد کنید", Toast.LENGTH_SHORT).show();
                else {
                    productPropertisClasses.add(new ProductPropertisClass(null,
                            "مشخصات اصلی", actxtPropertyName.getText().toString(),
                            actxtPropertyValue.getText().toString(), null));
                    productNames.add(actxtPropertyName.getText().toString());
                    propertyValues.add(actxtPropertyValue.getText().toString());
                    actxtPropertyName.setText("");
                    actxtPropertyValue.setText("");
                    initPropertyRecyclerView();
                }
            }
        });
        //*****************************************************************************************/

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbLoading.setVisibility(View.VISIBLE);
                authorizeCurrentProduct();
                sendProduct.setReorderLevel(Integer.parseInt(reOrder));
                if(sendProduct.getSpare1() == null)
                    sendProduct.setSpare1("#FFFFFF");
                if(sendProduct.getSpare2() == null)
                    sendProduct.setSpare2("#FFFFFF");
                if(sendProduct.getSpare3() == null)
                    sendProduct.setSpare3("#FFFFFF");
                sendProduct.setProductPropertis(productPropertisClasses);
                addProduct();
            }
        });


    }
    //************************************Methods**********************************************/
    private void loadProductCategory() {

        Call<List<String>> call = loadProductApi.getCategory(Integer.parseInt(BaseCodeClass.companyProfile.CompanyType));
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, response.body());
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spMainCat.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    private void loadProductSubCategory(String mainCat) {
        Call<List<String>> call = loadProductApi.getSubCatOne(Integer.parseInt(BaseCodeClass.companyProfile.CompanyType), mainCat);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, response.body());
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spCat1.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    private void initPropertyRecyclerView(){
        propertiesAdapter = new ProductPropertiesRecyclerViewAdapter(this,propertyNames,propertyValues,2);
        rvProperties.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rvProperties.setAdapter(propertiesAdapter);
    }

    private void authorizeCurrentProduct() {

        if (chkAddToStory.isChecked() && chkAddToBulletin.isChecked()) {
            reOrder = "200";
        } else if (chkAddToStory.isChecked() && !chkAddToBulletin.isChecked()) {
            reOrder = "100";
        } else if (!chkAddToStory.isChecked() && chkAddToBulletin.isChecked()) {
            reOrder = "150";
        } else if (!chkAddToStory.isChecked() && !chkAddToBulletin.isChecked()) {
            reOrder = "1";
        }
    }

    private void addProduct() {
        int a = 23;
        try {
            if (sendProduct != null) {
                if (sendProduct.getListPrice() <= 0)
                    sendProduct.setListPrice(0);
                Call<GetResualt> call = loadProductApi.sendProductDetail(sendProduct);
                Log.d("Success", "editProduct: " + sendProduct.getCompanyID());
                call.enqueue(new Callback<GetResualt>() {
                    @Override
                    public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                        List<MultipartBody.Part> files = new ArrayList<>();

                        GetResualt getResualt = new GetResualt(response.body().getResualt(), response.body().getMsg());
                        String productId = getResualt.getMsg();
                        if (imageUriList != null){
                            files = convertUriToFIle(productNames,imageUriList,productId);
                        }
                        Call<GetResualt> uploadImageCall = loadProductApi.uploadMultiProductImage(
                                productId,
                                BaseCodeClass.CompanyID,
                                BaseCodeClass.userID,
                                BaseCodeClass.token, files);
                        uploadImageCall.enqueue(new Callback<GetResualt>() {
                            @Override
                            public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                                if(response.body().getResualt().equals("100")) {
                                    Toast.makeText(getApplicationContext(), "ذخیره با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                                    pbLoading.setVisibility(View.GONE);
                                    finish();
                                }else {
                                    Toast.makeText(getApplicationContext(), "مشکل در ذخیره سازی", Toast.LENGTH_SHORT).show();
                                    pbLoading.setVisibility(View.GONE);
                                }
                            }


                            @Override
                            public void onFailure(Call<GetResualt> call, Throwable t) {
                                pbLoading.setVisibility(View.GONE);
                                Log.d("Error",t.getMessage());
                            }
                        });

                    }

                    @Override
                    public void onFailure(Call<GetResualt> call, Throwable t) {
                        if(pbLoading.getVisibility() == View.VISIBLE)
                            pbLoading.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "خطای مقدار دهی محصول", Toast.LENGTH_SHORT).show();
                pbLoading.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            pbLoading.setVisibility(View.GONE);
            Log.d("Catch add product >> ", e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private List<MultipartBody.Part> convertUriToFIle(List<String> partNames, List<Uri> imageUriList, String newsId) {
        List<MultipartBody.Part> files = new ArrayList<>();
        for (int i = 0; i < imageUriList.size(); i++) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUriList.get(i));
                Cache cache = new Cache(getApplicationContext());
                File imageFile = cache.saveToCacheAndGetFile2(bitmap, newsId);
                long fileSize = imageFile.length() / 1024;
                int quality = (int) Math.abs((fileSize - 3775) / 61.25);
                if (quality < 20)
                    quality = 20;
                else if (quality > 100)
                    quality = 100;

                Bitmap imageBitmap = new Compressor(getApplicationContext())
                        .setMaxWidth(1080)
                        .setMaxHeight(1080)
                        .setQuality(quality)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .compressToBitmap(imageFile);
                Cache cacheCompressed = new Cache(getApplicationContext());
                File compressedFile = cacheCompressed.saveToCacheAndGetFile2(imageBitmap, newsId + i);
                //File file = FileUtils.getFile(getContext(),imageUriList.get(i));
                RequestBody requestFile = RequestBody.create(MediaType.parse(FileUtils.MIME_TYPE_IMAGE), compressedFile);
                files.add(MultipartBody.Part.createFormData(partNames.get(i), compressedFile.getName(), requestFile));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return files;
    }

    //*****************************************************************************************/
}