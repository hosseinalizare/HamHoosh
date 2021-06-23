package com.example.koohestantest1;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.example.koohestantest1.Utils.Cache;
import com.example.koohestantest1.Utils.NumberTextChanger;
import com.example.koohestantest1.classDirectory.SendProduct;
import com.example.koohestantest1.classDirectory.StandardPrice;
import com.example.koohestantest1.local_db.entity.Product;
import com.example.koohestantest1.model.DeleteProduct;
import com.example.koohestantest1.model.UpdatedProductBody;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.google.android.gms.common.util.AndroidUtilsLight;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.koohestantest1.ApiDirectory.LoadProductApi;
import com.example.koohestantest1.ViewModels.BookMarkViewModel;
import com.example.koohestantest1.ViewModels.PostLikeViewModel;
import com.example.koohestantest1.ViewModels.PostViewViewModel;
import com.example.koohestantest1.classDirectory.AddProductImageRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.GetPropertisOfCompanyProducts;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.ProductPropertiesRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.ProductPropertisClass;
import com.example.koohestantest1.classDirectory.SendDeleteProduct;
import com.example.koohestantest1.classDirectory.ReceiveProductClass;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.selectedProduct;
import static com.nostra13.universalimageloader.utils.StorageUtils.getCacheDirectory;

public class AddProductActivity extends AppCompatActivity {

    private Context mContext = AddProductActivity.this;
    public static final int PICK_IMAGE = 123;
    public static final int CAMERA_REQUEST_CODE = 5;
    public static final int CAMERA_PERMISSION_CODE = 101;
    String productID;

//    String root = Environment.getExternalStorageDirectory().toString();
//    File myDir = new File(root + "/Dehkade/File");
//    String imagePath;

    LoadingDialog loadingDialog;

    ImageView imageProduct;
    EditText EdProductName;
    EditText EdProductPrice, EdDiscount, EdInventory;
    EditText EdProductDescription, EdCategory, EdProduct;
    SwitchMaterial swCategory;
    Spinner MainCat, Cat1, Cat2, Unit;
    AutoCompleteTextView EdProperty, EdPropertyValue;
    CheckBox cbStory, cbBulletin;
    Button btnAdd;
    Button btnDelete;
    LoadProductApi loadProductApi;
    LoadProductApi callBack;
    BaseCodeClass baseCodeClass;

    ImageView imageView;

    String MainCategory;
    String SubCat1, SubCat2;

    List<String> mMainCat = new ArrayList<>();
    boolean loadEdit = false;

    private ArrayList<String> mProperties = new ArrayList<>();
    private ArrayList<String> mPropertyValues = new ArrayList<>();
    private ArrayList<Bitmap> mAddImage = new ArrayList<>();

    private String[] mProperty;// = new String[3];

    String[] unit = {"کیلوگرم", "گرم", "متر", "لیتر", "بسته", "شانه", "دانه", "سایر"};

    String UnitValue;

    private List<Uri> imageUri = new ArrayList<>();
    private AddProductImageRecyclerViewAdapter rvAdapter = new AddProductImageRecyclerViewAdapter();

    private String TAG = AddProductActivity.class.getSimpleName();
    private CheckBox cbShowToUser;
    private TextView txtShowToUser;
    private SendProduct mainSendProductClass;

