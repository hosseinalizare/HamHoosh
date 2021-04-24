package com.example.koohestantest1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.example.koohestantest1.Utils.Cache;
import com.example.koohestantest1.databinding.ActivityEditProfileBinding;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.example.koohestantest1.model.network.UserProfileAPI;
import com.example.koohestantest1.viewModel.ProfileSharedViewModel;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import com.example.koohestantest1.ApiDirectory.UserProfileApi;
import com.example.koohestantest1.DB.DataBase;
import com.example.koohestantest1.InfoDirectory.GetOnlineInformationClass;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.EditProfileField;
import com.example.koohestantest1.classDirectory.EditProfileRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.UserProfile;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.CompanyEnum;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.userProfile;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.variableType.string_;
import static com.nostra13.universalimageloader.utils.StorageUtils.getCacheDirectory;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private BaseCodeClass baseCodeClass;
    private DataBase dataBase;
    private UserProfileApi userProfileApi;
    private CompanyEnum companyField;
    private UserProfile userView;
    private GetOnlineInformationClass getOnlineInformationClass;

    private List<EditProfileField> fields = new ArrayList<>();
    private String TAG = EditProfileActivity.class.getSimpleName();

    private Timer timer = new Timer();

    private Bitmap profilePictureBitmap;

    public void closeBtn(View view) {
        finish();
    }

    public void saveProfile(View view) {
        if (profilePictureBitmap == null) {
            Toast.makeText(this, "عکسی برای آپلود شدن انتخاب نشده", Toast.LENGTH_SHORT).show();
        } else {
            toastMessage("در حال آپلود عکس...");
            sendImageProduct(profilePictureBitmap);
        }
    }

    private ProfileSharedViewModel profileSharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        profileSharedViewModel = new ViewModelProvider(this).get(ProfileSharedViewModel.class);
        dataBase = new DataBase(this);
        baseCodeClass = new BaseCodeClass();
        baseCodeClass.LoadBaseData(this);
        userView = baseCodeClass.userProfile;
        getOnlineInformationClass = new GetOnlineInformationClass(this);

        String url = baseCodeClass.pBASE_URL + "User/DownloadFile?UserID=" + baseCodeClass.getUserID() + "&fileNumber=" + 1;
        Glide.with(this).load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.ic_profile).into(binding.imageViewEditProfile);

        companyField = CompanyEnum.Umenu_Setting;
        if (getIntent().hasExtra("mode")) {
            String mod = getIntent().getStringExtra("mode");
            if (mod.equals(CompanyEnum.Umenu_Setting.toString()))
                companyField = CompanyEnum.Umenu_Setting;
            else if (mod.equals(CompanyEnum.Umenu_CallInfo.toString()))
                companyField = CompanyEnum.Umenu_CallInfo;
            else if (mod.equals(CompanyEnum.Umenu_Money.toString()))
                companyField = CompanyEnum.Umenu_Money;
            else if (mod.equals(CompanyEnum.Umenu_Address.toString()))
                companyField = CompanyEnum.Umenu_Address;
        }
        fillProfile();

        binding.txtChangeProfilePhoto.setOnClickListener(v -> {

            CropImage.startPickImageActivity(EditProfileActivity.this);
        });

        profileSharedViewModel.getRefreshCondition().observe(this, aBoolean -> {
            if (aBoolean) {
                baseCodeClass.LoadBaseData(this);
                userView = userProfile;
                fillProfile();

                getOnlineInformationClass.loadMyProfileInformation();
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    userView = userProfile;

                    fillProfile();
                }, 500);
            }
        });
    }

    public void fillProfile() {
        try {

            fields.clear();
            EditProfileField profileField;


            for (BaseCodeClass.CompanyEnum c : BaseCodeClass.CompanyEnum.values()
            ) {
                profileField = GetProfileField(c);
                if (profileField != null)
                    Log.d(TAG, c.toString() + " <<" + profileField.Value + "\n");
                else
                    Log.d(TAG, c.toString() + " << is null object\n");

                if (!fields.contains(profileField) && profileField != null) {
                    try {
                        BaseCodeClass.compare(profileField.getExplain(), null);
                        if (BaseCodeClass.compare(profileField.getExplain(), null) || BaseCodeClass.compare(profileField.getExplain(), "")) {//profileField.getExplain().compareTo("") == 0 || profileField.getExplain() == null || profileField.getExplain().compareTo("null") == 0 || profileField.getExplain().isEmpty()){
                            profileField.setExplain(" ");
                        }
                        if (BaseCodeClass.compare(profileField.getValue(), null) || BaseCodeClass.compare(profileField.getValue(), "")) {//profileField.getValue().compareTo("") == 0 || profileField.getValue() == null || profileField.getValue().compareTo("null") == 0 || profileField.getValue().isEmpty()){
                            profileField.setValue(" ");
                        }
                    } catch (Exception e) {
                        logMessage("empty >> " + e.getMessage(), this);
                    }
                    //  profileField.setExplain(" " + profileField.getExplain());
                    //profileField.setValue(" " + profileField.getValue());

                    Log.d(TAG, "fillProfile: " + profileField.toString());
                    fields.add(profileField);
                }
            }
        } catch (Exception ex) {
            logMessage("fillProfile >> " + ex.getMessage(), this);
        }
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.editProfile_RecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        EditProfileRecyclerViewAdapter adapter = new EditProfileRecyclerViewAdapter(fields, this, getSupportFragmentManager());
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "\n\n\n *******onResume:********** ");


        try {

            getOnlineInformationClass.loadMyProfileInformation();
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                userView = userProfile;
                fillProfile();
            }, 500);

        } catch (Exception ex) {
            baseCodeClass.logMessage("onCreate >> " + ex.getMessage(), this);
        }
    }

    public EditProfileField GetProfileField(CompanyEnum Cenum_) {
        try {
            switch (Cenum_) {
                case AddresMenu:
                    break;

                case SettingMenu:
                    break;

                case CallInfo:
                    break;

                case money:
                    break;


                case U_MobilePhone:
                    if (companyField == CompanyEnum.Umenu_CallInfo)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                false,
                                "تلفن همراه",
                                userView.getMobilePhone(),
                                "",
                                BaseCodeClass.variableType.mobile,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_PID:
                    if (companyField == CompanyEnum.Umenu_Setting)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                false,
                                "نام کاربری",
                                userView.getUserName(),
                                "",
                                string_,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_FirstName:
                    if (companyField == CompanyEnum.Umenu_Setting)
                        return new EditProfileField(
                                true,
                                Cenum_,
                                true,
                                "نام ",
                                userView.getFirstName(),
                                "",
                                string_,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_LastName:
                    if (companyField == CompanyEnum.Umenu_Setting)
                        return new EditProfileField(
                                true,
                                Cenum_,
                                true,
                                "نام خانوادگی",
                                userView.getLastName(),
                                "",
                                string_,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_EmailAddress:
                    if (companyField == CompanyEnum.Umenu_CallInfo)
                        return new EditProfileField(
                                true,
                                Cenum_,
                                true,
                                "ايميل",
                                userView.getEmailAddress(),
                                "",
                                BaseCodeClass.variableType.email,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_HomePhone:
                    if (companyField == CompanyEnum.Umenu_CallInfo)
                        return new EditProfileField(
                                true,
                                Cenum_,
                                true,
                                " تلفن منزل",
                                userView.getHomePhone(),
                                "",
                                BaseCodeClass.variableType.phoneNumber,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_BusinessPhone:
                    if (companyField == CompanyEnum.Umenu_CallInfo)
                        return new EditProfileField(
                                true,
                                Cenum_,
                                true,
                                "تلفن محل کار",
                                userView.getBusinessPhone(),
                                "",
                                BaseCodeClass.variableType.phoneNumber,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_NationalCode:
                    if (companyField == CompanyEnum.Umenu_Setting)
                        return new EditProfileField(
                                true,
                                Cenum_,
                                true,
                                "کد ملی",
                                userView.getNationalCode(),
                                "",
                                BaseCodeClass.variableType.number,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_CompanyID:
                    if (companyField == CompanyEnum.NONE)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                false,
                                "آی دی فروشگاه",
                                userView.getCompanyID(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_JobTitle:
                    if (companyField == CompanyEnum.Umenu_Setting)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "شغل",
                                userView.getJobTitle(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_FaxNumber:
                    if (companyField == CompanyEnum.Umenu_CallInfo)
                        return new EditProfileField(
                                true,
                                Cenum_,
                                true,
                                "شماره فکس",
                                userView.getFaxNumber(),
                                "",
                                BaseCodeClass.variableType.phoneNumber,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_Country:
                    if (companyField == CompanyEnum.Umenu_Address)
                        return new EditProfileField(
                                true,
                                Cenum_,
                                true,
                                "کشور",
                                userView.getCountry(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_City:
                    if (companyField == CompanyEnum.Umenu_Address)
                        return new EditProfileField(
                                true,
                                Cenum_,
                                true,
                                "شهر",
                                userView.getCity(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_State:
                    if (companyField == CompanyEnum.Umenu_Address)
                        return new EditProfileField(
                                true,
                                Cenum_,
                                true,
                                "خيابان",
                                userView.getState(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_PostalCode:
                    if (companyField == CompanyEnum.Umenu_Address)
                        return new EditProfileField(
                                true,
                                Cenum_,
                                true,
                                "کد پستی",
                                userView.getPostalCode(),
                                "",
                                BaseCodeClass.variableType.number,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_Area:
                    if (companyField == CompanyEnum.Umenu_Address)
                        return new EditProfileField(
                                true,
                                Cenum_,
                                true,
                                "آدرس",
                                userView.getArea(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_Location:
                    if (companyField == CompanyEnum.Umenu_Address)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                false,
                                "موقعيت",
                                userView.getLocation(),
                                "",
                                BaseCodeClass.variableType.location,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_Address:
                    if (companyField == CompanyEnum.Umenu_Address)
                        return new EditProfileField(
                                true,
                                Cenum_,
                                true,
                                "آدرس",
                                userView.getAddress(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_Bio:
                    if (companyField == CompanyEnum.Umenu_Setting)
                        return new EditProfileField(
                                true,
                                Cenum_,
                                true,
                                "توضيحات",
                                userView.getBio(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_WebPage:
                    if (companyField == CompanyEnum.Umenu_CallInfo)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "تارنما",
                                userView.getWebPage(),
                                "",
                                BaseCodeClass.variableType.webPage,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_Referredby:
                    if (companyField == CompanyEnum.Umenu_Setting)
                        return new EditProfileField(
                                true,
                                Cenum_,
                                false,
                                "کد معرف",
                                userView.getReferredby(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_Hometown:
                    if (companyField == CompanyEnum.Umenu_Address)
                        return new EditProfileField(
                                true,
                                Cenum_,
                                true,
                                "شهر",
                                userView.getHometown(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_Birthdate:
                    if (companyField == CompanyEnum.Umenu_Setting)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "تاريخ تولد",
                                String.valueOf(userView.getBirthdate()),
                                "",
                                BaseCodeClass.variableType.date,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_MaritalStatus:
                    if (companyField == CompanyEnum.Umenu_Setting)
                        return new EditProfileField(
                                true,
                                Cenum_,
                                true,
                                "وضعيت تاهل",
                                userView.getMaritalStatus(),
                                "",
                                BaseCodeClass.variableType.marital,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_ImageAttachments:
                    if (companyField == CompanyEnum.NONE)
                        return new EditProfileField(
                                true,
                                Cenum_,
                                true,
                                "عکس",
                                userView.getImageAttachments(),
                                "",
                                string_,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_IsOnline:
                    if (companyField == CompanyEnum.NONE)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                false,
                                "وضعيت برخط",
                                userView.getIsOnline(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_LastSeen:
                    if (companyField == CompanyEnum.NONE)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                "تاريخ آنلاين",
                                String.valueOf(userView.getLastSeen()),
                                "",
                                BaseCodeClass.variableType.datetime,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_Privasy:
                    if (companyField == CompanyEnum.Umenu_Setting)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                false,
                                "وضعيت اکانت",
                                userView.getPrivasy(),
                                "",
                                BaseCodeClass.variableType.bool_,
                                BaseCodeClass.editMode.userProfile);
                    break;
                case U_ErrorMsg:
                    if (companyField == CompanyEnum.NONE)
                        return new EditProfileField(
                                false,
                                Cenum_,
                                true,
                                " . ",
                                userView.getErrorMsg(),
                                "",
                                BaseCodeClass.variableType.string_,
                                BaseCodeClass.editMode.userProfile);
                    break;
            }
        } catch (Exception ex) {

        }
        return null;
    }

    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode ==RESULT_OK){
            binding.btnSaveProfile.setVisibility(View.VISIBLE);
            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

                Uri imageUri = CropImage.getPickImageResultUri(this, data);
                cropRequest(imageUri);



            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());
                        Glide.with(this).load(bitmap)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(binding.imageViewEditProfile);
                        profilePictureBitmap = bitmap;

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }



        }else {
            binding.btnSaveProfile.setVisibility(View.INVISIBLE);
        }









    }


    private void sendImageProduct(Bitmap bitmap) {
        try {

            Cache cache = new Cache(this);
            File file = cache.saveToCacheAndGetFile(bitmap, "photo");
//
            Bitmap imageBitmap = new Compressor(this)
                    .setMaxWidth(1080)
                    .setMaxHeight(1080)
                    .setQuality(50)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .compressToBitmap(file);

            Cache cacheCompressed = new Cache(this);
            File compressedFile = cacheCompressed.saveToCacheAndGetFile(imageBitmap, "photoCompressed");

            RequestBody requestBody = RequestBody.create(compressedFile, MediaType.parse("multipart/form-data"));
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", compressedFile.getName(), requestBody);

            Retrofit retrofit = RetrofitInstance.getRetrofit();
            UserProfileAPI api = retrofit.create(UserProfileAPI.class);
            api.uploadUserProfile(baseCodeClass.getUserID(), baseCodeClass.getToken(), body).enqueue(new Callback<GetResualt>() {
                @Override
                public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                    String code = response.body().getResualt();
                    Log.d(TAG, "onResponse: " + response.body().toString());
                    if (code.equals("100"))
                        Toast.makeText(EditProfileActivity.this, "عکس پروفایل با موفقیت آپلود شد", Toast.LENGTH_SHORT).show();
                    binding.btnSaveProfile.setVisibility(View.INVISIBLE);

                    //reset profile bitmap
                    profilePictureBitmap = null;
                }

                @Override
                public void onFailure(Call<GetResualt> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            Log.d(TAG, "sendImageProduct: Error " + e.getMessage());
        }
    }

    public void cropRequest(Uri uri) {
        CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

}
