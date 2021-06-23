package com.example.koohestantest1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koohestantest1.activity.LoadingActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.example.koohestantest1.ApiDirectory.JsonApi;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.CheckVerification;
import com.example.koohestantest1.classDirectory.GetLoginDetail;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.GetVerifySmsClass;
import com.example.koohestantest1.classDirectory.HardwareIdsMobile;
import com.example.koohestantest1.classDirectory.HowToPay;
import com.example.koohestantest1.classDirectory.SendLoginDetail;
import com.example.koohestantest1.classDirectory.SendMobileNumberForSmsClass;
import com.example.koohestantest1.classDirectory.SendVerifyCode;
import com.example.koohestantest1.classDirectory.TermsAndConditions;
import com.example.koohestantest1.local_db.entity.NewsLetter;
import com.example.koohestantest1.model.DeleteNewsLetter;
import com.example.koohestantest1.model.NewsLetterModel;
import com.example.koohestantest1.model.VersioCheck;
import com.example.koohestantest1.model.network.RetrofitInstance;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;

public class GenerantCodeActivity extends AppCompatActivity {
    public String generateCode;
    JsonApi jsonApi;
    String userId, token, msg, msgDetail;

    BaseCodeClass baseCodeClass = new BaseCodeClass();
    HardwareIdsMobile hardwareIdsMobile;
    CountDownTimer downTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generant_code);
        hardwareIdsMobile = new HardwareIdsMobile(this);


        TextView tvComment = findViewById(R.id.tvComment);
        TextView tvEdit = findViewById(R.id.tvEditPhoneNo);
        final TextView tvTimerC = findViewById(R.id.tvTimerComment);
        final TextView tvTimer = findViewById(R.id.tvTimer);
        Button btnGenerate = findViewById(R.id.btnGenerateCode);
        final EditText ed1 = findViewById(R.id.editText1);
        final EditText ed2 = findViewById(R.id.editText2);
        final EditText ed3 = findViewById(R.id.editText3);
        final EditText ed4 = findViewById(R.id.editText4);

        Typeface wYekan = Typeface.createFromAsset(getAssets(), "fonts/koodak.TTF");
        Typeface wYekanBold = Typeface.createFromAsset(getAssets(), "fonts/koodak.TTF");
        Typeface Bnazanin = Typeface.createFromAsset(getAssets(), "fonts/koodak.TTF");

        tvComment.setTypeface(wYekanBold);
        tvEdit.setTypeface(wYekan);
        tvTimerC.setTypeface(Bnazanin);
        tvTimer.setTypeface(Bnazanin);
        btnGenerate.setTypeface(wYekanBold);

        tvEdit.setOnClickListener(v -> {
            startActivity(new Intent(GenerantCodeActivity.this, MainActivity.class));
            finish();
        });

        downTimer = new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvTimerC.setText("تا دریافت مجدد کد");
                tvTimer.setText("" + String.format("%d : %d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                tvTimer.setText("");
                tvTimerC.setText("دریافت مجدد کد تایید");
                tvTimerC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sendSms(baseCodeClass.getMobileNumber(), baseCodeClass.getIMEI(), baseCodeClass.getDeviceModel())) {
                            downTimer.start();
                        }
                    }
                });
            }
        }.start();

        ed1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ed1.getText().toString().trim().length() >= 1) {
                    ed1.clearFocus();
                    ed2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ed2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ed2.getText().toString().trim().length() >= 1) {
                    ed2.clearFocus();
                    ed3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ed3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ed3.getText().toString().trim().length() >= 1) {
                    ed3.clearFocus();
                    ed4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ed4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ed4.getText().toString().trim().length() < 0) {
                    ed4.clearFocus();
                    ed3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final Retrofit retrofit = RetrofitInstance.getRetrofit(); /*new Retrofit.Builder()
                .baseUrl(baseCodeClass.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();*/

        jsonApi = retrofit.create(JsonApi.class);
    }// end OnCreate

    public void sendVerifyCode(final JsonApi callback) {
        try {
            if (generateCode.length() < 4) {
                Toast.makeText(getApplicationContext(), "لطفا کد را وارد کنید", Toast.LENGTH_LONG).show();
            }
            SendVerifyCode sendVerifyCode = new SendVerifyCode(generateCode, baseCodeClass.mobileNumber, baseCodeClass.IMEI, baseCodeClass.deviceModel);
            Call<CheckVerification> call = jsonApi.checkVerification(sendVerifyCode);
            call.enqueue(new Callback<CheckVerification>() {
                @Override
                public void onResponse(Call<CheckVerification> call, Response<CheckVerification> response) {
                    if (response.body() != null) {
                        CheckVerification checkVerification = new CheckVerification(response.body().getUserID(),
                                response.body().getToken(),
                                response.body().getMsg());
                        callback.onResponseCheckVerify(checkVerification);
                    }
                }

                @Override
                public void onFailure(Call<CheckVerification> call, Throwable t) {
                    Log.d("Error",t.getMessage());
                }
            });
        } catch (Exception e) {
           Log.d("Error", e.getMessage());
        }
    }// end public void sendVerifyCode


    public void AddData(String[] item, String tableName, String[] newEntry) {
        /*boolean insertData = dataBase.addData(item, tableName, newEntry);

        try {
            if (insertData) {
                toastMessage("Data Successfully Inserted");
            } else {
                toastMessage("Something went wrong");
            }
        } catch (Exception e) {
            logMessage("GenerateCode 100 >> " + e.getMessage(), this);
        }*/
    }// end public void AddData

    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public void startApp() {
//        try {
//            final EditText ed1 = (EditText) findViewById(R.id.editText1);
//            final EditText ed2 = (EditText) findViewById(R.id.editText2);
//            final EditText ed3 = (EditText) findViewById(R.id.editText3);
//            final EditText ed4 = (EditText) findViewById(R.id.editText4);
//
//            if (msg == "OK") {
//                if (token != null || token != "") {
//
//                    String[] item = {baseCodeClass.getUserID(), baseCodeClass.getToken(), baseCodeClass.getDeviceModel(),
//                            baseCodeClass.getIMEI(), baseCodeClass.getMobileNumber(), baseCodeClass.getUserName(), baseCodeClass.getPassword()};
//
//                    AddData(item, dataBase.BASE_TABLE, dataBase.BaseTableField);
//
//                    Intent intent = new Intent(GenerantCodeActivity.this, LoadingActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            } else {
//                ColorStateList colorStateList = ColorStateList.valueOf(Color.rgb(255, 23, 68));
//                ed1.setBackgroundTintList(colorStateList);
//                ed2.setBackgroundTintList(colorStateList);
//                ed3.setBackgroundTintList(colorStateList);
//                ed4.setBackgroundTintList(colorStateList);
//                if (baseCodeClass.debug)
//                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//            }
//        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
//        }
//    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void btnGeneratCLick(View view) {

        final EditText ed1 = (EditText) findViewById(R.id.editText1);
        final EditText ed2 = (EditText) findViewById(R.id.editText2);
        final EditText ed3 = (EditText) findViewById(R.id.editText3);
        final EditText ed4 = (EditText) findViewById(R.id.editText4);

        hardwareIdsMobile.getMobileConfig();

        generateCode = ed1.getText().toString() + ed2.getText().toString() + ed3.getText().toString() + ed4.getText().toString();


        sendVerifyCode(new JsonApi() {
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
                String msg = checkVerification.getMsg();
                if (msg.equals("ok")) {

                    BaseCodeClass.userID = checkVerification.getUserID();
                    baseCodeClass.setToken(checkVerification.getToken());

                    String[] item = {baseCodeClass.getUserID(), baseCodeClass.getToken(), baseCodeClass.getDeviceModel(),
                            baseCodeClass.getIMEI(), baseCodeClass.getMobileNumber(), baseCodeClass.getUserName(), BaseCodeClass.getPassword()};

                    //AddData(item, DataBase.BASE_TABLE, DataBase.BaseTableField);
                    //TODO
                    /**
                     * Create new table or shared prefrences and add item's value into it
                     */
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("baseInfo",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userId",baseCodeClass.getUserID());
                    editor.putString("token",baseCodeClass.getToken());
                    editor.putString("deviceModel",baseCodeClass.getDeviceModel());
                    editor.putString("imei", BaseCodeClass.getIMEI());
                    editor.putString("mobileNumber", baseCodeClass.getMobileNumber());
                    editor.putString("username", baseCodeClass.getUserName());
                    editor.putString("password", BaseCodeClass.getPassword());
                    editor.apply();
                    setCostumerMethid(baseCodeClass.getCompanyID(), baseCodeClass.getUserID());


                } else {
                    ColorStateList colorStateList = ColorStateList.valueOf(Color.rgb(255, 23, 68));
                    ed1.setBackgroundTintList(colorStateList);
                    ed2.setBackgroundTintList(colorStateList);
                    ed3.setBackgroundTintList(colorStateList);
                    ed4.setBackgroundTintList(colorStateList);

                    Toast.makeText(getApplicationContext(), msg + ">>" + "کد وارد شده صحیح نمیباشد", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public Call<GetLoginDetail> getLoginDetail(SendLoginDetail sendLoginDetail) {
                return null;
            }

            @Override
            public void onResponseLogin(GetLoginDetail getLoginDetail) {

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

            @Override
            public Call<GetResualt> deleteOneNewsLetter(DeleteNewsLetter deleteNewsLetter) {
                return null;
            }
        });


    }

    private void setCostumerMethid(String company, String user) {
        try {
            Call<GetResualt> call = jsonApi.setCostumerID(company, user, company + user);
            call.enqueue(new Callback<GetResualt>() {
                @Override
                public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                    nextPage();
                }

                @Override
                public void onFailure(Call<GetResualt> call, Throwable t) {
                    nextPage();
                }
            });
        } catch (Exception e) {
            logMessage("GenerateCode 200 >> " + e.getMessage(), this);
        }
    }

    public void nextPage() {
        try {
            Toast.makeText(getApplicationContext(), "خوش آمدید", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(GenerantCodeActivity.this, LoadingActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            logMessage("GenerateCode 300 >> " + e.getMessage(), this);
        }
    }

    public boolean sendSms(String mobile, String imei, String device) {
        try {
            if (mobile == null || imei == null || device == null) {
                //Toast.makeText(getApplicationContext(), "khsig", Toast.LENGTH_LONG).show();
                return false;
            }
            SendMobileNumberForSmsClass sendmobile = new SendMobileNumberForSmsClass(mobile, imei, device);
            Call<GetVerifySmsClass> call = jsonApi.getVerifySms(sendmobile);
            call.enqueue(new Callback<GetVerifySmsClass>() {
                @Override
                public void onResponse(Call<GetVerifySmsClass> call, Response<GetVerifySmsClass> response) {
                    userId = response.body().getUserID();
                    msg = response.body().getMsg();
                    msgDetail = response.body().getMsgDetaile();
                }

                @Override
                public void onFailure(Call<GetVerifySmsClass> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Internet", Toast.LENGTH_LONG).show();
                }
            });
            return true;
        } catch (Exception e) {
            Log.d("Error",e.getMessage());
            return false;
        }
    }// end public boolean sendSms

}