    private Bitmap mainBitmap = null;
    private static final int CHOOSE_IMAGE_CODE= 13784;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        try {
            //dataBase = new DataBase(mContext);
            baseCodeClass = new BaseCodeClass();
            baseCodeClass.LoadBaseData(mContext);

//            //make directory
//            myDir.mkdirs();

            final Retrofit retrofit = RetrofitInstance.getRetrofit();
/*
            final Retrofit retrofit2 = MyApiClient.getRetrofitTest();
*/


            loadProductApi = retrofit.create(LoadProductApi.class);
/*
            loadProductApi2 = retrofit.create(LoadProductApi.class);
*/

            EdProductName = (EditText) findViewById(R.id.AddProductName);
            EdProductPrice = (EditText) findViewById(R.id.AddProductPrice);
            EdProductDescription = (EditText) findViewById(R.id.AddProductDescription);
            EdDiscount = (EditText) findViewById(R.id.AddProductDiscount);
            EdInventory = (EditText) findViewById(R.id.AddProductInventory);
            EdCategory = findViewById(R.id.edtMainCat);
            EdProduct = findViewById(R.id.edtCat1);
            swCategory = findViewById(R.id.smCategory);
            MainCat = (Spinner) findViewById(R.id.MainCatSpinner);
            Cat1 = (Spinner) findViewById(R.id.Cat1Spinner);
            Cat2 = (Spinner) findViewById(R.id.Cat2Spinner);
            Unit = (Spinner) findViewById(R.id.UnitSpinner);
            EdProperty = (AutoCompleteTextView) findViewById(R.id.edProperty);
            EdPropertyValue = (AutoCompleteTextView) findViewById(R.id.edPropertyValue);
            cbStory = findViewById(R.id.cbAddToStory);
            cbBulletin = findViewById(R.id.cbAddToBulletin);
            btnAdd = findViewById(R.id.btnAddProduct);
            btnDelete = findViewById(R.id.btnDeleteProduct);
            cbShowToUser = findViewById(R.id.cb_show_to_customers);
            txtShowToUser = findViewById(R.id.txtShowToUser);

            boolean publishProductPermission = baseCodeClass.getPermissions().get(BaseCodeClass.EmploeeAccessLevel.EditProductDiscont.getValue()).isState();
            if (!publishProductPermission) {
                cbShowToUser.setChecked(false);
                cbShowToUser.setVisibility(View.GONE);
                txtShowToUser.setVisibility(View.GONE);
            } else {
                cbShowToUser.setVisibility(View.VISIBLE);
                txtShowToUser.setVisibility(View.VISIBLE);
            }


            String mobileLangFormat = Locale.getDefault().getLanguage();
            //we should add comma otherwise persian font(koodak) does it for us
            if (!mobileLangFormat.equals("fa")) {
                EdProductPrice.addTextChangedListener(new NumberTextChanger(EdProductPrice));
                EdDiscount.addTextChangedListener(new NumberTextChanger(EdDiscount));
            }

            loadingDialog = new LoadingDialog(AddProductActivity.this);

            ArrayAdapter<String> unitAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, unit);
            unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Unit.setAdapter(unitAdapter);

            imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setOnClickListener(v -> CropImage.startPickImageActivity(AddProductActivity.this));
/*
            imageView.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                intent.addCategory(Intent.CATEGORY_OPENABLE);
                //setType to image/* so that only images will show up
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, CHOOSE_IMAGE_CODE);
            });
*/


            String pid = getIntent().getStringExtra("PID");

            if (pid != null && !pid.equals(""))
                loadEdit = true;
            if (loadEdit)
                btnDelete.setVisibility(View.VISIBLE);
            else
                btnDelete.setVisibility(View.GONE);

