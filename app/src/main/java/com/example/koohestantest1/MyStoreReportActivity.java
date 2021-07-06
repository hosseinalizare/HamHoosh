package com.example.koohestantest1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koohestantest1.Utils.TimeUtils;
import com.example.koohestantest1.downloadmanager.DownloadReceiver;
import com.example.koohestantest1.downloadmanager.IDownloadManager;
import com.example.koohestantest1.model.DiscountModel;
import com.example.koohestantest1.model.MyTime;
import com.example.koohestantest1.viewModel.TimeViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.koohestantest1.ApiDirectory.CartApi;
import com.example.koohestantest1.InfoDirectory.GetOnlineInformationClass;
import com.example.koohestantest1.ViewModels.UpdateOrderClass;
import com.example.koohestantest1.ViewModels.UserReportViewModel;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.CompanyOrderRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.SendOrderClass;
import com.google.android.material.switchmaterial.SwitchMaterial;

import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import retrofit2.Call;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.orderClassList;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.userOrder;

public class MyStoreReportActivity extends AppCompatActivity implements CartApi, IDownloadManager {

    RecyclerView reportRecyclerView;
    BaseCodeClass baseCodeClass;
    GetOnlineInformationClass getOnlineInformationClass;
    RelativeLayout preparingBack, sendingBack, readyBack, deliveredBack, canceledBack, checkBack, allOrderBack;
    RelativeLayout relToday, rel3Day, rel1Month, relByDate;
    LinearLayout linearShowFilterDate;
    MyTime currentTime;
    CardView cardStartTimeFilter, cardEndTimeFilter;
    TextView txtStartTimeFilter, txtEndTimeFilter;
    SwitchMaterial swShowReport;
    List<SendOrderClass> currentOrders;
    boolean receiveData = false;
    String statusID = "1", filterId = "";
    Context mContext;
    List<SendOrderClass> orderList;
    List<SendOrderClass> filterList;

    private TimeViewModel timeViewModel;

    private String TAG = MyStoreReportActivity.class.getSimpleName() + "Debug";

    private static MyTime sharedTime;

    private boolean checker;
    private Date start, end;


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
        filterList = new ArrayList<>();
        currentOrders = new ArrayList<>();
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
        allOrderBack = findViewById(R.id.allOrderBack);

        relToday = findViewById(R.id.relative_filter_today);
        rel3Day = findViewById(R.id.relative_filter_3day);
        rel1Month = findViewById(R.id.relative_filter_1month);
        relByDate = findViewById(R.id.relative_filter_byDate);
        linearShowFilterDate = findViewById(R.id.linearShowFilteringByDate);
        cardStartTimeFilter = findViewById(R.id.cardStartDateFilter);
        cardEndTimeFilter = findViewById(R.id.cardEndDateFilter);
        txtStartTimeFilter = findViewById(R.id.txtStartTimeFilter);
        txtEndTimeFilter = findViewById(R.id.txtEndTimeFilter);

        swShowReport = findViewById(R.id.sw_showReport);

        //setup Rv
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        reportRecyclerView.setLayoutManager(layoutManager);
        adapter = new CompanyOrderRecyclerViewAdapter(this, null, timeViewModel, checker, this::onDownloadCalled, resultLauncherPermission);
//         adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT);
        reportRecyclerView.setAdapter(adapter);




        /*setDefaultRelBackgroundColor(relToday);*/


