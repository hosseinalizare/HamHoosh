package com.example.koohestantest1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koohestantest1.activity.LoginActivity;

import java.util.concurrent.TimeUnit;

import com.example.koohestantest1.ApiDirectory.JsonApi;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.GetVerifySmsClass;
import com.example.koohestantest1.classDirectory.HardwareIdsMobile;
import com.example.koohestantest1.classDirectory.SendMobileNumberForSmsClass;
import com.example.koohestantest1.model.network.RetrofitInstance;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;
import static javax.crypto.Mac.getInstance;

public class MainActivity extends AppCompatActivity {

    String userId, msg, msgDetail;
    boolean result = false;
    JsonApi jsonApi;
    boolean msgRes = false;
    private Handler mHandler = new Handler();
    EditText EdPhoneNo;
    private CheckBox chkTerms;
    HardwareIdsMobile  hardwareIdsMobile ;
    LoadingDialog loadingDialog;
    TermsDialog termsDialog;

    BaseCodeClass baseCodeClass = new BaseCodeClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hardwareIdsMobile = new HardwareIdsMobile(MainActivity.this);
        loadingDialog = new LoadingDialog(MainActivity.this);
        termsDialog = new TermsDialog(MainActivity.this);
        chkTerms = findViewById(R.id.chkTerms);
        EdPhoneNo = (EditText)findViewById(R.id.EdPhoneNo);
        EdPhoneNo.setText(baseCodeClass.getMobileNumber());

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();


        final Retrofit retrofit = RetrofitInstance.getRetrofit(); /*new Retrofit.Builder()
                .baseUrl(baseCodeClass.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();*/

        jsonApi = retrofit.create(JsonApi.class);
    }

    /**
     * sendSms
     * @param mobile
     * @param imei
     * @param device
     * @return
     */
    public boolean sendSms(String mobile, String imei, String device){
        try {
            if(mobile == null || imei == null || device == null){
               toastMessage("mobile or imei or device is null");
                return false;
            }
            SendMobileNumberForSmsClass sendmobile = new SendMobileNumberForSmsClass(mobile, imei, device);
            Call<GetVerifySmsClass> call = jsonApi.getVerifySms(sendmobile);
            call.enqueue(new Callback<GetVerifySmsClass>() {
                @Override
                public void onResponse(Call<GetVerifySmsClass> call, Response<GetVerifySmsClass> response) {
                    loadingDialog.startLoadingDialog();
                    userId = response.body().getUserID();
                    msg = response.body().getMsg();
                    msgDetail = response.body().getMsgDetaile();
                    SmsActivity();

                }
                @Override
                public void onFailure(Call<GetVerifySmsClass> call, Throwable t) {
                    loadingDialog.dismissDialog();
                    Toast.makeText(getApplicationContext(), "No Network", Toast.LENGTH_LONG).show();
                }
            });
            return true;
        }catch (Exception e){
            Log.d("Error", e.getMessage());
            return false;
        }
    }// end public boolean sendSms

    /**
     * SmsActivity
     */
    public void SmsActivity(){
        try {
            if (msg.equals("OK")) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissDialog();
                        Intent intent = new Intent(MainActivity.this, GenerantCodeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 2000);

            } else
            {
                loadingDialog.dismissDialog();
                if (baseCodeClass.debug) {
                    Toast.makeText(getApplicationContext(), msg + ">>>" + msgDetail, Toast.LENGTH_LONG).show();
                }
            }
        }catch (Exception e){
            Log.d("Error", e.getMessage());
        }
    }// end smsActivity

    /**
     * tvSignUpClick
     * @param view
     */
    public void tvSignUpClick(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }//end tvSignUpClick


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void btnGetCodeClick(View view) {
        if(chkTerms.isChecked()) {
            try {
                BaseCodeClass.mobileNumber = baseCodeClass.CheckMobileNumber(EdPhoneNo.getText().toString());
                if (!hardwareIdsMobile.getMobileConfig()) {
                    // toastMessage("OnTime");
                    // hardwareIdsMobile.getMobileConfig();
                    //toastMessage("OnTime >> 1");
                    // sendSms();
                } else {
                    sendSms();
                }

            } catch (Exception e) {
                if (baseCodeClass.debug)
                    Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "برای ورود به برنامه باید با قوانین و مقررات موافقت کنید", Toast.LENGTH_SHORT).show();
        }
    }// end btnGetCodeClick

    int index = 0;

    public void txtShowTermsClick(View view){
        termsDialog.startTermsDialog();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendSms() {
        try {
            if(baseCodeClass.mobileNumber == null || baseCodeClass.mobileNumber.equals(""))
            {
                EdPhoneNo.setBackgroundResource(R.drawable.edittextrongnmobile);
                TextView tvMessage = (TextView)findViewById(R.id.tvMsg);
                tvMessage.setText("لطفا شماره تلفن همراه خود را به درستی وارد کنید");
                return;
            }

            if (sendSms(BaseCodeClass.mobileNumber, BaseCodeClass.IMEI, BaseCodeClass.deviceModel))
            {
                index = 0;
            }
            else
            {
                Toast.makeText(getApplicationContext(), "مجدد تلاش کنید", Toast.LENGTH_SHORT).show();
                //  hardwareIdsMobile.getMobileConfig();
                index++;
                if (index < 1){
                    sendSms();
                }

            }
        }catch (Exception e){
            logMessage("MainActivity 100 >> " + e.getMessage(), this);
        }

    }


    public void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }


}
