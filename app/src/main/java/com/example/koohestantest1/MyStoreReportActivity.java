package com.example.koohestantest1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.koohestantest1.downloadmanager.DownloadReceiver;
import com.example.koohestantest1.downloadmanager.IDownloadManager;
import com.example.koohestantest1.model.MyTime;
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
import static com.example.koohestantest1.classDirectory.BaseCodeClass.orderClassList;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.userOrder;

public class MyStoreReportActivity extends AppCompatActivity implements CartApi, IDownloadManager {

    RecyclerView reportRecyclerView;
    BaseCodeClass baseCodeClass;
    GetOnlineInformationClass getOnlineInformationClass;
    RelativeLayout preparingBack, sendingBack, readyBack, deliveredBack, canceledBack, checkBack;

    boolean receiveData = false;
    String statusID = "1";
    Context mContext;
    List<SendOrderClass> orderList;

    private TimeViewModel timeViewModel;

    private String TAG = MyStoreReportActivity.class.getSimpleName() + "Debug";

    private static MyTime sharedTime;

    private boolean checker;

    private CompanyOrderRecyclerViewAdapter adapter;
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
        setContentView(R.layout.activity_my_store_report);

        registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        timeViewModel = new ViewModelProvider(this).get(TimeViewModel.class);

        checker = getIntent().hasExtra("mode");

        if (getIntent().hasExtra("status")) {
            statusID = getIntent().getStringExtra("status");
        }


        //get called data
        timeViewModel.getCurrentTimeLiveData().observe(this, fullData -> {
            Log.d(TAG, "onCreate: date: " + fullData.getCurrentDate());
            Log.d(TAG, "onCreate: time" + fullData.getCurrentTime());

            //after getting current data from the server init lists
            Log.d(TAG, "onCreate: " + checker);
            getReportsFromServer(checker);
        });
        timeViewModel.getErrorMessage().observe(this, s -> {
            Log.d(TAG, "onCreate: Error " + s);
        });


        baseCodeClass = new BaseCodeClass();
        mContext = MyStoreReportActivity.this;


        reportRecyclerView = findViewById(R.id.reportRecycler);
        preparingBack = findViewById(R.id.preparingBack);
        sendingBack = findViewById(R.id.sendingBack);
        readyBack = findViewById(R.id.readyBack);
        deliveredBack = findViewById(R.id.deliveredBack);
        canceledBack = findViewById(R.id.canceledBack);
        checkBack = findViewById(R.id.checkBack);


