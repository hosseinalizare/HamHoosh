package com.example.koohestantest1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koohestantest1.Utils.TimeUtils;
import com.example.koohestantest1.classDirectory.CartProductRecyclerViewAdapterVer2;
import com.example.koohestantest1.constants.PaymentTypeConstants;
import com.example.koohestantest1.fragments.bottomsheet.CancelOrdersBottomSheet;
import com.example.koohestantest1.local_db.DBViewModel;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.example.koohestantest1.viewModel.OrderDetailSharedViewModel;

import java.util.List;
import java.util.Map;

import com.example.koohestantest1.ApiDirectory.AddressApi;
import com.example.koohestantest1.ApiDirectory.CartApi;
import com.example.koohestantest1.InfoDirectory.MangeAddressClass;
import com.example.koohestantest1.ViewModels.UpdateOrderClass;
import com.example.koohestantest1.classDirectory.AddressViewModel;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.CartProductRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.GetResualt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.myStoreSelectedOrder;

public class MyStoreOrderDetail extends AppCompatActivity implements AddressApi {

    Button btnAddOrder;
    Button btnCancel;
    BaseCodeClass baseCodeClass;
    Context mContext;
    RecyclerView cartListRecyclerView;
    TextView cartSumPrice, cartSumDiscount, cartSumP, notes, address, time;
    boolean showbutton = true;
    Retrofit retrofit;
    CartApi cartApi;
    boolean cancelOrderPermission = false;
    private TextView tvPaymentType;
    int status;
    private OrderDetailSharedViewModel detailSharedViewModel;