            callBack = new LoadProductApi() {


                @Override
                public Call<GetResualt> sendProductDetail(SendProduct sendProductClass) {
                    return null;
                }

                @Override
                public void onResponseSendProduct(GetResualt getResualt) {
                    try {
                        String result = getResualt.getResualt();
                        toastMessage(getResualt.getMsg());
                        if (result.equals("100")) {
                            if (loadEdit) {
                                String root = getCacheDirectory(mContext).getPath();
//                                imagePath = root + "/Dehkade/File/" + productID + ".jpg";
                                sendImageProduct(productID);
                            } else {
                                productID = getResualt.getMsg();
                                sendImageProduct(productID);
                            }
                            finish();
                        } else {
                            toastMessage(getResualt.getMsg());
                            baseCodeClass.ActionRes(getResualt);
                            loadingDialog.dismissDialog();
                        }
                    } catch (Exception e) {
                        Log.d("1500", e.getMessage());
                    }
                }

                @Override
                public Call<GetResualt> uploadProductImage(String prId, String coId, String uID, String token,
                                                           MultipartBody.Part file) {
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
                    if (getResualt.getResualt().equals("100")) {
                        //toastMessage("delete ok");
                    } else {
                        baseCodeClass.ActionRes(getResualt);
                        toastMessage(getResualt.getResualt());
                    }
                }

                @Override
                public Call<ResponseBody> downloadImage(String PId, String fileNumber) {
                    return null;
                }

                @Override
                public void onResponseDownloadImage(ResponseBody responseBody, String pid) {

                }

                @Override
                public Call<List<String>> getCategory(int companyType) {
                    return null;
                }

                @Override
                public void onResponseGetCategory(List<String> cat) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,
                            cat);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    MainCat.setAdapter(adapter);
                    mMainCat = cat;
                    if (loadEdit) {
                        String[] category = selectedProduct.Category.split("\\.");
                        if (category.length > 0) {
                            MainCat.setSelection(mMainCat.indexOf((String) category[0]));
                        } else {
                            logMessage(selectedProduct.Category, mContext);
                        }
                    }
                }

                @Override
                public Call<List<String>> getSubCatOne(int companytype, String mainCat) {
                    return null;
                }

                @Override
                public void onResponseGetSubCatOne(List<String> subCat1) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,
                            subCat1);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Cat1.setAdapter(adapter);

                    if (loadEdit) {
                        String[] category = selectedProduct.Category.split("\\.");
                        if (category.length > 0) {
                            Cat1.setSelection(subCat1.indexOf((String) category[1]));
                        } else {
                            baseCodeClass.logMessage(selectedProduct.Category, mContext);
                        }
                    }
                }

                @Override
                public Call<List<GetPropertisOfCompanyProducts>> getProperty(int companytype) {
                    return null;
                }

                @Override
                public void onResponseGetProperty(List<GetPropertisOfCompanyProducts> propertisOfCompanyProducts) {
                    int i = 0;
                    mProperty = new String[propertisOfCompanyProducts.size()];

                    for (GetPropertisOfCompanyProducts s : propertisOfCompanyProducts
                    ) {
                        mProperty[i] = s.getPropertisName();
                        i++;
                    }

                    ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,
                            mProperty);

                    EdProperty.setAdapter(adapter);
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
                public void recyclerViewCanUpdating(List<Product> products) {

                }



                @Override
                public void imageAdapterCanUpdating(String imagePID) {

                }

                @Override
                public Call<GetResualt> deleteProduct(DeleteProduct deleteProduct) {
                    return null;
                }


            };

            loadMainCategory();
            loadProperty();

            EdProperty.setOnFocusChangeListener((v, hasFocus) -> EdProperty.showDropDown());

            Unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    UnitValue = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            swCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        MainCat.setVisibility(View.INVISIBLE);
                        Cat1.setVisibility(View.INVISIBLE);
                        EdCategory.setVisibility(View.VISIBLE);
                        EdProduct.setVisibility(View.VISIBLE);
                    } else {
                        MainCat.setVisibility(View.VISIBLE);
                        Cat1.setVisibility(View.VISIBLE);
                        EdCategory.setVisibility(View.INVISIBLE);
                        EdProduct.setVisibility(View.INVISIBLE);
                    }
                }
            });
            EdCategory.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    MainCategory = s.toString();
                }
            });

            EdProduct.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    SubCat1 = s.toString();
                }
            });
            MainCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String text = parent.getItemAtPosition(position).toString();
                    MainCategory = text;
                    loadSubCatOne(text);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            Cat1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    SubCat1 = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            Cat2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    SubCat2 = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            if (loadEdit) {
                try {
                    productID = pid;
                    EdProductName.setText(selectedProduct.ProductName);
                    EdProductPrice.setText(selectedProduct.ShowPrice);
                    EdProductDescription.setText(selectedProduct.Description);
                    EdDiscount.setText(selectedProduct.ShowoffPrice);
                    EdInventory.setText(String.valueOf(selectedProduct.Discontinued));

                    String showTik = String.valueOf(selectedProduct.Show);
                    if (showTik != null && showTik.equals("false"))
                        cbShowToUser.setChecked(false);

                    for (int i = 0; i < unit.length; i++) {
                        if (unit[i].equals(selectedProduct.Unit)) {
                            Unit.setSelection(i);
                            break;
                        }
                    }
                    /**
                     * Check the loop statement
                     */
                    /*for (ProductPropertisClass pp : selectedProduct.getProductClass().getProductPropertis()) {
                        mProperties.add(pp.getPropertisName());
                        mPropertyValues.add(pp.getPropertisValue());
                        productPropertisClasses.add(new ProductPropertisClass(pid, "مشخصات اصلی", pp.getPropertisName(),
                                pp.getPropertisValue(), null));
                    }*/
                    initRecyclerView();

                    cbStory.setChecked(ISParticular(String.valueOf(selectedProduct.ReorderLevel)));
                    cbBulletin.setChecked(ISBulletin(String.valueOf(selectedProduct.ReorderLevel)));

                    newDownloadImage(selectedProduct.ProductID, imageView);

                    btnAdd.setText("ثبت تغییرات");
                } catch (Exception e) {
                    logMessage("750 >> " + e.getMessage(), mContext);
                }
            }
        } catch (Exception e) {
            Log.d("addProduct OnCreate >> ", e.getMessage());
        }
    }//end OnCreate

    public void newDownloadImage(String pid, ImageView _imageView) {
        String url = baseCodeClass.BASE_URL + "Products/DownloadFile?ProductID=" + pid + "&fileNumber=1";
        Glide.with(mContext).load(url).into(_imageView);
    }

    private boolean ISParticular(String reOrder) {
        try {
            if (reOrder.equals("100") || reOrder.equals("200")) {
                return true;
            }
        } catch (Exception e) {
            logMessage("AddProduct 199 >> " + e.getMessage(), this);
            return false;
        }
        return false;
    }

    private boolean ISBulletin(String reOrder) {
        try {
            if (reOrder.equals("150") || reOrder.equals("200")) {
                return true;
            }
        } catch (Exception e) {
            logMessage("AddProduct 299 >> " + e.getMessage(), this);
        }

        return false;
    }

    public void cropRequest(Uri uri) {
        try {
            CropImage.activity(uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setMultiTouchEnabled(true)
                    .start(this);
        } catch (Exception e) {
            logMessage("AddProduct 399 >> " + e.getMessage(), this);
        }

    }

    public void DeletProduct(String Pid) {
        try {
            SendDeleteProduct deleteProduct = new SendDeleteProduct(baseCodeClass.getToken(), baseCodeClass.getUserID(),
                    baseCodeClass.getCompanyID(), Pid);
            Call<GetResualt> call = loadProductApi.deleteProduct(deleteProduct);
            call.enqueue(new Callback<GetResualt>() {
                @Override
                public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                    callBack.onResponseDeleteProduct(response.body());
                }

                @Override
                public void onFailure(Call<GetResualt> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            logMessage("AddProduct 499 >> " + e.getMessage(), this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

/*
        manageChooseImageInOnResult1(requestCode,resultCode,data);
*/


//        if (requestCode == PICK_IMAGE /*&& requestCode == RESULT_OK && data != null*/) {
////            Uri imageData = data.getData();
////            //imageProduct.setImageURI(imageData);
////            imagePath = getPathFromURI(mContext, imageData);
////            try {
//////                imageList.add(new SlideModel(String.valueOf(imageData), ScaleTypes.CENTER_INSIDE));
//////                imageList.add(new SlideModel(R.drawable.ic_add_photo, ScaleTypes.CENTER_INSIDE));
//////                imageSlider.setImageList(imageList, ScaleTypes.FIT);
//////                imageSlider.stopSliding();
//////                imageSlider.setItemClickListener(new ItemClickListener() {
//////                    @Override
//////                    public void onItemSelected(int i) {
//////                        try {
//////                            if (i+1 == imageList.size()) {
//////                                Intent intent = new Intent("com.android.camera.action.CROP");
//////                                intent.setType("image/*");
//////                                imageList.remove(i);
////////            intent.putExtra("crop", "true");
//////                                intent.setAction(Intent.ACTION_GET_CONTENT);
//////                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
//////                            }
//////                        }catch (Exception e){
//////                            Toast.makeText(AddProductActivity.this,e.getMessage().toString(), Toast.LENGTH_SHORT).show();
//////                        }
//////                        toastMessage("" + i);
////                    }
////                });
////            }catch (Exception e){
////                toastMessage(e.getMessage());
////            }
////            Toast.makeText(MyStroreFragment.this,imagePath, Toast.LENGTH_SHORT).show();
//        }
        try {
            if (requestCode == CAMERA_REQUEST_CODE) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                imageProduct.setImageBitmap(image);
            }

            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                Uri imageUri = CropImage.getPickImageResultUri(this, data);
                cropRequest(imageUri);
            }
        } catch (Exception e) {
            logMessage("AddProduct 603 >> " + e.getMessage(), this);
        }
//
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                try {
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());
//                    mAddImage.add(bitmap);
//                    imagePath = getPathFromURI(mContext, result.getUri());
//                    imageView.setImageBitmap(bitmap);
//
//
//                    initImageProductRecyclerView();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());
                    Glide.with(this).load(bitmap)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(imageView);
                    mainBitmap = bitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void manageChooseImageInOnResult1(int requestCode, int resultCode, Intent data){
        if (requestCode == CHOOSE_IMAGE_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            //chosenPhotoUri is the Uri of the image the user has picked

            try {
                Uri chosenPhotoUri = data.getData();
                File file = new File(chosenPhotoUri.getPath());
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), chosenPhotoUri);
                mainBitmap = bitmap;
/*
                imageView.setImageURI(chosenPhotoUri);
*/

                Glide.with(this).load(bitmap)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(imageView);

            } catch (IOException e) {
                e.printStackTrace();
            }



          /*  //display the chosen photo in the image view
            Glide.with(this).load(chosenPhotoUri).into(imageView);*/

        }
    }


    public void loadProperty() {
        try {
            Call<List<GetPropertisOfCompanyProducts>> call = loadProductApi.getProperty(2);
            call.enqueue(new Callback<List<GetPropertisOfCompanyProducts>>() {
                @Override
                public void onResponse(Call<List<GetPropertisOfCompanyProducts>> call,
                                       Response<List<GetPropertisOfCompanyProducts>> response) {
                    callBack.onResponseGetProperty(response.body());
                }

                @Override
                public void onFailure(Call<List<GetPropertisOfCompanyProducts>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            logMessage("AddProduct 699 >> " + e.getMessage(), this);
        }

    }

    public void btnAddClick(View view) {
        try {
            //check for fileds emptiness
            if (authorizeCurrentProduct()) {
                loadingDialog.startLoadingDialog();
                if (loadEdit) {
                    editProduct();
                } else {
                    addProduct();
                }
            }
        } catch (Exception e) {
            logMessage("AddProduct 799 >> " + e.getMessage(), this);
        }

    }

    public void btnDeleteClick(View view) {

        /*if(authorizeCurrentProduct()) {
            mainSendProductClass.setDeleted(String.valueOf(true));
            editProduct();
        }*/
        DeleteProduct deleteProduct = new DeleteProduct();
        deleteProduct.setCompanyID(BaseCodeClass.CompanyID);
        deleteProduct.setProductID(productID);
        deleteProduct.setToken(BaseCodeClass.token);
        deleteProduct.setUserID(BaseCodeClass.userID);
        Call<GetResualt> call = loadProductApi.deleteProduct(deleteProduct);
        call.enqueue(new Callback<GetResualt>() {
            @Override
            public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                String result = response.body().getResualt();
                String msg = response.body().getMsg();
                if (result.equals("100")) {
                    Toast.makeText(AddProductActivity.this, "محصول با موفقیت حذف شد", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddProductActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetResualt> call, Throwable t) {
                Log.d("Error", t.toString());
            }
        });
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
        boolean showToUser = cbShowToUser.isChecked();

        if (cbStory.isChecked() && cbBulletin.isChecked()) {
            reOrder = "200";
        } else if (cbStory.isChecked() && !cbBulletin.isChecked()) {
            reOrder = "100";
        } else if (!cbStory.isChecked() && cbBulletin.isChecked()) {
            reOrder = "150";
        } else if (!cbStory.isChecked() && !cbBulletin.isChecked()) {
            reOrder = "1";
        }

        String productName = EdProductName.getText().toString();
        if (checkEmptyString(productName)) {
            Toast.makeText(this, "نام محصول نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
            return false;
        }
        String description = EdProductDescription.getText().toString();
        if (checkEmptyString(description)) {
            Toast.makeText(this, "توضیحات نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
            return false;
        }

        StandardPrice standardCost = new StandardPrice();
        standardCost.setShowStandardCost(EdProductPrice.getText().toString());
        if (checkEmptyString(EdProductPrice.getText().toString())) {
            Toast.makeText(this, "قیمت نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
            return false;
        }

        String listPrice = EdDiscount.getText().toString();
//        if (checkEmptyString(listPrice)) {
//            Toast.makeText(this, "قیمت  نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        if (checkEmptyString(UnitValue)) {
            Toast.makeText(this, "واحد نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
            return false;
        }

        String stock = EdInventory.getText().toString();
        if (checkEmptyString(stock)) {
            Toast.makeText(this, "موجودی نمیتواند خالی باشد", Toast.LENGTH_SHORT).show();
            return false;
        }
        int Count = Integer.parseInt(stock);

        if (checkEmptyString(MainCategory) || checkEmptyString(SubCat1)) {
            Toast.makeText(this, "دسته بندی محصول نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (productPropertisClasses == null || productPropertisClasses.size() == 0) {
            Toast.makeText(this, "به مشخصات اصلی کالا حداقل یک مورد اضافه کنید.", Toast.LENGTH_SHORT).show();
            return false;
        }
        mainSendProductClass = new SendProduct(baseCodeClass.getToken(),baseCodeClass.getUserID(),
                baseCodeClass.getCompanyID(),baseCodeClass.getCompanyID(),productID,productName,
                description,standardCost.getStandardCost(),standardCost.getOffPrice(),Integer.parseInt(reOrder),
                10,UnitValue,"1",Count,1,
                MainCategory + "." + SubCat1 + "." + SubCat2,true,false,null,
                null,null,0,false,false,false,
                baseCodeClass.getUserID(),null,null,false,productPropertisClasses);
        /*mainSendProductClass = new SendProduct(
                baseCodeClass.getCompanyID(),baseCodeClass.getCompanyID(),productID,productName,description,standardCost,
                Integer.parseInt(reOrder),10,UnitValue,"1",Count,1,MainCategory + "." + SubCat1 + "." + SubCat2,showToUser,
                0,false,false,0,0,0,false,false,null,null,null,1,0,false,false,false,BaseCodeClass.userID,null,null,false,
                productPropertisClasses
        );*/


        return true;
    }

    private void editProduct() {
        try {

            if (mainSendProductClass != null) {
                Call<GetResualt> call = loadProductApi.editProductDetail(mainSendProductClass);
                Log.d(TAG, "editProduct: " + mainSendProductClass.getCompanyID());
                call.enqueue(new Callback<GetResualt>() {
                    @Override
                    public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                        GetResualt getResualt = new GetResualt(response.body().getResualt(), response.body().getMsg());
                        loadingDialog.dismissDialog();
                        Log.d(TAG, "onResponse: " + response.body());
                        callBack.onResponseSendProduct(getResualt);
                    }

                    @Override
                    public void onFailure(Call<GetResualt> call, Throwable t) {
                        loadingDialog.dismissDialog();
                        Toast.makeText(AddProductActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else
                Toast.makeText(this, "خطای مقدار دهی محصول", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("Catch add product >> ", e.getMessage());
        }
    }

    public void initImageProductRecyclerView() {
        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            layoutManager.setReverseLayout(true);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewAddImage);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(rvAdapter);
        } catch (Exception e) {
            logMessage("AddProduct 899 >> " + e.getMessage(), this);
        }
    }


    public void loadMainCategory() {
        try {
            Call<List<String>> call = loadProductApi.getCategory(Integer.parseInt(baseCodeClass.companyProfile.CompanyType));

            call.enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    if (response.body().size() == 0) {
                        swCategory.setChecked(true);

                    } else {
                        callBack.onResponseGetCategory(response.body());

                    }

                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            logMessage("AddProduct 999 >> " + e.getMessage(), this);
        }

    }

    public void loadSubCatOne(String mainCat) {
        try {
            Call<List<String>> call = loadProductApi.getSubCatOne(2, mainCat);
            call.enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    callBack.onResponseGetSubCatOne(response.body());
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            logMessage("AddProduct 100 >> " + e.getMessage(), this);
        }

    }

    List<ProductPropertisClass> productPropertisClasses = new ArrayList<ProductPropertisClass>();

    public void addProduct() {
        try {
            if (mainSendProductClass != null) {
                if (mainSendProductClass.getListPrice() <= 0)
                    mainSendProductClass.setListPrice(0);
                Call<GetResualt> call = loadProductApi.sendProductDetail(mainSendProductClass);
                Log.d(TAG, "editProduct: " + mainSendProductClass.getCompanyID());
                call.enqueue(new Callback<GetResualt>() {
                    @Override
                    public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                        Toast.makeText(AddProductActivity.this, "ذخیره با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                        GetResualt getResualt = new GetResualt(response.body().getResualt(), response.body().getMsg());
                        callBack.onResponseSendProduct(getResualt);
                        loadingDialog.dismissDialog();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<GetResualt> call, Throwable t) {
                        loadingDialog.dismissDialog();
                        Toast.makeText(AddProductActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else
                Toast.makeText(this, "خطای مقدار دهی محصول", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("Catch add product >> ", e.getMessage());
        }
    }

    private void sendImageProductSabeq(final String productID) {
        try {

            Cache cache = new Cache(this);

            File file = cache.saveToCacheAndGetFile(mainBitmap, productID);
/*
            Bitmap imageBitmap;
*/
            Bitmap imageBitmap = new Compressor(this)
                    .setMaxWidth(1080)
                    .setMaxHeight(1080)
                    .setQuality(50)
                    .setCompressFormat(Bitmap.CompressFormat.PNG)
                    .compressToBitmap(file);


     /*       if (!file.getName().endsWith("PNG")) {
                imageBitmap = new Compressor(this)
                        .setMaxWidth(1080)
                        .setMaxHeight(1080)
                        .setQuality(50)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .compressToBitmap(file);
            } else {
                imageBitmap = new Compressor(this)
                        .setMaxWidth(1080)
                        .setMaxHeight(1080)
                        .setQuality(50)
                        .compressToBitmap(file);
            }
*/

            Cache cacheCompressed = new Cache(this);
            File compressedFile = cacheCompressed.saveToCacheAndGetFile(imageBitmap, productID);

//            String root = getCacheDirectory(this).getPath();
//            File myDir = new File(root + "/Dehkade/Upload");
//            File f = new File(myDir, "1.jpg");
//            myDir.mkdirs();

//
//            FileOutputStream out = new FileOutputStream(f);
//            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
//
//            SaveProductImage(imageBitmap, "YasinCheck");
//
//            out.flush();
//            out.close();


            RequestBody requestBody = RequestBody.create(compressedFile, MediaType.parse("multipart/form-data"));
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", compressedFile.getName(), requestBody);

            Call<GetResualt> call = loadProductApi.uploadProductImage(productID, baseCodeClass.getCompanyID(), baseCodeClass.getUserID(), baseCodeClass.getToken(), body);
            call.enqueue(new Callback<GetResualt>() {
                @Override
                public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {

                    Toast.makeText(AddProductActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    loadingDialog.dismissDialog();
                    finish();
                }

                @Override
                public void onFailure(Call<GetResualt> call, Throwable t) {
                    Toast.makeText(AddProductActivity.this, "ثبت نا موفق بود" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    DeletProduct(productID);
                    loadingDialog.dismissDialog();
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "sendImageProduct: Error " + e.getMessage());
        }
    }


    private void sendImageProduct(final String productID) {
        try {

            Cache cache = new Cache(this);
            File file = cache.saveToCacheAndGetFile2(mainBitmap, productID);

            Bitmap imageBitmap = new Compressor(this)
                    .setMaxWidth(1080)
                    .setMaxHeight(1080)
                    .setQuality(50)
                    .compressToBitmap(file);


     /*       if (!file.getName().endsWith("PNG")) {
                imageBitmap = new Compressor(this)
                        .setMaxWidth(1080)
                        .setMaxHeight(1080)
                        .setQuality(50)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .compressToBitmap(file);
            } else {
                imageBitmap = new Compressor(this)
                        .setMaxWidth(1080)
                        .setMaxHeight(1080)
                        .setQuality(50)
                        .compressToBitmap(file);
            }
*/

            Cache cacheCompressed = new Cache(this);
            File compressedFile = cacheCompressed.saveToCacheAndGetFile2(imageBitmap, productID);

//            String root = getCacheDirectory(this).getPath();
//            File myDir = new File(root + "/Dehkade/Upload");
//            File f = new File(myDir, "1.jpg");
//            myDir.mkdirs();

//
//            FileOutputStream out = new FileOutputStream(f);
//            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
//
//            SaveProductImage(imageBitmap, "YasinCheck");
//
//            out.flush();
//            out.close();


            RequestBody requestBody = RequestBody.create(compressedFile, MediaType.parse("multipart/form-data"));
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", compressedFile.getName(), requestBody);

            Call<GetResualt> call = loadProductApi.uploadProductImage(productID, baseCodeClass.getCompanyID(), baseCodeClass.getUserID(), baseCodeClass.getToken(), body);
            call.enqueue(new Callback<GetResualt>() {
                @Override
                public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {

                    Toast.makeText(AddProductActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    loadingDialog.dismissDialog();
                    finish();
                }

                @Override
                public void onFailure(Call<GetResualt> call, Throwable t) {
                    Toast.makeText(AddProductActivity.this, "ثبت نا موفق بود" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    DeletProduct(productID);
                    loadingDialog.dismissDialog();
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "sendImageProduct: Error " + e.getMessage());
        }
    }


    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (requestCode == CAMERA_PERMISSION_CODE) {
                if (grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    toastMessage("CAMERA PERMISSION IS Required to Use Camera");
                }
            }
        } catch (Exception e) {
            logMessage("AddProduct 200 >> " + e.getMessage(), this);
        }

    }

    private void openCamera() {
        try {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        } catch (Exception e) {
            logMessage("AddProduct 300 >> " + e.getMessage(), this);
        }
    }

    public static String getPathFromURI(final Context context, final Uri uri) {
        try {

            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }

            return null;
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[]
            selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    private void initRecyclerView() {
        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            layoutManager.setReverseLayout(true);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.RecycleProductProperties);
            recyclerView.setLayoutManager(layoutManager);
            ProductPropertiesRecyclerViewAdapter adapter = new ProductPropertiesRecyclerViewAdapter(this, mProperties, mPropertyValues, 2);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            logMessage("AddProduct 400 >> " + e.getMessage(), this);
        }

    }


    public void toastMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    public void btnAddPropertyClick(View view) {

        try {
            if (EdProperty.getText().toString().equals("") || EdPropertyValue.getText().toString().equals(""))
                Toast.makeText(getApplicationContext(), "لطفا ویژگی های محصول را وارد کنید", Toast.LENGTH_SHORT).show();
            else {
                mProperties.add(EdProperty.getText().toString());
                mPropertyValues.add(EdPropertyValue.getText().toString());
                productPropertisClasses.add(new ProductPropertisClass(null, "مشخصات اصلی", EdProperty.getText().toString(), EdPropertyValue.getText().toString(), null));
                EdProperty.setText("");
                EdPropertyValue.setText("");
            }
        } catch (Exception e) {
            logMessage("AddProduct 500 >> " + e.getMessage(), this);
        }


        initRecyclerView();
    }


    public void btnCancel(View view) {
        finish();
    }

    public Bitmap replaceColor(Bitmap src) {
        if (src == null)
            return null;
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = new int[width * height];
        src.getPixels(pixels, 0, 1 * width, 0, 0, width, height);
        for (int x = 0; x < pixels.length; ++x) {
            //    pixels[x] = ~(pixels[x] << 8 & 0xFF000000) & Color.BLACK;
            if(pixels[x] == Color.BLACK) pixels[x] = 0;
        }
        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
    }


    //it should works for jpeg
//    public boolean SaveProductImage(Bitmap inputStream, String name) {
//        try {
////            InputStream inputStream = null;
//            OutputStream outputStream = null;
//
//            try {
//
//                Bitmap bm = null;
//                if (inputStream != null) {
////                    inputStream = body.byteStream();
//                    bm = inputStream;
//                } else {
//                    return false;
//                }
//
//
////
//                String fname = name + ".jpg";
//                File file = new File(myDir, fname);
//                try {
//                    FileOutputStream out = new FileOutputStream(file);
//                    bm.compress(Bitmap.CompressFormat.JPEG, 50, out);
//                    out.flush();
//                    out.close();
//                } catch (Exception e) {
//                }
//            } catch (Exception e) {
//                return false;
//            } finally {
//                if (outputStream != null) {
//                    outputStream.close();
//                }
//                return true;
//            }
//        } catch (Exception e) {
//            return false;
//        }
//    }
}
