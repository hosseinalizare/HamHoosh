package com.example.koohestantest1.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.koohestantest1.MainActivity;
import com.example.koohestantest1.R;
import com.example.koohestantest1.Utils.SharedPreferenceUtils;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.example.koohestantest1.viewModel.LogOutVewModel;

import com.example.koohestantest1.ApiDirectory.CheckAccessApi;

import com.example.koohestantest1.InfoDirectory.GetOnlineInformationClass;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.DownloadPrudoctThread;
import com.example.koohestantest1.classDirectory.GetCheckAccess;
import com.example.koohestantest1.classDirectory.SendCheckAccess;
import com.example.koohestantest1.classDirectory.SendOrderClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;

public class LoadingActivity extends AppCompatActivity {

    BaseCodeClass baseCodeClass;

    private Handler mHandler = new Handler();
    CheckAccessApi checkAccessApi;
    Cursor data;

    ImageView animLogo;
    AnimatedVectorDrawableCompat avd;
    AnimatedVectorDrawable avd2;

    ProgressBar progressBar;
    TextView tvInternetError, tvRefresh;
    ImageView imgRefresh;

    GetOnlineInformationClass getOnlineInformationClass;

    private TextView tvCompanyGreeting;
    private String TAG = LoadingActivity.class.getSimpleName() + "Debug";
    private LogOutVewModel logOutVewModel;
    private SharedPreferenceUtils sharedPreferenceUtils;

    /**
     * onCreate
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        sharedPreferenceUtils = new SharedPreferenceUtils(this);
        progressBar = findViewById(R.id.progressBar);
        tvInternetError = findViewById(R.id.txtInternetError);
        tvRefresh = findViewById(R.id.txtRefresh);
        imgRefresh = findViewById(R.id.refreshIcon);
        tvCompanyGreeting = findViewById(R.id.tvLogo);
        String greeting = null;
        if (sharedPreferenceUtils.getCompanyName() == null) {
            greeting = "خوش آمدید";
        } else {
            greeting = "به " +
                    sharedPreferenceUtils.getCompanyName() +
                    " خوش آمدید";
        }
        tvCompanyGreeting.setText(greeting);

        try {
            getOnlineInformationClass = new GetOnlineInformationClass(LoadingActivity.this);
//            dataBase = new DataBase(this);
//            myDB = new MyDataBase(this);
            baseCodeClass = new BaseCodeClass();
            baseCodeClass.LoadBaseData(this);
            //data = dataBase.getAllData(DataBase.BASE_TABLE);
            logOutVewModel = new ViewModelProvider(this).get(LogOutVewModel.class);
            final Retrofit retrofit = RetrofitInstance.getRetrofit();
            checkAccessApi = retrofit.create(CheckAccessApi.class);

            mHandler.postDelayed(() -> startCheckAccess(), 700);


            tvRefresh.setOnClickListener(v -> {
                refresh();

            });

            imgRefresh.setOnClickListener(view -> {
                refresh();
            });

            animLogo = findViewById(R.id.iv_logo_anim);



//            loadCompanyImage();
            /**
             * animation logo code
             */