    private Map<Integer, String> mapPayment = PaymentTypeConstants.getPaymentTypes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_store_order_detail);

        detailSharedViewModel = new ViewModelProvider(this).get(OrderDetailSharedViewModel.class);
        tvPaymentType = findViewById(R.id.paymentTypeTextView);
        btnAddOrder = findViewById(R.id.btnAddOrder);
        btnCancel = findViewById(R.id.btnCancelOrder);
        cartListRecyclerView = findViewById(R.id.cartProductList);
        cartSumPrice = findViewById(R.id.cartSumPrice);
        cartSumDiscount = findViewById(R.id.cartSumDiscount);
        cartSumP = findViewById(R.id.cartSumP);
        notes = findViewById(R.id.Notes);
        address = findViewById(R.id.AddressInCart);
        time = findViewById(R.id.sendTime);
        mContext = MyStoreOrderDetail.this;

        baseCodeClass = new BaseCodeClass();

        if (baseCodeClass.getPermissions() != null && baseCodeClass.getPermissions().size() > 0)
            cancelOrderPermission = baseCodeClass.getPermissions().get(BaseCodeClass.EmploeeAccessLevel.CanCancleOrder.getValue()).isState();
        if (!cancelOrderPermission)
            btnCancel.setVisibility(View.GONE);

        if (getIntent().hasExtra("User")) {
            showbutton = false;
            btnAddOrder.setVisibility(View.INVISIBLE);
            btnCancel.setVisibility(View.INVISIBLE);
        }
        retrofit = RetrofitInstance.getRetrofit();

        cartApi = retrofit.create(CartApi.class);

        initRecyclerView();


        cartSumPrice.setText("قیمت کالاها : " + myStoreSelectedOrder.getSumPrice() + " تومان");
        cartSumDiscount.setText("جمع تخفیف ها : " + myStoreSelectedOrder.getSumDisCount() + " تومان");
        cartSumP.setText("مبلغ قابل پرداخت : " + myStoreSelectedOrder.getSumTotal() + " تومان");

        notes.setText(myStoreSelectedOrder.getNotes());

        int inputType = Integer.parseInt(myStoreSelectedOrder.getPaymentType());
        for (Map.Entry<Integer, String> map : mapPayment.entrySet()) {
            if (map.getKey() == inputType)
                tvPaymentType.setText(map.getValue());
        }


        if (myStoreSelectedOrder.getStatusID().equals("1")) {
            btnAddOrder.setText("ارسال به آماده سازی");
            myStoreSelectedOrder.setStatusID("2");
        } else if (myStoreSelectedOrder.getStatusID().equals("2")) {
            if (myStoreSelectedOrder.getPaymentType().equals("1")) {
                btnAddOrder.setText("آماده تحویل حضوری");
            } else {
                btnAddOrder.setText("ارسال به واحد تحویل");
            }
            myStoreSelectedOrder.setStatusID("3");
        } else if (myStoreSelectedOrder.getStatusID().equals("3")) {
            btnAddOrder.setText("تغییر وضعیت به در حال ارسال");
            myStoreSelectedOrder.setStatusID("4");
        } else if (myStoreSelectedOrder.getStatusID().equals("4")) {
            btnAddOrder.setText("تغییر وضعیت به تحویل شده");
            myStoreSelectedOrder.setStatusID("5");
        } else if (myStoreSelectedOrder.getStatusID().equals("5")) {
            btnCancel.setVisibility(View.GONE);
        } else if (myStoreSelectedOrder.getStatusID().equals("6")) {
            btnCancel.setVisibility(View.GONE);
        } else if (myStoreSelectedOrder.getStatusID().equals("7")) {
            btnCancel.setVisibility(View.GONE);
        } else if (myStoreSelectedOrder.getStatusID().equals("8")) {
            btnCancel.setVisibility(View.GONE);
        } else if (myStoreSelectedOrder.getStatusID().equals("9")) {
            btnCancel.setVisibility(View.GONE);
        }
        new MangeAddressClass(this).getAddressByID(myStoreSelectedOrder.getShipAddress());

        String date = myStoreSelectedOrder.getShippedDate();
        StringBuilder stringBuilder = new StringBuilder()
                .append(TimeUtils.getCleanHourAndMinByString(date))
                .append("\n")
                .append(TimeUtils.getPersianCleanDate(date));
        time.setText(stringBuilder.toString());


        detailSharedViewModel.getCanceledStatus().observe(this, integer -> {
            detailSharedViewModel.callForUpdate(new UpdateOrderClass(baseCodeClass.getToken(), baseCodeClass.getUserID(), myStoreSelectedOrder.getOrderID(), String.valueOf(integer)));
        });

        detailSharedViewModel.getCancelOrderRes().observe(this, resualt -> {
            if (resualt.getResualt().equals("100"))
                finish();
            else
                Toast.makeText(this, resualt.getMsg(), Toast.LENGTH_SHORT).show();

        });


    }

    public void btnBakClick(View view) {
        finish();
    }

    public void btnCancelOrder(View view) {
        try {
            CancelOrdersBottomSheet cancelOrdersBottomSheet = new CancelOrdersBottomSheet();
            cancelOrdersBottomSheet.show(getSupportFragmentManager(), "TAG");
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
    }

    public void initRecyclerView() {
        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            cartListRecyclerView.setLayoutManager(layoutManager);
            DBViewModel dbViewModel = new ViewModelProvider(this).get(DBViewModel.class);
            CartProductRecyclerViewAdapterVer2 adapter = new CartProductRecyclerViewAdapterVer2(mContext, myStoreSelectedOrder, this, true);
            cartListRecyclerView.setAdapter(adapter);
            cartListRecyclerView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            baseCodeClass.logMessage(e.getMessage(), mContext);
        }
    }

    @Override
    public Call<List<String>> listState() {
        return null;
    }

    @Override
    public void onResponseListState(List<String> stateList) {

    }

    @Override
    public Call<List<String>> listCity(String state) {
        return null;
    }

    @Override
    public void onResponseListCity(List<String> cityList) {

    }

    @Override
    public Call<GetResualt> setAddress(AddressViewModel addressViewModel) {
        return null;
    }

    @Override
    public void onResponseSetAddress(GetResualt getResualt) {

    }

    @Override
    public Call<List<AddressViewModel>> getAddress(AddressViewModel addressViewModel) {
        return null;
    }

    @Override
    public void onResponseGetMyAddress(List<AddressViewModel> addressViewModel) {

    }

    @Override
    public Call<GetResualt> delAddress(AddressViewModel addressViewModel) {
        return null;
    }

    @Override
    public void onResponseDelAddress(GetResualt getResualt) {

    }

    @Override
    public Call<AddressViewModel> getAddress(String addressID) {
        return null;
    }

    @Override
    public void onResponseGetAddress(AddressViewModel addressViewModel) {
        if (addressViewModel != null)
            address.setText(addressViewModel.getState() + "," + addressViewModel.getCity() + "," + addressViewModel.getAddress1());
    }

    @Override
    public void AddressOnCliclckListener(AddressViewModel address) {

    }

    @Override
    public void CartRecyclerViewClickListener(View v, boolean b) {

    }

    @Override
    public void CartPaymentClickListener(int id) {

    }

    public void btnGreenClick(View view) {

        try {
            UpdateOrderClass updateOrderClass = new UpdateOrderClass(baseCodeClass.getToken(), baseCodeClass.getUserID(),
                    myStoreSelectedOrder.getOrderID(), myStoreSelectedOrder.getStatusID());
            Call<GetResualt> call = cartApi.updateOrder(updateOrderClass);
            call.enqueue(new Callback<GetResualt>() {
                @Override
                public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                    if (response.body().getResualt().equals("100")) {
                        finish();
                    } else {
                        baseCodeClass.logMessage(response.body().getMsg(), mContext);
                    }
                }

                @Override
                public void onFailure(Call<GetResualt> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            logMessage("MyStoreOrderDetail 100 >> " + e.getMessage(), mContext);
        }
    }
}
