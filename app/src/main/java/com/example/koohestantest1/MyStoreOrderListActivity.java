package com.example.koohestantest1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.koohestantest1.downloadmanager.DownloadReceiver;
import com.example.koohestantest1.downloadmanager.IDownloadManager;
import com.example.koohestantest1.viewModel.TimeViewModel;

import java.util.ArrayList;
import java.util.List;

import com.example.koohestantest1.ApiDirectory.CartApi;
import com.example.koohestantest1.InfoDirectory.GetOnlineInformationClass;
import com.example.koohestantest1.ViewModels.UpdateOrderClass;
import com.example.koohestantest1.ViewModels.UserReportViewModel;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.CompanyOrderRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.SendOrderClass;
import retrofit2.Call;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;

public class MyStoreOrderListActivity extends AppCompatActivity implements CartApi, IDownloadManager {

    RecyclerView orderRecyclerView;
    BaseCodeClass baseCodeClass;
    GetOnlineInformationClass getOnlineInformationClass;
    private String TAG = MyStoreOrderListActivity.class.getSimpleName();

    private TimeViewModel timeViewModel;
    List<String> ms = new ArrayList<>();

    private ProgressBar progressBar;
    private RelativeLayout relativeLayoutContainerList;

    private DownloadReceiver downloadReceiver = new DownloadReceiver(null);

    private final ActivityResultLauncher<String> resultLauncherPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
                if (result) {
                    Toast.makeText(this, "اجازه دسترسی داد شد، برای دانلود روی عکس نکه دارید", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "بدون اجازه دسترسی، فاکتور سفارش دانلود نمیشه!", Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_store_order_list);

        registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        timeViewModel = new ViewModelProvider(this).get(TimeViewModel.class);
        progressBar = findViewById(R.id.pb_store_order);
        relativeLayoutContainerList = findViewById(R.id.rl_list_container);


        //listen for server answer
        timeViewModel.getCurrentTimeLiveData().observe(this, time -> {
            getListFromServer();

        });


        Log.d(TAG, "onCreate: ");
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");

        //calls for getting current time from server
        timeViewModel.callForCurrentTime(4);
    }

    public void initOrderRecyclerView(List<SendOrderClass> sendOrderClass) {
        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(MyStoreOrderListActivity.this, LinearLayoutManager.VERTICAL, false);
            orderRecyclerView.setLayoutManager(layoutManager);
            CompanyOrderRecyclerViewAdapter adapter = new CompanyOrderRecyclerViewAdapter(MyStoreOrderListActivity.this, sendOrderClass, timeViewModel, false, this::onDownloadCalled , resultLauncherPermission);
            orderRecyclerView.setAdapter(adapter);
        } catch (Exception e) {
            logMessage("5 >> " + e.getMessage(), MyStoreOrderListActivity.this);
        }
    }

    public void btnBakClick(View view) {
        finish();
    }

    @Override
    public Call<GetResualt> sendOrder(SendOrderClass sendOrderClass) {
        return null;
    }

    @Override
    public void onResponseSendOrder(GetResualt getResualt) {

    }

    @Override
    public Call<List<SendOrderClass>> getCompanyOrder(String companyID) {
        return null;
    }

    @Override
    public void onResponseGetCompanyOrder(List<SendOrderClass> sendOrderClasses) {

        try {
            List<SendOrderClass> list = new ArrayList<>();
            for (SendOrderClass soc : sendOrderClasses
            ) {
                if (soc.getStatusID().equals("1")) {
                    list.add(soc);
                    Log.d(TAG, "onResponseGetCompanyOrder: " + soc.getSumTotal());
                }
            }
            initOrderRecyclerView(list);

            //setUp view visibility
            progressBar.setVisibility(View.GONE);
            relativeLayoutContainerList.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            logMessage("MyStoreOrderList 200 >> " + e.getMessage(), this);
        }
    }

    @Override
    public Call<GetResualt> updateOrder(UpdateOrderClass updateOrderClass) {
        return null;
    }

    @Override
    public Call<List<SendOrderClass>> getUserOrder(UserReportViewModel userReportViewModel) {
        return null;
    }

    @Override
    public void onResponseUserOrder(List<SendOrderClass> sendOrderClasses) {

    }

    public void getListFromServer() {
        try {

            orderRecyclerView = findViewById(R.id.orderListRecyclerView);

            baseCodeClass = new BaseCodeClass();

            getOnlineInformationClass = new GetOnlineInformationClass(MyStoreOrderListActivity.this, this);

            getOnlineInformationClass.readCompanyOrder(baseCodeClass.getCompanyID());

        } catch (Exception e) {
            baseCodeClass.logMessage(e.getMessage(), MyStoreOrderListActivity.this);
        }
    }

    @Override
    public void onDownloadCalled(long id) {
        downloadReceiver.setDownloadId(id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(downloadReceiver);
    }
}