            this.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );

            Drawable drawable = animLogo.getDrawable();
            if (drawable instanceof AnimatedVectorDrawableCompat) {
                avd = (AnimatedVectorDrawableCompat) drawable;
                avd.start();
            } else if (drawable instanceof AnimatedVectorDrawable) {
                avd2 = (AnimatedVectorDrawable) drawable;
                avd2.start();
            }

            // end animation logo code


        } catch (Exception e) {
            toastMessage("200 >> " + e.getMessage());
        }

        //SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("baseInfo",MODE_PRIVATE);
        //sharedPreferences.edit().clear().commit();
        /*if(sharedPreferences.getString("token",null) == null && sharedPreferences.getString("username",null) == null){
            startActivity(new Intent(LoadingActivity.this, MainActivity.class));
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "امکان خروج وجود ندارد", Toast.LENGTH_SHORT).show();
        }*/

        /*logOutVewModel.getLiveDataLogoutResult().observe(this, s -> {
            if (s.equals("ok")) {

            } else
                Toast.makeText(this, "خطایی رخ داد", Toast.LENGTH_SHORT).show();
        });*/


    }/// end onCreate

    private void refresh() {
        YoYo.with(Techniques.RotateIn)
                .duration(1000)
                .playOn(imgRefresh);

        mHandler.postDelayed(() -> {
            tvInternetError.setVisibility(View.INVISIBLE);
            tvRefresh.setVisibility(View.INVISIBLE);
            imgRefresh.setVisibility(View.INVISIBLE);

            startCheckAccess();

            progressBar.setVisibility(View.VISIBLE);
        }, 500);
    }

    private void startCheckAccess() {
        try {
            if (checkLogin()) {
                checkAccess(new CheckAccessApi() {
                    @Override
                    public Call<GetCheckAccess> getCheckAccess(SendCheckAccess sendCheckAccess) {
                        return null;
                    }

                    @Override
                    public void onResponseCheckAccess(GetCheckAccess getCheckAccess) {

                        String result = getCheckAccess.getResualt();
                        if (result.equals("111")) {

                            Log.d(TAG, "onResponseCheckAccess: TOKEN: \n" + getCheckAccess.getMsg());

                            //dataBase.updateToken(getCheckAccess.getMsg());
                            //TODO Save Token into DB or SP
                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("baseInfo",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token",getCheckAccess.getMsg());
                            editor.apply();
                            baseCodeClass.setToken(getCheckAccess.getMsg());
                            try {
                                Log.d(TAG, "onResponseCheckAccess: " + baseCodeClass.getCompanyID());
                                getOnlineInformationClass.readCompany(baseCodeClass.getCompanyID());
                                getOnlineInformationClass.companyEmployee(baseCodeClass.getCompanyID());
                                getOnlineInformationClass.loadCompanyFollower(baseCodeClass.getCompanyID());
                                getOnlineInformationClass.loadMyProfileInformation();
                                /*
                                 Not Using yet
                                 */
                                DownloadPrudoctThread p = new DownloadPrudoctThread(null, LoadingActivity.this);
                                p.start();
                                //TODO if ADD to Card is exists in DB create new order and show it
                                /*SendOrderClass orderClass = myDB.GetDefualtOrder(LoadingActivity.this);
                                if (orderClass != null) {
                                    //baseCodeClass.manageOrderClass = new ManageOrderClass(orderClass, LoadingActivity.this);
                                    //toastMessage("dsfdfsdfs");
                                }*/
                                // end of disused codes

                            } catch (Exception e) {
                                toastMessage(e.getMessage());
                            }
                            //toastMessage("token update");
                            Toast.makeText(getApplicationContext(), "خوش آمدید", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoadingActivity.this, Main2Activity.class);


                            mHandler.postDelayed(() -> {
                                // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                                finish();
                            }, 2000);
                        } else if (result.equals("113")) {
                            toastMessage("اطلاعات شما از دیتابیس حذف شده است");
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                                    finish();
                                }
                            }, 500);
                        } else {
                            if (result.equals("118")) {
                                //user token has been changed we should update its token
                                Toast.makeText(LoadingActivity.this, "118", Toast.LENGTH_SHORT).show();


                                //we should clear database, tokens are not equal, its results come by an observer
                                //listen to its res logOutVewModel.getLiveDataLogoutResult()
                                logOutVewModel.clearDatabase();

                                Log.d(TAG, "onResponseCheckAccess: " + getCheckAccess.toString());

                            } else {
                                toastMessage(result + getCheckAccess.getMsg());
                            }
                        }
                    }
                });

            } else {
                mHandler.postDelayed(() -> {
                    ///toastMessage("aaaaaa");
                    startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                    finish();
                }, 3000);
            }
        } catch (Exception e) {
            logMessage("Loading 100 >> " + e.getMessage(), this);
        }
    }

    /**
     * checkLogin
     */
    public boolean checkLogin() {
        try {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("baseInfo", MODE_PRIVATE);
            String token = sharedPreferences.getString("token", null);
            String deviceModel = sharedPreferences.getString("deviceModel", null);
            String userId = sharedPreferences.getString("userId", null);
            String IMEI = sharedPreferences.getString("imei", null);
            String username = sharedPreferences.getString("username", null);
            String password = sharedPreferences.getString("password", null);
            //Cursor data = dataBase.getAllData(dataBase.BASE_TABLE);
            if (token == null) {
                //toastMessage("1");
                return false;
            }else{
                baseCodeClass.setUserID(userId);
                baseCodeClass.setToken(token);
                BaseCodeClass.setDeviceModel(deviceModel);
                baseCodeClass.setIMEI(IMEI);
                baseCodeClass.setUserName(username);
                baseCodeClass.setPassword(password);
            }


            /*if (data.moveToFirst()) {
                while (data.moveToNext()) {

                }*/

                return true;
//            } else {
                //toastMessage("2");
                //return false;
            //}
        }//try
        catch (Exception e) {
            String m = e.getMessage();
            Toast.makeText(getApplicationContext(), m, Toast.LENGTH_LONG).show();
            return false;
        }
    }// end public boolean checkLogin()

    /**
     * checkAccess
     */
    public void checkAccess(final CheckAccessApi callBack) {
        try {
            //TODO Read data from DB or SP and fill SendCheckAccess
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("baseInfo", MODE_PRIVATE);
            String token = sharedPreferences.getString("token", null);
            String deviceModel = sharedPreferences.getString("deviceModel", null);
            String userId = sharedPreferences.getString("userId", null);
            String IMEI = sharedPreferences.getString("imei", null);

            SendCheckAccess sendCheckAccess = new SendCheckAccess(userId,
                    token,
                    IMEI,
                    deviceModel);

            /*data = dataBase.getAllData(DataBase.BASE_TABLE);
            data.moveToFirst();
            SendCheckAccess sendCheckAccess = new SendCheckAccess(
            data.getString(data.getColumnIndex(DataBase.getUserId())),
                    data.getString(data.getColumnIndex(DataBase.getTOKEN())),
                    data.getString(data.getColumnIndex(DataBase.getIMEI())),
                    data.getString(data.getColumnIndex(DataBase.getDeviceModel())));*/
            //
            Call<GetCheckAccess> call = checkAccessApi.getCheckAccess(sendCheckAccess);

            Log.d(TAG, "checkAccess: " + sendCheckAccess.toString());
            call.enqueue(new Callback<GetCheckAccess>() {
                @Override
                public void onResponse(Call<GetCheckAccess> call, Response<GetCheckAccess> response) {
                    GetCheckAccess getCheckAccess = new GetCheckAccess(response.body().getResualt(), response.body().getMsg());
                    callBack.onResponseCheckAccess(getCheckAccess);
                }

                @Override
                public void onFailure(Call<GetCheckAccess> call, Throwable t) {
                    internetError();
                }
            });
            //toastMessage(sendCheckAccess.getToken());
        } catch (Exception e) {
            toastMessage(e.getMessage());
        }
    }//end public void checkAccess(final CheckAccessApi callBack)

    public void internetError() {
        try {
            progressBar.setVisibility(View.INVISIBLE);
            tvInternetError.setVisibility(View.VISIBLE);
            tvRefresh.setVisibility(View.VISIBLE);
            imgRefresh.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            logMessage("Loading 200 >> " + e.getMessage(), this);
        }
    }

    /**
     * toast
     */
    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void loadCompanyImage() {
        String url = baseCodeClass.BASE_URL + "Company/DownloadFile?CompanyID=" + BaseCodeClass.CompanyID + "&ImageAddress=" + 1;
        Glide.with(this).load(url)
                .placeholder(R.drawable.dehkade)
                .error(R.drawable.dehkade)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(animLogo);
    }

}