        //setup Rv
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        reportRecyclerView.setLayoutManager(layoutManager);
         adapter = new CompanyOrderRecyclerViewAdapter(this, null, timeViewModel, checker, this::onDownloadCalled, resultLauncherPermission);
//         adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT);
        reportRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //calls for get time from server
        timeViewModel.callForCurrentTime(4);
    }

    public void initOrderRecyclerView(List<SendOrderClass> sendOrderClass) {
        if (sendOrderClass.size() == 0) {
            Toast.makeText(this, "سفارشی برای نمایش وجود ندارد", Toast.LENGTH_SHORT).show();
        }else {
            adapter.updateData(sendOrderClass);
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
            if (sendOrderClasses != null) {
                receiveData = true;
            }
            orderList = sendOrderClasses;
            orderClassList = orderList;

            Log.d(TAG, "onResponseGetCompanyOrder: status: " + statusID);
            Log.d(TAG, "onResponseGetCompanyOrder: size : " + sendOrderClasses.size());
            int status = Integer.parseInt(statusID);
            if (status < 6) {
                filterOrder(statusID);
            } else {
                filterCancelOrder();
            }
        } catch (Exception e) {
            logMessage("MyStoreReport 200 >> " + e.getMessage(), mContext);
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
        try {
            if (sendOrderClasses != null) {
                receiveData = true;
            }
            orderList = sendOrderClasses;
            userOrder = sendOrderClasses;

            int status = Integer.parseInt(statusID);

            Log.d(TAG, "onResponseUserOrder: status: " + statusID);
            Log.d(TAG, "onResponseUserOrder: size : " + sendOrderClasses.size());

            if (status < 6) {
                filterOrder(statusID);
            } else {
                filterCancelOrder();
            }
        } catch (Exception e) {
            logMessage("MyStoreReport 300 >> " + e.getMessage(), mContext);
        }

    }

    public void setDefaultColor(RelativeLayout layout) {
        preparingBack.setBackgroundResource(R.color.white);
        sendingBack.setBackgroundResource(R.color.white);
        readyBack.setBackgroundResource(R.color.white);
        deliveredBack.setBackgroundResource(R.color.white);
        canceledBack.setBackgroundResource(R.color.white);
        checkBack.setBackgroundResource(R.color.white);

        layout.setBackgroundResource(R.color.LighterBlue);
    }

    public RelativeLayout selectedLayout(String id) {
        if (id.equals("1")) {
            return checkBack;
        }
        if (id.equals("2")) {
            return preparingBack;
        }
        if (id.equals("3")) {
            return readyBack;
        }
        if (id.equals("4")) {
            return sendingBack;
        }
        if (id.equals("5")) {
            return deliveredBack;
        }
        return canceledBack;
    }

    @SuppressLint("ResourceType")
    public void cancelCard(View view) {
        filterCancelOrder();
        setDefaultColor(canceledBack);
    }

    public void deliveredCard(View view) {
        onFilterClicked("5");
        setDefaultColor(deliveredBack);
    }

    public void readyCard(View view) {
        onFilterClicked("3");
        setDefaultColor(readyBack);
    }

    public void sendingCard(View view) {
        onFilterClicked("4");
        setDefaultColor(sendingBack);
    }

    public void preparationCard(View view) {
        onFilterClicked("2");
        setDefaultColor(preparingBack);
    }

    public void checkCard(View view) {
        onFilterClicked("1");
        setDefaultColor(checkBack);
    }

    public void filterOrder(String status) {
        try {
            if (!receiveData) {
                return;
            }
            List<SendOrderClass> list = new ArrayList<>();

            for (SendOrderClass soc : orderList
            ) {
                Log.d(TAG, "filterOrder: " + soc.toString());
                if (soc.getStatusID().equals(status)) {

                    list.add(soc);
                }
            }
            initOrderRecyclerView(list);
        } catch (Exception e) {
            logMessage("MyStoreReport 400 >> " + e.getMessage(), mContext);
        }
    }

    public void onFilterClicked(String id) {
        filterOrder(id);
        statusID = id;
    }

    public void filterCancelOrder() {
        try {
            if (!receiveData) {
                return;
            }
            List<SendOrderClass> list = new ArrayList<>();

            for (SendOrderClass soc : orderList
            ) {
                if (soc.getStatusID().equals("5") || soc.getStatusID().equals("6") || soc.getStatusID().equals("7")
                        || soc.getStatusID().equals("8") || soc.getStatusID().equals("9")) {
                    list.add(soc);
                }
            }
            initOrderRecyclerView(list);
        } catch (Exception e) {
            logMessage("MyStoreReport 500 >> " + e.getMessage(), mContext);
        }
    }

    public void getReportsFromServer(boolean userMode) {
        try {
            getOnlineInformationClass = new GetOnlineInformationClass(MyStoreReportActivity.this, this);


            setDefaultColor(selectedLayout(statusID));

            Log.d(TAG, "getReportsFromServer: " + userMode);
            if (userMode) {

                //  orderList = userOrder;
                Handler nHandler = new Handler();
                nHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getOnlineInformationClass.readUserOrder(baseCodeClass.getUserID());
                        Log.d(TAG, "run: user order" + baseCodeClass.getUserID());
                    }
                }, 100);
            } else {
                Handler aHandler = new Handler();
                aHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getOnlineInformationClass.readCompanyOrder(baseCodeClass.getCompanyID());
                        Log.d(TAG, "run: company order" + baseCodeClass.getUserID());

                    }
                }, 100);
            }
        } catch (Exception e) {
            logMessage("MyStoreReport 100 >> " + e.getMessage(), mContext);
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
