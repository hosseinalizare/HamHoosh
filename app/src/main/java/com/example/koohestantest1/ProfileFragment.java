package com.example.koohestantest1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.koohestantest1.ApiDirectory.UserProfileApi;
import com.example.koohestantest1.Utils.StringUtils;
import com.example.koohestantest1.ViewModels.CompanyProfileFieldViewModel;
import com.example.koohestantest1.activity.EventActivity;
import com.example.koohestantest1.activity.LoadingActivity;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.example.koohestantest1.viewModel.LogOutVewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import com.example.koohestantest1.ApiDirectory.SignUpApi;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.GetSignUp;
import com.example.koohestantest1.classDirectory.ProfileRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.ProfileSettingRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.SendSignUp;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.selectedProfileField;

public class ProfileFragment extends Fragment implements ProfileRecyclerViewAdapter.OnListListener, NavigationView.OnNavigationItemSelectedListener {

    private Context mContext; //= ProfileFragment.this;
    private static final int ACTIVITY_NU = 4;

    BaseCodeClass baseCodeClass;

    Cursor data;
    SignUpApi signUpApi;

    String userName, password;

    TextView tvWelcomeSignUp;
    EditText edUserName;
    EditText edPassword;
    EditText edRPassword;
    private EditText edtName, edtFamily;

    //Button btnCreateUser;
    TextView tvRules;
    TextView tvUserFullName;
    TextView tvPhoneNumber, tvUsername;
    TermsDialog termsDialog;

    Typeface wYekan;
    Typeface wYekanBold;
    Typeface koodak;

    View view;
    View view_;
    ViewFlipper vf;
    ImageButton drawerMenuBtn;
    DrawerLayout drawerLayout;

    NavigationView navigationView;

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Integer> mImageUrls = new ArrayList<>();

