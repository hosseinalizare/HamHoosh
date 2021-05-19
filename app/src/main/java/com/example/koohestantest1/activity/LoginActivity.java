package com.example.koohestantest1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koohestantest1.ForgotPasswordActivity;
import com.example.koohestantest1.MainActivity;
import com.example.koohestantest1.R;
import com.example.koohestantest1.local_db.entity.NewsLetter;
import com.example.koohestantest1.model.NewsLetterModel;
import com.example.koohestantest1.model.VersioCheck;
import com.example.koohestantest1.model.network.RetrofitInstance;

import java.util.List;

import com.example.koohestantest1.ApiDirectory.JsonApi;
import com.example.koohestantest1.DB.DataBase;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.CheckVerification;
import com.example.koohestantest1.classDirectory.GetLoginDetail;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.GetVerifySmsClass;
import com.example.koohestantest1.classDirectory.HowToPay;
import com.example.koohestantest1.classDirectory.SendLoginDetail;
import com.example.koohestantest1.classDirectory.SendMobileNumberForSmsClass;
import com.example.koohestantest1.classDirectory.SendVerifyCode;
import com.example.koohestantest1.classDirectory.TermsAndConditions;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    JsonApi jsonApi;

    BaseCodeClass baseCodeClass;
    DataBase dataBase;
    EditText edUserName;
    EditText edPassword;

    String userName;
    String password;

    String deviceModel;
    String IMEI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dataBase = new DataBase(this);
        baseCodeClass = new BaseCodeClass();
        baseCodeClass.LoadBaseData(this);

        TextView tvWelcome = (TextView) findViewById(R.id.tvWelcomeSignUp);
        EditText EdPhoneNo = (EditText) findViewById(R.id.EdUserNameSignUp);
        final EditText EdPassword = (EditText) findViewById(R.id.EdPasswordSignUp);
        edUserName = (EditText) findViewById(R.id.EdUserNameSignUp);
        edPassword = (EditText) findViewById(R.id.EdPasswordSignUp);
        TextView tvForgot = (TextView) findViewById(R.id.tvForgot);
        Button btnLogin = (Button) findViewById(R.id.btnSignUp);
        TextView tvComment = (TextView) findViewById(R.id.tvCommentLogin);
        TextView tvNewAccount = (TextView) findViewById(R.id.tvRulsSignUp);
        String greeting = "سلام، " + getString(R.string.greeting);
        tvWelcome.setText(greeting);

        tvForgot.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));

        final Retrofit retrofit = RetrofitInstance.getRetrofit();

        jsonApi = retrofit.create(JsonApi.class);

    }

    public void getMobileConfig() {
        try {
            BaseCodeClass.deviceModel = Build.MANUFACTURER
                    + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                    + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();
        } catch (Exception e) {
            toastMessage("ERROR IN Device Model");
        }

        //IMEI Code
        try {
            //IMEI = "IMEI No longer required";
            IMEI = BaseCodeClass.CompanyID;
            BaseCodeClass.IMEI = IMEI;
//            if ((Build.VERSION.RELEASE.compareTo("10")) == 0) {
//                IMEI = "android 10 Q";
//                baseCodeClass.IMEI = "android 10 Q";
//            } else {
//                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
//                    return;
//                }
//                IMEI = tm.getDeviceId();
//                baseCodeClass.IMEI = tm.getDeviceId();
//            }
        } catch (IllegalArgumentException e) {
            toastMessage(e.getMessage());
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            toastMessage("ERROR IN IMEI");
        }
    }

    public void sendLogin(final JsonApi callback) {
        try {
            SendLoginDetail sendLoginDetail = new SendLoginDetail(userName, password, baseCodeClass.IMEI, baseCodeClass.deviceModel);
            Call<GetLoginDetail> jsonObjectCall = jsonApi.getLoginDetail(sendLoginDetail);
            jsonObjectCall.enqueue(new Callback<GetLoginDetail>() {
                @Override
                public void onResponse(Call<GetLoginDetail> call, Response<GetLoginDetail> response) {
                    if (response.body() != null) {
                        GetLoginDetail getLoginDetail = new GetLoginDetail(response.body().getUserID(), response.body().getToken(), response.body().getMsg());


                        callback.onResponseLogin(getLoginDetail);
                    } else {
                        Toast.makeText(getApplicationContext(), "body null", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<GetLoginDetail> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(getApplicationContext(), "Internet", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void NewAccountClick(View view) {

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }


    public void btnLoginCLick(View view) {

        getMobileConfig();
        userName = edUserName.getText().toString();
        password = edPassword.getText().toString();

        sendLogin(new JsonApi() {
            @Override
            public Call<GetVerifySmsClass> getVerifySms(SendMobileNumberForSmsClass sendMobileNumberForSmsClass) {
                return null;
            }

            @Override
            public Call<CheckVerification> checkVerification(SendVerifyCode sendVerifyCode) {
                return null;
            }

            @Override
            public void onResponseCheckVerify(CheckVerification checkVerification) {

            }

            @Override
            public Call<GetLoginDetail> getLoginDetail(SendLoginDetail sendLoginDetail) {
                return null;
            }

            @Override
            public void onResponseLogin(GetLoginDetail getLoginDetail) {
                String msg = getLoginDetail.getMsg();
                if (msg.equals("ok")) {


                    baseCodeClass.setToken(getLoginDetail.getToken());
                    baseCodeClass.setUserID(getLoginDetail.getUserID());
                    baseCodeClass.setUserName(edUserName.getText().toString());
                    baseCodeClass.setPassword(edPassword.getText().toString());
                    String[] item = {baseCodeClass.getUserID(), baseCodeClass.getToken(), baseCodeClass.getDeviceModel(),
                            baseCodeClass.getIMEI(), baseCodeClass.getMobileNumber(), baseCodeClass.getUserName(), BaseCodeClass.getPassword()};
                    AddData(item, DataBase.BASE_TABLE, DataBase.BaseTableField);

                    Call<GetResualt> call = jsonApi.setCostumerID(baseCodeClass.getCompanyID(), baseCodeClass.getUserID(), baseCodeClass.getCompanyID() + baseCodeClass.getUserID());
                    call.enqueue(new Callback<GetResualt>() {
                        @Override
                        public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                            Toast.makeText(getApplicationContext(), "خوش آمدید", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, LoadingActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<GetResualt> call, Throwable t) {

                        }
                    });



                } else {
                    Toast.makeText(getApplicationContext(), "msg" + ">>" + getLoginDetail.getMsg(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public Call<GetResualt> setCostumerID(String company, String user, String custumerID) {
                return null;
            }

            @Override
            public void onResponseCostumerID(GetResualt getResualt) {

            }

            @Override
            public Call<TermsAndConditions> getTerms(String companyId) {
                return null;
            }

            @Override
            public Call<List<HowToPay>> getPays(String companyId) {
                return null;
            }

            @Override
            public Call<GetResualt> getLastVersion(VersioCheck versioCheck) {
                return null;
            }

            @Override
            public Call<List<NewsLetter>> getAllNews(String companyId, long time, String userId) {
                return null;
            }


            @Override
            public Call<GetResualt> setNewsLetter(NewsLetterModel newsLetter) {
                return null;
            }

            @Override
            public Call<GetResualt> uploadNewsLetterImage(String newsLetterId, String coId, String uID, String token, List<MultipartBody.Part> files) {
                return null;
            }
        });

    }

    boolean checkVisible = true;

    public void btnVisiblePassClick(View view) {
        EditText pass = findViewById(R.id.EdPasswordSignUp);
        ImageButton btn = findViewById(R.id.btnVisiblePass);

        if (checkVisible) {
            pass.setTransformationMethod(new HideReturnsTransformationMethod());
            btn.setBackgroundResource(R.drawable.invisiblepass);
            checkVisible = false;
        } else {
            pass.setTransformationMethod(new PasswordTransformationMethod());
            btn.setBackgroundResource(R.drawable.visiblepass);
            checkVisible = true;
        }

    }

    public void AddData(String[] item, String tableName, String[] newEntry) {
        boolean insertData = dataBase.addData(item, tableName, newEntry);
        if (insertData) {
            //toastMessage("Data Successfully Inserted");
        } else {
            toastMessage("Something went wrong");
        }
    }// end public void AddData

    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
