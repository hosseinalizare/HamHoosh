package com.example.koohestantest1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.example.koohestantest1.ApiDirectory.LoadProductApi;
import com.example.koohestantest1.Utils.Cache;
import com.example.koohestantest1.Utils.FileUtils;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.ProductPropertiesRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.ProductPropertisClass;
import com.example.koohestantest1.classDirectory.SendProduct;
import com.example.koohestantest1.classDirectory.StandardPrice;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;

public class AddProductMainActivity extends AppCompatActivity {

    private AppCompatButton btnNext, btnAddPhoto;
    private TextInputEditText edtProductName, edtDiscount, edtPrice;
    private Spinner spProductSubCat, spProductMainCat, spProductUnit;
    private EditText edtProductSubCat, edtProductMainCat, edtDescription, edtInventory;
    private SwitchMaterial swCat;
    private CheckBox chkAddToStory, chkAddToBulletin, chkShowToCustomer;
    private RecyclerView rvProductProperties;
    private AutoCompleteTextView actPropertyValue, actProperty;
    private ImageButton ibtnAddProperty;
    private ViewFlipper vf;

    private boolean addToStory, addToBulletin, showToCustomer;
    private String[] unitList = {"کیلوگرم", "گرم", "متر", "لیتر", "بسته", "شانه", "دانه", "سایر"};
    private ArrayList<String> propertiesList, propertyValueList;
    private List<ProductPropertisClass> propertiesClassList;
    private String
            mainCat = "",
            subCat = "",
            price = "",
            discount = "",
            productName = "",
            unitValue = "",
            description = "",
            inventory = "";
    private List<Uri> imageUriList;
    private List<String> partNames;
    private SendProduct mainSendProductClass;
    private Retrofit retrofit;
    private LoadProductApi loadProductApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_main);
        retrofit = RetrofitInstance.getRetrofit();
        loadProductApi = retrofit.create(LoadProductApi.class);
        propertiesList = new ArrayList<>();
        propertyValueList = new ArrayList<>();
        imageUriList = new ArrayList<>();
        partNames = new ArrayList<>();
        propertiesClassList = new ArrayList<>();
        //**************************************************Define Views***************************/
        btnNext = findViewById(R.id.FirstAddProductBtnNext);
        btnAddPhoto = findViewById(R.id.SecondAddProductBtnAddPhoto);
        edtProductName = findViewById(R.id.FirstAddProductName);
        edtDiscount = findViewById(R.id.FirstAddProductDiscount);
        edtPrice = findViewById(R.id.FirstAddProductPrice);
        edtDescription = findViewById(R.id.SecondAddProductEdtDescription);
        edtInventory = findViewById(R.id.SecondAddProductEdtInventory);
        spProductMainCat = findViewById(R.id.FirstAddProductMainCatSpinner);
        spProductSubCat = findViewById(R.id.FirstAddProductCat1Spinner);
        spProductUnit = findViewById(R.id.SecondAddProductUnitSpinner);
        edtProductSubCat = findViewById(R.id.FirstAddProductEdtCat1);
        edtProductMainCat = findViewById(R.id.FirstAddProductEdtMainCat);
        swCat = findViewById(R.id.FirstAddProductSmCategory);
        chkAddToStory = findViewById(R.id.SecondAddProductCbAddToStory);
        chkAddToBulletin = findViewById(R.id.SecondAddProductCbAddToBulletin);
        chkShowToCustomer = findViewById(R.id.SecondAddProductCbShowToCustomers);
        rvProductProperties = findViewById(R.id.SecondAddProductRvProductProperties);
        actProperty = findViewById(R.id.SecondAddProductEdProperty);
        actPropertyValue = findViewById(R.id.SecondAddProductEdPropertyValue);
        ibtnAddProperty = findViewById(R.id.SecondAddProductBtnAddProperty);
        vf = findViewById(R.id.vfAddProduct);
        vf.setDisplayedChild(0);
        loadProductCategory();
        //*****************************************************************************************/

        /**
         * These methods are for first page
         */
        //***************************************Next Button ClickListener*************************/
        btnNext.setOnClickListener(v -> {
            if (edtProductName.getText().toString().equals(""))
                Toast.makeText(this, "نام محصول را وارد کنید", Toast.LENGTH_SHORT).show();
            else if (edtPrice.getText().toString().equals(""))
                Toast.makeText(this, "قیمت محصول را وارد کنید", Toast.LENGTH_SHORT).show();
            else if ((edtProductMainCat.getText().toString().equals("") || edtProductSubCat.getText().toString().equals("")) &&
                    (mainCat.equals("") || subCat.equals("")))
                Toast.makeText(this, "دسته بندی محصول خود را مشخص کنید", Toast.LENGTH_SHORT).show();
            else {
                productName = edtProductName.getText().toString();
                price = edtPrice.getText().toString();
                discount = edtDiscount.getText().toString();
                vf.setDisplayedChild(1);
            }
        });
        //*****************************************************************************************/

        //************************************Category Switch CheckedChangeListener****************/
        swCat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                spProductSubCat.setVisibility(View.INVISIBLE);
                spProductMainCat.setVisibility(View.INVISIBLE);
                edtProductMainCat.setVisibility(View.VISIBLE);
                edtProductSubCat.setVisibility(View.VISIBLE);
            } else {
                spProductSubCat.setVisibility(View.VISIBLE);
                spProductMainCat.setVisibility(View.VISIBLE);
                edtProductMainCat.setVisibility(View.INVISIBLE);
                edtProductSubCat.setVisibility(View.INVISIBLE);
            }
        });
        //*****************************************************************************************/

        //************************************MainCat spinner ItemSelectedListener*****************/





        spProductMainCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainCat = spProductMainCat.getItemAtPosition(position).toString();
                loadProductSubCategory(mainCat);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //*****************************************************************************************/

        //************************************SubCat spinner ItemSelectedListener******************/
        spProductSubCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subCat = spProductSubCat.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //*****************************************************************************************/

        /**
         * Here first page's methods finished
         * These methods are for second page
         */
        //*************************initialize unit spinner and set value of selected Item**********/
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, unitList);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProductUnit.setAdapter(unitAdapter);
        spProductUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unitValue = spProductUnit.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //*****************************************************************************************/
        //****************************show to customer checkbox CheckedChangeListener**************/
        chkShowToCustomer.setOnCheckedChangeListener((buttonView, isChecked) -> {
            showToCustomer = isChecked;
        });
        //*****************************************************************************************/

        //****************************add to bulletin checkbox CheckedChangeListener***************/
        chkAddToBulletin.setOnCheckedChangeListener((buttonView, isChecked) -> {
            addToBulletin = isChecked;
        });
        //*****************************************************************************************/

        //****************************add to story checkbox CheckedChangeListener******************/
        chkAddToStory.setOnCheckedChangeListener((buttonView, isChecked) -> {
            addToStory = isChecked;
        });
        //*****************************************************************************************/

        //****************************add product's property ClickListener*************************/
        ibtnAddProperty.setOnClickListener(v -> {
            if (actProperty.getText().toString().equals("") ||
                    actPropertyValue.getText().toString().equals(""))
                Toast.makeText(this, "ویژگی محصول خود را وارد کنید", Toast.LENGTH_SHORT).show();
            else {
                propertiesClassList.add(new ProductPropertisClass(null,
                        "مشخصات اصلی", actProperty.getText().toString(),
                        actPropertyValue.getText().toString(), null));
                propertiesList.add(actProperty.getText().toString());
                propertyValueList.add(actPropertyValue.getText().toString());
                actProperty.setText("");
                actPropertyValue.setText("");
                initRecyclerView();
            }
        });
        //*****************************************************************************************/

        //*************************Fetch data From Gallery and send them to server*****************/
        ActivityResultLauncher<Intent> mLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        if (result.getData() != null) {
                            if (result.getData().getClipData() != null) {
                                int count = result.getData().getClipData().getItemCount();
                                int currentItem = 0;
                                while (currentItem < count) {
                                    Uri imageUri = result.getData().getClipData().getItemAt(currentItem).getUri();
                                    Uri uri = CropImage.getPickImageResultUri(getApplicationContext(),result.getData().getClipData().getItemAt(currentItem).getIntent());
                                    cropRequest(uri);
                                    imageUriList.add(imageUri);
                                    partNames.add(currentItem + "");
                                    currentItem++;
                                }
                                StandardPrice standardPrice = new StandardPrice();
                                standardPrice.setShowStandardCost(price);
                                standardPrice.setStandardCost(Integer.parseInt(price));
                                if (!edtDiscount.getText().toString().equals("")) {
                                    standardPrice.setShowoffPrice(edtDiscount.getText().toString());
                                    standardPrice.setOffPrice(Integer.parseInt(edtDiscount.getText().toString()));
                                }
                                if (mainCat.equals(""))
                                    mainCat = edtProductMainCat.getText().toString();
                                if (subCat.equals(""))
                                    subCat = edtProductSubCat.getText().toString();
                                if (authorizeCurrentProduct())
                                    addProduct();
                            }
                        }
                    }
                }
        );
        //*****************************************************************************************/

        //****************************Get required permissions and open Gallery********************/
        ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                result -> {
                    if (result) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        Intent chooserIntent = Intent.createChooser(intent, "Open Gallery");
                        mLauncher.launch(chooserIntent);
                    }
                }
        );
        //*****************************************************************************************/
        //****************************add product's photo ClickListener****************************/

        btnAddPhoto.setOnClickListener(v -> {
            
            if (propertyValueList.size() != 0){
                if (!edtDescription.getText().toString().equals(""))
                    description = edtDescription.getText().toString();
                if (edtInventory.getText().toString().equals(""))
                    inventory = edtInventory.getText().toString();

                mPermissionResult.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }else {
                Toast.makeText(this, "حداقل یک ویژگی برای محصول ثبت کنید!", Toast.LENGTH_SHORT).show();
            }
            
           

        });

        //*****************************************************************************************/
        /**
         * End of second page's methods
         */
    }

    //***************************************Methods***********************************************/
    private void loadProductCategory() {

        Call<List<String>> call = loadProductApi.getCategory(Integer.parseInt(BaseCodeClass.companyProfile.CompanyType));
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, response.body());
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spProductMainCat.setAdapter(adapter);
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
                spProductSubCat.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    private void initRecyclerView() {
        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            layoutManager.setReverseLayout(true);

            rvProductProperties.setLayoutManager(layoutManager);
            ProductPropertiesRecyclerViewAdapter adapter = new ProductPropertiesRecyclerViewAdapter(this, propertiesList, propertyValueList, 2);
            rvProductProperties.setAdapter(adapter);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    private boolean checkEmptyString(String value) {
        if (value == null)
            return true;

        if (value.equals("") || value.isEmpty())
            return true;

        return false;
    }

    private boolean authorizeCurrentProduct() {
        String reOrder = "0";
        boolean showToUser = chkShowToCustomer.isChecked();

        if (chkAddToStory.isChecked() && chkAddToBulletin.isChecked()) {
            reOrder = "200";
        } else if (chkAddToStory.isChecked() && !chkAddToBulletin.isChecked()) {
            reOrder = "100";
        } else if (!chkAddToStory.isChecked() && chkAddToBulletin.isChecked()) {
            reOrder = "150";
        } else if (!chkAddToStory.isChecked() && !chkAddToBulletin.isChecked()) {
            reOrder = "1";
        }

        String productName = edtProductName.getText().toString();
        if (checkEmptyString(productName)) {
            Toast.makeText(this, "نام محصول نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
            return false;
        }
        String description = edtDescription.getText().toString();
        if (checkEmptyString(description)) {
            Toast.makeText(this, "توضیحات نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
            return false;
        }

        StandardPrice standardCost = new StandardPrice();
        standardCost.setShowStandardCost(edtPrice.getText().toString());
        standardCost.setStandardCost(Integer.parseInt(edtPrice.getText().toString()));
        if(!edtDiscount.getText().toString().equals(""))
            standardCost.setOffPrice(Integer.parseInt(edtDiscount.getText().toString()));
        if (checkEmptyString(edtPrice.getText().toString())) {
            Toast.makeText(this, "قیمت نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
            return false;
        }

        String listPrice = edtDiscount.getText().toString();
//        if (checkEmptyString(listPrice)) {
//            Toast.makeText(this, "قیمت  نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        if (checkEmptyString(unitValue)) {
            Toast.makeText(this, "واحد نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
            return false;
        }

        String stock = edtInventory.getText().toString();
        if (checkEmptyString(stock)) {
            Toast.makeText(this, "موجودی نمیتواند خالی باشد", Toast.LENGTH_SHORT).show();
            return false;
        }
        int Count = Integer.parseInt(stock);

        if (checkEmptyString(mainCat) || checkEmptyString(subCat)) {
            Toast.makeText(this, "دسته بندی محصول نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (propertiesClassList == null || propertiesClassList.size() == 0) {
            Toast.makeText(this, "به مشخصات اصلی کالا حداقل یک مورد اضافه کنید.", Toast.LENGTH_SHORT).show();
            return false;
        }

        mainSendProductClass = new SendProduct(BaseCodeClass.token, BaseCodeClass.userID,
                BaseCodeClass.CompanyID, BaseCodeClass.CompanyID, null, productName,
                description, standardCost.getStandardCost(), standardCost.getOffPrice(), Integer.parseInt(reOrder),
                10, unitValue, "1", Count, 1,
                mainCat + "." + subCat, true, false, null,
                null, null, 0, false, false, false,
                BaseCodeClass.userID, null, null, false, propertiesClassList);


        return true;
    }

    public void addProduct() {

        try {
            if (mainSendProductClass != null) {
                if (mainSendProductClass.getListPrice() <= 0)
                    mainSendProductClass.setListPrice(0);
                Call<GetResualt> call = loadProductApi.sendProductDetail(mainSendProductClass);
                Log.d("Success", "editProduct: " + mainSendProductClass.getCompanyID());
                call.enqueue(new Callback<GetResualt>() {
                    @Override
                    public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                        List<MultipartBody.Part> files = new ArrayList<>();

                        GetResualt getResualt = new GetResualt(response.body().getResualt(), response.body().getMsg());
                        String productId = getResualt.getMsg();
                        if (imageUriList != null){
                            files = convertUriToFIle(partNames,imageUriList,productId);
                        }
                            Call<GetResualt> uploadImageCall = loadProductApi.uploadMultiProductImage(
                                    productId,
                                    BaseCodeClass.CompanyID,
                                    BaseCodeClass.userID,
                                    BaseCodeClass.token, files);
                        uploadImageCall.enqueue(new Callback<GetResualt>() {
                            @Override
                            public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                                if(response.body().getResualt().equals("100"))
                                Toast.makeText(getApplicationContext(), "ذخیره با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getApplicationContext(), "مشکل در ذخیره سازی", Toast.LENGTH_SHORT).show();
                            }


                            @Override
                            public void onFailure(Call<GetResualt> call, Throwable t) {
                                Log.d("Error",t.getMessage());
                            }
                        });
                        finish();
                    }

                    @Override
                    public void onFailure(Call<GetResualt> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else
                Toast.makeText(this, "خطای مقدار دهی محصول", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("Catch add product >> ", e.getMessage());
        }
    }

    private void cropRequest(Uri uri) {
        try {
            CropImage.activity(uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setMultiTouchEnabled(true)
                    .start(this);
        } catch (Exception e) {
            logMessage("AddProduct 399 >> " + e.getMessage(), this);
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
}