    private ArrayList<String> mNamess = new ArrayList<>();
    private ArrayList<Integer> mImageUrlss = new ArrayList<>();
    private ImageView ivProfile;
    private LogOutVewModel logOutVewModel;
    private LinearLayout linearIdentifierCode;
    private EditText edtIdentifierCode;
    private ImageView imgSaveIdentifierCode;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logOutVewModel = new ViewModelProvider(this).get(LogOutVewModel.class);
        termsDialog = new TermsDialog(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        mContext = getActivity();
        navigationView = view.findViewById(R.id.myStoreNavView);
        tvWelcomeSignUp = view.findViewById(R.id.tvWelcomeSignUp);
        edUserName = view.findViewById(R.id.EdUserNameSignUp);
        edPassword = view.findViewById(R.id.EdPasswordSignUp);
        edRPassword = view.findViewById(R.id.EdRepeatPasswordSignUp);
        edtName = view.findViewById(R.id.edt_sign_up_name);
        edtFamily = view.findViewById(R.id.edt_sign_up_family);
        linearIdentifierCode = view.findViewById(R.id.rel_identifierCode);
        edtIdentifierCode = view.findViewById(R.id.edt_identifierCode);
        imgSaveIdentifierCode = view.findViewById(R.id.img_saveIdentifierCode);

        tvRules = view.findViewById(R.id.tvRulsSignUp);
        view_ = view.findViewById(R.id.btnSignUp);
        vf = view.findViewById(R.id.vf);
        tvUserFullName = view.findViewById(R.id.txtUserName);
        tvPhoneNumber = view.findViewById(R.id.txtPhoneNumber);
        drawerMenuBtn = view.findViewById(R.id.btnDrawerMenu);
        drawerLayout = view.findViewById(R.id.profileDrawerLayout);
        //dataBase = new DataBase(mContext);
        baseCodeClass = new BaseCodeClass();
        baseCodeClass.LoadBaseData(mContext);
        ivProfile = view.findViewById(R.id.imageViewProfile);


        drawerMenuBtn.setOnClickListener(v -> {
            YoYo.with(Techniques.RubberBand).playOn(drawerMenuBtn);
            drawerLayout.openDrawer(GravityCompat.END);
        });

        if (BaseCodeClass.userProfile.isHasProfile()) {
            vf.setDisplayedChild(1);
            fillUserProfile();
        } else {
            vf.setDisplayedChild(0);
        }

        imgSaveIdentifierCode.setOnClickListener(v -> {
            try {
                if (edtIdentifierCode.getText().toString().isEmpty()){
                    Toast.makeText(mContext, "کد معرف را وارد کنید!", Toast.LENGTH_SHORT).show();
                }else {
                    UserProfileApi userProfileApi = RetrofitInstance.getRetrofit().create(UserProfileApi.class);
                    Call<GetResualt> call =   userProfileApi.updateUserProfileField(new CompanyProfileFieldViewModel(baseCodeClass.getUserID(),
                            baseCodeClass.getToken(),
                            baseCodeClass.getUserID(),
                            edtIdentifierCode.getText().toString(),
                            BaseCodeClass.CompanyEnum.U_Referredby.name()));
                    call.enqueue(new Callback<GetResualt>() {
                        @Override
                        public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                            if (response.body().getResualt().equals("100")){
                                Toast.makeText(getContext(),"با موفقیت ثبت شد" , Toast.LENGTH_SHORT).show();
                                linearIdentifierCode.setVisibility(View.GONE);

                            }else {
                                Toast.makeText(getContext(), response.body().getMsg().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<GetResualt> call, Throwable t) {

                        }
                    });
                }


            } catch (Exception e) {
                logMessage("EditField 100 >> " + e.getMessage(), getContext());
            }
        });




        view_.setOnClickListener(v -> btnSignUpClick());

        wYekan = Typeface.createFromAsset(mContext.getAssets(), "fonts/koodak.TTF");
        wYekanBold = Typeface.createFromAsset(mContext.getAssets(), "fonts/koodak.TTF");
        koodak = Typeface.createFromAsset(mContext.getAssets(), "fonts/koodak.TTF");

        tvWelcomeSignUp.setTypeface(wYekanBold);
        edUserName.setTypeface(koodak);
        edPassword.setTypeface(koodak);
        edRPassword.setTypeface(koodak);
        //btnCreateUser.setTypeface(wYekanBold);
        tvRules.setTypeface(wYekan);

        //baseCodeClass = new BaseCodeClass();
        //data = dataBase.getAllData(DataBase.BASE_TABLE);

        final Retrofit retrofit = RetrofitInstance.getRetrofit();

        signUpApi = retrofit.create(SignUpApi.class);

        getImages();
        getImagesVertical();

        navigationView.setNavigationItemSelectedListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logOutVewModel.getLiveDataLogoutResult().observe(getViewLifecycleOwner(), s -> {
            if (s.equals("ok")) {
                Intent intent = new Intent(requireContext(), LoadingActivity.class);
                startActivity(intent);
                requireActivity().finish();
            } else
                Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
        });

        tvRules.setOnClickListener(v -> {
            termsDialog.startTermsDialog();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        String url = baseCodeClass.BASE_URL + "User/DownloadFile?UserID=" + baseCodeClass.getUserID() + "&fileNumber=" + 1;
        Glide.with(this).load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.ic_profile).into(ivProfile);
    }

    public void btnSignUpClick() {
        YoYo.with(Techniques.Shake).repeat(1).duration(700).playOn(view.findViewById(R.id.btnSignUp));
        view.findViewById(R.id.btnSignUp).setEnabled(false);
        try {
            if (!signUp()) {
                checkSignUp(new SignUpApi() {
                    @Override
                    public Call<GetSignUp> getSignUp(SendSignUp sendSignUp) {
                        return null;
                    }

                    @Override
                    public void onResponseSingUp(GetSignUp getSignUp) {
                        String token = getSignUp.getToken();
                        if (token.length() > 0) {
                            final ProgressButton progressButton = new ProgressButton(mContext, view_);
                            progressButton.buttonActivated("در حال ایجاد حساب کاربری");


                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("baseInfo",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token",token);
                            editor.putString("username",userName);
                            editor.putString("password",password);
                            editor.apply();
                            //dataBase.updateToken(token);
                            baseCodeClass.setToken(token);
                            //dataBase.updateUserPass(userName, password);
                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                progressButton.buttonFinished("حساب کاربری با موفقیت ایجاد شد");
                                Handler handler1 = new Handler();
                                handler1.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        vf.setDisplayedChild(1);
                                        fillUserProfile();
                                    }
                                }, 1000);
                            }, 3000);
                        } else {
                            toastMessage(getSignUp.getMsg());
                            view.findViewById(R.id.btnSignUp).setEnabled(true);
                        }
                    }

                });
            } else {
                //toastMessage("چیشد چیشد!!!!");
                view.findViewById(R.id.btnSignUp).setEnabled(true);
            }
        } catch (Exception e) {
            logMessage("ProfileFragment 100 >> " + e.getMessage(), mContext);
            view.findViewById(R.id.btnSignUp).setEnabled(true);
        }

    }// end public void btnSignUpClick

    public boolean signUp() {
        //TODO Get data from DB or SP and set them into baseCodeClass
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("baseInfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        String deviceModel = sharedPreferences.getString("deviceModel", null);
        String userId = sharedPreferences.getString("userId", null);
        String IMEI = sharedPreferences.getString("imei", null);
        String mobileNumber = sharedPreferences.getString("mobileNumber", null);
        baseCodeClass.setUserID(userId);
        baseCodeClass.setToken(token);
        BaseCodeClass.setMobileNumber(mobileNumber);
        BaseCodeClass.setDeviceModel(deviceModel);
        baseCodeClass.setIMEI(IMEI);
        /*try {
            if (data == null) {
                return false;
            }
            if (data.moveToFirst()) {
                do {
                    baseCodeClass.setUserID(data.getString(data.getColumnIndex(dataBase.getUserId())));
                    baseCodeClass.setToken(data.getString(data.getColumnIndex(dataBase.getTOKEN())));
                    baseCodeClass.setMobileNumber(data.getString(data.getColumnIndex(dataBase.getMobileNumber())));
                    baseCodeClass.setDeviceModel(data.getString(data.getColumnIndex(dataBase.getDeviceModel())));
                    baseCodeClass.setIMEI(data.getString(data.getColumnIndex(dataBase.getIMEI())));
                } while (data.moveToNext());
                return true;
            } else {
                return false;
            }
        }//try
        catch (Exception e) {
            return false;
        }*/
        return false;
    }// end public boolean signUp

    public void checkSignUp(final SignUpApi callBack) {
        try {
            edUserName = (EditText) view.findViewById(R.id.EdUserNameSignUp);
            edPassword = (EditText) view.findViewById(R.id.EdPasswordSignUp);
            edRPassword = (EditText) view.findViewById(R.id.EdRepeatPasswordSignUp);
            String name = edtName.getText().toString();
            String family = edtFamily.getText().toString();
            if (name.equals("")) {
                Toast.makeText(mContext, "نام نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
                return;
            }
            if (family.equals("")) {
                Toast.makeText(mContext, "فامیلی نمیتواند خالی باشد", Toast.LENGTH_SHORT).show();
                return;
            }
            if (edPassword.getText().toString().length() >= 8) {
                if (edPassword.getText().toString().equals(edRPassword.getText().toString())) {
                    userName = edUserName.getText().toString();
                    password = edPassword.getText().toString();
                    SendSignUp sendSignUp = new SendSignUp(edUserName.getText().toString(), edPassword.getText().toString(),
                            baseCodeClass.getUserID(), baseCodeClass.getToken(), baseCodeClass.getMobileNumber(),
                            BaseCodeClass.getIMEI(), baseCodeClass.getDeviceModel(), name, family);
                    Call<GetSignUp> call = signUpApi.getSignUp(sendSignUp);
                    call.enqueue(new Callback<GetSignUp>() {
                        @Override
                        public void onResponse(Call<GetSignUp> call, Response<GetSignUp> response) {
                            GetSignUp getSignUp = new GetSignUp(response.body().getUserID(), response.body().getToken(), response.body().getMsg());
                            callBack.onResponseSingUp(getSignUp);
                        }

                        @Override
                        public void onFailure(Call<GetSignUp> call, Throwable t) {
                            Log.d("Error",t.getMessage());
                        }
                    });
                } else {
                    toastMessage("لطفا پسورد را بررسی کنید");
                }
            } else {
                toastMessage("پسورد باید بیشتر از 8 کاراکتر باشد");
            }
        }//try
        catch (Exception e) {
            Log.d("Error",e.getMessage());
        }
    }// end public void checkSignUp

    /**
     * fill User Profile
     */
    public void fillUserProfile() {
        try {
            String fName = BaseCodeClass.userProfile.getFirstName() != null ? BaseCodeClass.userProfile.getFirstName() : "";
            String lName = BaseCodeClass.userProfile.getLastName() != null ? BaseCodeClass.userProfile.getLastName() : "";
            tvUserFullName.setText(fName + " " + lName);
            tvPhoneNumber.setText(BaseCodeClass.userProfile.getMobilePhone());
//            tvUsername.setText(BaseCodeClass.);
        } catch (Exception e) {
            logMessage("ProfileFragment 200 >> " + e.getMessage(), mContext);
        }
    }// end fillUserProfile()

    public void btnBtnClick(View view) {
        vf.setDisplayedChild(0);
    }

    public void toastMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    private void getImages() {
        mImageUrls.add(R.drawable.time);
        mNames.add("در حال بررسی");

        mImageUrls.add(R.drawable.box);
        mNames.add("در حال آماده سازی");

        mImageUrls.add(R.drawable.delivery);
        mNames.add("در حال ارسال");

        mImageUrls.add(R.drawable.pproduct);
        mNames.add("آماده تحویل");

        mImageUrls.add(R.drawable.ic_doller);
        mNames.add("تحویل شده");

        mImageUrls.add(R.drawable.ic_del);
        mNames.add("لغو شده");

        initRecyclerView();
    }

    private void initRecyclerView() {
        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            layoutManager.setReverseLayout(true);
            RecyclerView recyclerView = view.findViewById(R.id.profile_RecyclerView);
            recyclerView.setLayoutManager(layoutManager);
            ProfileRecyclerViewAdapter adapter = new ProfileRecyclerViewAdapter(mContext, mNames, mImageUrls, R.layout.layout_listitem, this);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            logMessage("ProfileFragment 300 >> " + e.getMessage(), mContext);
        }
    }

    private void getImagesVertical() {
        mImageUrlss.add(R.drawable.ic_profile);
        mNamess.add("اطلاعات حساب کاربری");

        mImageUrlss.add(R.drawable.ic_call);
        mNamess.add("اطلاعات تماس");

        mImageUrlss.add(R.drawable.ic_doller);
        mNamess.add("اطلاعات مالی");

        mImageUrlss.add(R.drawable.ic_setting);
        mNamess.add("تنظیمات");

        mImageUrlss.add(R.drawable.ic_address);
        mNamess.add("آدرس ها");

        initRecyclerViewVertical();
    }

    private void initRecyclerViewVertical() {
        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.profile_Setting_RecyclerView);
            recyclerView.setLayoutManager(layoutManager);
            ProfileSettingRecyclerViewAdapter adapter = new ProfileSettingRecyclerViewAdapter(mContext, mNamess, mImageUrlss, R.layout.layout_verticallistitem);
            recyclerView.setAdapter(adapter);

            if (StringUtils.textIsEmpty(BaseCodeClass.userProfile.getReferredby())){
                linearIdentifierCode.setVisibility(View.VISIBLE);

            }else {
                linearIdentifierCode.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            logMessage("ProfileFragment 400 >> " + e.getMessage(), mContext);
        }
    }

    @Override
    public void OnListClick(int position) {
        try {
            Intent intent = new Intent(mContext, MyStoreReportActivity.class);
            intent.putExtra("status", String.valueOf(position + 1));
            intent.putExtra("mode", "user");
            startActivity(intent);
        } catch (Exception e) {
            logMessage("ProfileFragment 500 >> " + e.getMessage(), mContext);
        }
    }


    @SuppressLint("WrongConstant")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(requireContext(), EventActivity.class);
        switch (item.getItemId()) {
            case R.id.menu_fav:
                intent.putExtra(EventActivity.KEY_EVENT_MODE, 0);
                startActivity(intent);
                break;
            case R.id.menu_book:
                intent.putExtra(EventActivity.KEY_EVENT_MODE, 1);
                startActivity(intent);
                break;
            case R.id.setting:
                startActivity(new Intent(requireContext(), ProfileSettingActivity.class));
                break;

            case R.id.menu_rules:
                TermsDialog termsDialog = new TermsDialog(getActivity());
                termsDialog.startTermsDialog();
                break;
            case R.id.logout:
                showLogoutDialog();
                break;
        }
        drawerLayout.closeDrawer(Gravity.END);
        return true;
    }

    private void logout() {
        logOutVewModel.clearDatabase();
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle("خروج از حساب کاربری")
                .setMessage("با تایید خروج، شما از این حساب کاربری خارج میشید")
                .setNeutralButton("لغو", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("تایید", (dialog, which) -> logout());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
