package com.example.koohestantest1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koohestantest1.ApiDirectory.JsonApi;
import com.example.koohestantest1.activity.ReportActivity;
import com.example.koohestantest1.adapter.IconListViewAdapter;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.UpdateApplication;
import com.example.koohestantest1.downloadmanager.DownloadReceiver;
import com.example.koohestantest1.downloadmanager.FileDownloadManager;
import com.example.koohestantest1.fragments.transinterface.ISettingClick;

import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.model.VersioCheck;
import com.example.koohestantest1.model.network.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileSettingActivity extends AppCompatActivity implements ISettingClick {

    ListView settingList;
    TextView appVer;
    BaseCodeClass baseCodeClass;
    JsonApi jsonApi;
    Retrofit retrofit;

    private final DownloadReceiver downloadReceiver = new DownloadReceiver(null);
    ActivityResultLauncher<String> permissionResult = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (result) {
            getLastVersion();
        } else
            Toast.makeText(this, "برای دانلود کردن آخرین نسخه، نیاز به اجازه دسترسی دارید", Toast.LENGTH_SHORT).show();
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

//        String[] settingItem = {"بروز رسانی", "تماس باما", "گزارش خطا"};
//        int[] settingIcon = {R.drawable.update_icon, R.drawable.contact_us, R.drawable.bug};

        String[] settingItem = {"بروز رسانی", "گزارش خطا"};
        int[] settingIcon = {R.drawable.update_icon, R.drawable.bug};

        IconListViewAdapter adapter = new IconListViewAdapter(this, settingItem, settingIcon,this);

        settingList = findViewById(R.id.settingListView);
        settingList.setAdapter(adapter);

        baseCodeClass = new BaseCodeClass();
        appVer = findViewById(R.id.txtAppVersion);
        registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void getLastVersion(){
        VersioCheck versioCheck = new VersioCheck();
        versioCheck.setCompanyID(BaseCodeClass.CompanyID);
        versioCheck.setUserID(BaseCodeClass.userID);
        versioCheck.setVersionName(getResources().getString(R.string.LastVer) + ".apk");
        retrofit = RetrofitInstance.getRetrofit();
        jsonApi = retrofit.create(JsonApi.class);
        Call<GetResualt> call = jsonApi.getLastVersion(versioCheck);
        call.enqueue(new Callback<GetResualt>() {
            @Override
            public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                if(response.body().getResualt().equals("134")){
                    //updateApp();
                    //FileDownloadManager fileDownloadManager = new FileDownloadManager(getApplicationContext()); This line has been commented temporary
                    String url = "https://dehkade.nokhbgan.ir/api/User/DownloadLastVersion?CompanyID=" + BaseCodeClass.CompanyID;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    BaseCodeClass.appName = response.body().getMsg();
                    //BaseCodeClass.appId = fileDownloadManager.downloadFromUrl(url,BaseCodeClass.appName,FileDownloadManager.APK_FORMAT); This line has been commented temporary

                }else
                    Toast.makeText(ProfileSettingActivity.this, "نسخه جدیدی وجود ندارد", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetResualt> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

    public void closeBtn(View view) {
        finish();
    }

    @Override
    public void onUpdateClicked() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            getLastVersion();
        } else {
            permissionResult.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

    }

    @Override
    public void onReportClicked() {
        Intent intent = new Intent(this, ReportActivity.class);
        startActivity(intent);
    }

    private void updateApp() {
        FileDownloadManager fileDownloadManager = new FileDownloadManager(this);
        long id = fileDownloadManager.downloadFromUrl(baseCodeClass.pBASE_URL + "User/DownloadLastVersion?CompanyID=" + baseCodeClass.getCompanyID(), "dehkade_latest_version", FileDownloadManager.APK_FORMAT);
        downloadReceiver.setDownloadId(id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(downloadReceiver);
    }


}