        relToday.setOnClickListener(v -> {

            currentTime = timeViewModel.getCurrentTimeLiveData().getValue();
            try {
                Date nowDate = TimeUtils.getDateFromString(currentTime.getCurrentDate() + " " + currentTime.getCurrentTime(), 0);
                Calendar cal = Calendar.getInstance();
                cal.setTime(nowDate);
                cal.add(Calendar.HOUR, -24);
                filterByDateOrder(cal.getTime(), nowDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            setDefaultRelBackgroundColor(relToday);
            if (linearShowFilterDate.getVisibility() == View.VISIBLE) {
                linearShowFilterDate.setVisibility(View.GONE);
                setDefaultText();
            }
        });

        swShowReport.setOnCheckedChangeListener((buttonView, isChecked) -> {
            currentOrders.clear();
            if (isChecked) {
                if (statusID.equals("")) {
                    for (SendOrderClass currentOrder : orderList) {
                        if (currentOrder.getCompanyID().equals(BaseCodeClass.CompanyID)) {
                            currentOrders.add(currentOrder);
                        }
                    }
                    if (currentOrders != null || !currentOrders.isEmpty()) {
                        initOrderRecyclerView(currentOrders);

                    }
                } else if (statusID.equals("-1")) {
                    filterCancelOrder();
                } else {
                    onFilterClicked(statusID);
                }
            } else if (statusID.equals("")) {
                initOrderRecyclerView(orderList);
            } else if (statusID.equals("-1")) {
                filterCancelOrder();
            } else {
                onFilterClicked(statusID);
            }
        });

        rel3Day.setOnClickListener(v -> {

            currentTime = timeViewModel.getCurrentTimeLiveData().getValue();
            try {

                Date nowDate = TimeUtils.getDateFromString(currentTime.getCurrentDate() + " " + currentTime.getCurrentTime(), 0);
                Calendar cal = Calendar.getInstance();
                cal.setTime(nowDate);
                cal.add(Calendar.HOUR, -168);
                filterByDateOrder(cal.getTime(), nowDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }


            setDefaultRelBackgroundColor(rel3Day);
            if (linearShowFilterDate.getVisibility() == View.VISIBLE) {
                linearShowFilterDate.setVisibility(View.GONE);
                setDefaultText();

            }
        });

        rel1Month.setOnClickListener(v -> {

            currentTime = timeViewModel.getCurrentTimeLiveData().getValue();
            try {

                Date nowDate = TimeUtils.getDateFromString(currentTime.getCurrentDate() + " " + currentTime.getCurrentTime(), 0);
                Calendar cal = Calendar.getInstance();
                cal.setTime(nowDate);
                cal.add(Calendar.HOUR, -744);
                filterByDateOrder(cal.getTime(), nowDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            setDefaultRelBackgroundColor(rel1Month);
            if (linearShowFilterDate.getVisibility() == View.VISIBLE) {
                linearShowFilterDate.setVisibility(View.GONE);
                setDefaultText();


            }
        });
        relByDate.setOnClickListener(v -> {
            setDefaultRelBackgroundColor(relByDate);
            linearShowFilterDate.setVisibility(View.VISIBLE);
        });
        cardStartTimeFilter.setOnClickListener(v -> {
            chooseStartDate(txtStartTimeFilter);


        });

        cardEndTimeFilter.setOnClickListener(v -> {
            chooseEndDate(txtEndTimeFilter);


        });


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
        }
        adapter.updateData(sendOrderClass);

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

    @Override
    public Call<GetResualt> setDiscount(DiscountModel discountModel) {
        return null;
    }

    public void setDefaultColor(RelativeLayout layout) {
        preparingBack.setBackgroundResource(R.color.white);
        sendingBack.setBackgroundResource(R.color.white);
        readyBack.setBackgroundResource(R.color.white);
        deliveredBack.setBackgroundResource(R.color.white);
        canceledBack.setBackgroundResource(R.color.white);
        checkBack.setBackgroundResource(R.color.white);
        allOrderBack.setBackgroundResource(R.color.white);

        layout.setBackgroundResource(R.color.LighterBlue);
    }

    public void setUnSelectedFilterByTimeCard() {
        relToday.setBackgroundResource(R.color.white);
        rel3Day.setBackgroundResource(R.color.white);
        rel1Month.setBackgroundResource(R.color.white);
        relByDate.setBackgroundResource(R.color.white);
    }

    public void setDefaultText() {
        txtStartTimeFilter.setText("شروع تاریخ");
        txtEndTimeFilter.setText("پایان تاریخ");
        start = null;
        end = null;
    }

    public void setDefaultRelBackgroundColor(RelativeLayout relativeLayout) {
        relToday.setBackgroundResource(R.color.white);
        rel3Day.setBackgroundResource(R.color.white);
        rel1Month.setBackgroundResource(R.color.white);
        relByDate.setBackgroundResource(R.color.white);
        relativeLayout.setBackgroundResource(R.color.LighterBlue);
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
        setUnSelectedFilterByTimeCard();
        if (linearShowFilterDate.getVisibility() == View.VISIBLE) {
            linearShowFilterDate.setVisibility(View.GONE);
            setDefaultText();

        }

    }

    public void deliveredCard(View view) {
        filterId = "5";
        onFilterClicked("5");
        setDefaultColor(deliveredBack);
        setUnSelectedFilterByTimeCard();
        if (linearShowFilterDate.getVisibility() == View.VISIBLE) {
            linearShowFilterDate.setVisibility(View.GONE);
            setDefaultText();

        }

    }

    public void readyCard(View view) {
        filterId = "3";
        onFilterClicked("3");
        setDefaultColor(readyBack);
        setUnSelectedFilterByTimeCard();
        if (linearShowFilterDate.getVisibility() == View.VISIBLE) {
            linearShowFilterDate.setVisibility(View.GONE);
            setDefaultText();
        }
    }

    public void sendingCard(View view) {
        filterId = "4";
        onFilterClicked("4");
        setDefaultColor(sendingBack);
        setUnSelectedFilterByTimeCard();
        if (linearShowFilterDate.getVisibility() == View.VISIBLE) {
            linearShowFilterDate.setVisibility(View.GONE);
            setDefaultText();

        }

    }

    public void preparationCard(View view) {
        filterId = "2";
        onFilterClicked("2");
        setDefaultColor(preparingBack);
        setUnSelectedFilterByTimeCard();
        if (linearShowFilterDate.getVisibility() == View.VISIBLE) {
            linearShowFilterDate.setVisibility(View.GONE);
            setDefaultText();

        }

    }

    public void checkCard(View view) {
        filterId = "1";
        onFilterClicked("1");
        setDefaultColor(checkBack);
        setUnSelectedFilterByTimeCard();
        if (linearShowFilterDate.getVisibility() == View.VISIBLE) {
            linearShowFilterDate.setVisibility(View.GONE);
            setDefaultText();

        }

    }

    public void allOrderCard(View view) {
        if (swShowReport.isChecked()) {
            List<SendOrderClass> currentOrders = new ArrayList<>();
            for (SendOrderClass currentOrder : orderList) {
                if (currentOrder.getCompanyID().equals(BaseCodeClass.CompanyID)) {
                    currentOrders.add(currentOrder);
                }
            }
            initOrderRecyclerView(currentOrders);
        } else {
            initOrderRecyclerView(orderList);
        }

        setDefaultColor(allOrderBack);
        setUnSelectedFilterByTimeCard();
        if (linearShowFilterDate.getVisibility() == View.VISIBLE) {
            linearShowFilterDate.setVisibility(View.GONE);
            setDefaultText();

        }


    }

    public void filterOrder(String status) {
        try {
            if (!receiveData) {
                return;
            }
            filterList.clear();
            if (swShowReport.isChecked()) {
                for (SendOrderClass soc : orderList) {
                    Log.d(TAG, "filterOrder: " + soc.toString());
                    if (soc.getStatusID().equals(status) && soc.getCompanyID().equals(BaseCodeClass.CompanyID)) {
                        filterList.add(soc);

                    }
                }
            } else {
                /**
                 * Here we should change status value
                 * We should check status value
                 */

                for (SendOrderClass soc : orderList) {
                    Log.d(TAG, "filterOrder: " + soc.toString());
                    if (soc.getStatusID().equals(status)) {
                        filterList.add(soc);

                    }
                }
            }
            filterId = "";

            initOrderRecyclerView(filterList);
        } catch (Exception e) {
            logMessage("MyStoreReport 400 >> " + e.getMessage(), mContext);
        }
    }

    public void filterByDateOrder(Date from, Date to) {
        try {
            if (!receiveData) {
                return;
            }
            List<SendOrderClass> list = new ArrayList<>();


            for (SendOrderClass sendOrderClass : filterList) {

                Date orderDate = TimeUtils.convertStrToDate(sendOrderClass.getOrderDate());
                if (orderDate.after(from) && orderDate.before(to) && !swShowReport.isChecked()) {
                    list.add(sendOrderClass);
                } else if (orderDate.after(from) && orderDate.before(to) && swShowReport.isChecked()) {
                    if (sendOrderClass.getCompanyID().equals(BaseCodeClass.CompanyID))
                        list.add(sendOrderClass);
                }

            }

            //adapter.updateData(list);
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
            statusID = "-1";

            filterList.clear();
            for (SendOrderClass soc : orderList) {
                if (soc.getCompanyID().equals(BaseCodeClass.CompanyID)) {

                    if ((soc.getStatusID().equals("5") || soc.getStatusID().equals("6") || soc.getStatusID().equals("7")
                            || soc.getStatusID().equals("8") || soc.getStatusID().equals("9")) &&
                            !swShowReport.isChecked()) {
                        filterList.add(soc);
                    }
                }
            }

            initOrderRecyclerView(filterList);
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
                }, 1000);
            } else {
                Handler aHandler = new Handler();
                aHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getOnlineInformationClass.readCompanyOrder(baseCodeClass.getCompanyID());
                        Log.d(TAG, "run: company order" + baseCodeClass.getUserID());

                    }
                }, 1000);
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

    private void chooseStartDate(TextView textView) {
        PersianDatePickerDialog picker = new PersianDatePickerDialog(mContext)
                .setPositiveButtonString("باشه")
                .setNegativeButton("بیخیال")
                .setTodayButton("امروز")
                .setTodayButtonVisible(true)
                .setMinYear(1300)
                .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
                .setActionTextColor(Color.GRAY)
                .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                .setShowInBottomSheet(true)
                .setListener(new Listener() {
                    @Override
                    public void onDateSelected(PersianCalendar persianCalendar) {
                        Log.d(TAG, "onDateSelected: " + persianCalendar.getGregorianChange());//Fri Oct 15 03:25:44 GMT+04:30 1582
                        Log.d(TAG, "onDateSelected: " + persianCalendar.getTimeInMillis());//1583253636577
                        Log.d(TAG, "onDateSelected: " + persianCalendar.getTime());//Tue Mar 03 20:10:36 GMT+03:30 2020
                        Log.d(TAG, "onDateSelected: " + persianCalendar.getDelimiter());//  /
                        Log.d(TAG, "onDateSelected: " + persianCalendar.getPersianLongDate());// سه‌شنبه  13  اسفند  1398
                        Log.d(TAG, "onDateSelected: " + persianCalendar.getPersianLongDateAndTime()); //سه‌شنبه  13  اسفند  1398 ساعت 20:10:36
                        Log.d(TAG, "onDateSelected: " + persianCalendar.getPersianMonthName()); //اسفند
                        Log.d(TAG, "onDateSelected: " + persianCalendar.isPersianLeapYear());//false
//                                bornDate = persianCalendar.getPersianYear(+"/" + persianCalendar.getPersianMonth() + "/" + persianCalendar.getPersianDay();
                        Date date = new Date(persianCalendar.getTimeInMillis());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                        /*bornDate = sdf.format(date);*/

                        textView.setText(persianCalendar.getPersianLongDate());
                        start = date;
                        if (end != null) {
                            filterByDateOrder(start, end);
                        }


                    }

                    @Override
                    public void onDismissed() {

                    }
                });

        picker.show();
    }

    private void chooseEndDate(TextView textView) {
        PersianDatePickerDialog picker = new PersianDatePickerDialog(mContext)
                .setPositiveButtonString("باشه")
                .setNegativeButton("بیخیال")
                .setTodayButton("امروز")
                .setTodayButtonVisible(true)
                .setMinYear(1300)
                .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
                .setActionTextColor(Color.GRAY)
                .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                .setShowInBottomSheet(true)
                .setListener(new Listener() {
                    @Override
                    public void onDateSelected(PersianCalendar persianCalendar) {
                        Log.d(TAG, "onDateSelected: " + persianCalendar.getGregorianChange());//Fri Oct 15 03:25:44 GMT+04:30 1582
                        Log.d(TAG, "onDateSelected: " + persianCalendar.getTimeInMillis());//1583253636577
                        Log.d(TAG, "onDateSelected: " + persianCalendar.getTime());//Tue Mar 03 20:10:36 GMT+03:30 2020
                        Log.d(TAG, "onDateSelected: " + persianCalendar.getDelimiter());//  /
                        Log.d(TAG, "onDateSelected: " + persianCalendar.getPersianLongDate());// سه‌شنبه  13  اسفند  1398
                        Log.d(TAG, "onDateSelected: " + persianCalendar.getPersianLongDateAndTime()); //سه‌شنبه  13  اسفند  1398 ساعت 20:10:36
                        Log.d(TAG, "onDateSelected: " + persianCalendar.getPersianMonthName()); //اسفند
                        Log.d(TAG, "onDateSelected: " + persianCalendar.isPersianLeapYear());//false
//                                bornDate = persianCalendar.getPersianYear(+"/" + persianCalendar.getPersianMonth() + "/" + persianCalendar.getPersianDay();
                        Date date = new Date(persianCalendar.getTimeInMillis());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                        /*bornDate = sdf.format(date);*/

                        textView.setText(persianCalendar.getPersianLongDate());
                        end = date;
                        if (start != null) {
                            filterByDateOrder(start, end);
                        }
                    }

                    @Override
                    public void onDismissed() {

                    }
                });

        picker.show();
    }


}
