package com.example.koohestantest1;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koohestantest1.Utils.SharedPreferenceUtils;
import com.example.koohestantest1.Utils.StringUtils;
import com.example.koohestantest1.adapter.recyclerinterface.ICartEvents;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.example.koohestantest1.viewModel.BadgeSharedViewModel;
import com.example.koohestantest1.viewModel.LocalCartViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.koohestantest1.ApiDirectory.AddressApi;
import com.example.koohestantest1.ApiDirectory.CartApi;
import com.example.koohestantest1.ApiDirectory.JsonApi;
import com.example.koohestantest1.DB.DataBase;
import com.example.koohestantest1.DB.MyDataBase;
import com.example.koohestantest1.InfoDirectory.MangeAddressClass;
import com.example.koohestantest1.classDirectory.AddressViewModel;
import com.example.koohestantest1.classDirectory.AllProductData;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.CartProductRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.HowToPay;
import com.example.koohestantest1.classDirectory.PayType;
import com.example.koohestantest1.classDirectory.PaymentTypeRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.SendOrderClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.CompanyID;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.PageShow;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.allAddress;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.productDataList;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.selectedAddress;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.sendOrderClass;

public class CartFragment extends Fragment implements AddressApi, ICartEvents {
    private SharedPreferenceUtils sharedPreferenceUtils;
    private String TAG = CartFragment.class.getSimpleName() + "Debug";
    private Context mContext;
    private DataBase dataBase;
    private Cursor data;
    BaseCodeClass baseCodeClass;
    CartApi cartApi;
    double sum = 0;
    ViewFlipper vf;
    RecyclerView cartListRecyclerView;
    Button btnAddCart, btnCopy;
    MyDataBase myDataBase;
    RelativeLayout cashLayout;
    RadioGroup radioGroup;
    CardView company;
    private AlertDialog alertDialogAddAddress;
    int receiveChecked = 1;

    View view;

    CartProductRecyclerViewAdapter adapter;
    CardView addressCard;

    TextView cartSumPrice, cartSumDiscount, cartSumP,deliveryPrice;

    Date currentTime;

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    TextView addressInCart, bankAccount;
    private EditText note, paygir;

    int paymentType = 0;
    private String[] ss = new String[3];
    private AddressDialogFragment addressDialogFragment;
    private BadgeSharedViewModel badgeSharedViewModel;
    private LocalCartViewModel localCartViewModel;

    private Retrofit retrofit;
    private JsonApi jsonApi;

    public CartFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        badgeSharedViewModel = new ViewModelProvider(requireActivity()).get(BadgeSharedViewModel.class);
        localCartViewModel = new ViewModelProvider(requireActivity()).get(LocalCartViewModel.class);
        sharedPreferenceUtils = new SharedPreferenceUtils(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart, container, false);

        Log.d(TAG, "onCreateView: ");
        baseCodeClass = new BaseCodeClass();

        mContext = getActivity();
        vf = view.findViewById(R.id.cartViewFlipper);
        cartListRecyclerView = view.findViewById(R.id.cartProductList);
        btnAddCart = view.findViewById(R.id.btnAddCart);
        addressCard = view.findViewById(R.id.AddressCard);
        cashLayout = view.findViewById(R.id.cashLayout);
        paygir = view.findViewById(R.id.paygir);
        radioGroup = view.findViewById(R.id.radioGroup);

        /*radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.personalReceive:
                    getImages(2);
                    receiveChecked = 1;
                    break;
                case R.id.sheepReceive:
                    //getImages(1);
                    receiveChecked = 2;
                    break;
            }
        });*/
        getPayInformation(CompanyID);
        company = view.findViewById(R.id.company);

        company.setOnClickListener(v -> startActivity(new Intent(mContext, ShowStoreActivity.class)));

        addressInCart = view.findViewById(R.id.AddressInCart);


        btnCopy = view.findViewById(R.id.btnCopy);

        btnCopy.setOnClickListener(v -> {
            try {
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", BaseCodeClass.companyProfile.getSpare1());
                toastMessage("شماره کارت در کلیپ بورد ذخیره شد");
                clipboard.setPrimaryClip(clip);
            } catch (Exception e) {
                logMessage("CartFragment 100 >> " + e.getMessage(), mContext);
            }
        });
        bankAccount = view.findViewById(R.id.bankAccount);
        if (BaseCodeClass.companyProfile != null)
            bankAccount.setText(BaseCodeClass.companyProfile.getSpare1());

        cartSumPrice = view.findViewById(R.id.cartSumPrice);
        cartSumDiscount = view.findViewById(R.id.cartSumDiscount);
        cartSumP = view.findViewById(R.id.cartSumP);
        deliveryPrice = view.findViewById(R.id.deliveryPrice);
        note = view.findViewById(R.id.EdNotes);

        //if in sp any Note exists, we replace it
        if (sharedPreferenceUtils.getCartNote() != null)
            note.setText(sharedPreferenceUtils.getCartNote());

        PageShow = BaseCodeClass.myAppPage.myProfile;

        getLocationData();

        addressDialogFragment = new AddressDialogFragment(this);

        addressCard.setOnClickListener(v -> {
            try {
                if (allAddress.size() > 0)
                    addressDialogFragment.show(getChildFragmentManager(), "dialogAddress");
                else {
                    Intent intent = new Intent(requireContext(), AddAddressActivity.class);
                    startActivity(intent);
                }
            } catch (Exception e) {
                Log.d("Error",e.getMessage());
            }
        });

        dataBase = new DataBase(mContext);
        myDataBase = new MyDataBase(mContext);
        baseCodeClass.LoadBaseData(mContext);

        final Retrofit retrofit = RetrofitInstance.getRetrofit();

        cartApi = retrofit.create(CartApi.class);


        //handle viewflipper for showing views
        handleViewFlipper();


        btnAddCart.setOnClickListener(v -> {
            generatePaymentButtonSabegh();
        });

        return view;
    }

    private void handleViewFlipper() {
        Log.d(TAG, "handleViewFlipper: ");
        if (isCartEmpty()) {
            vf.setDisplayedChild(0);
        } else {
            vf.setDisplayedChild(1);
            initRecyclerView();
            calcPrice();
            Log.d(TAG, "handleViewFlipper:2 ");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        localCartViewModel.getCartData().observe(getViewLifecycleOwner(), cartWithProducts -> {

            //handles SCENARIO 1: user adds item, comes to this page and comes back to main and remove that item
            if (adapter == null) {
                handleViewFlipper();
            }

            if (cartWithProducts.size() > 0) {
                //keep update main2fragment changes with cart fragment
                if (adapter != null) {
                    //update items in cart
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "onViewCreated: 2");
                    //update prices
                    calcPrice();
                }
            }

            //if all items removed by user, we should reset view
            handleViewFlipper();

        });
    }

    private void calcPrice() {
        try {
            cartSumPrice.setText("قیمت کالاها : " + baseCodeClass.sendOrderClass.getSumPrice() + " تومان");
            cartSumDiscount.setText("جمع تخفیف ها : " + baseCodeClass.sendOrderClass.getSumDisCount() + " تومان");
            cartSumP.setText("مبلغ قابل پرداخت : " + baseCodeClass.sendOrderClass.getSumTotal() + " تومان");


        } catch (Exception e) {
            Log.d("Error",e.getMessage());
        }
    }

    private void getLocationData() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            try {
                if (allAddress.size() > 0) {
                    if (sendOrderClass.getSpare1() != null && !sendOrderClass.getSpare1().equals("")) {
                        Log.d(TAG, "getLocationData: 1 " + sendOrderClass.getSpare1());
                        addressInCart.setText(sendOrderClass.getSpare1());
                    } else {
                        String s = allAddress.get(0).getAddress1();
                        addressInCart.setText(s);
                        selectedAddress = allAddress.get(0);
                        Log.d(TAG, "getLocationData: 2 " + allAddress.get(0));
                    }
                } else {
                    if (sendOrderClass.getSpare1() != null) {
                        addressInCart.setText(sendOrderClass.getSpare1());
                        Log.d(TAG, "getLocationData: 3 " + sendOrderClass.getSpare1());
                    } else {
                        addressInCart.setText(R.string.adding_address_hint);
                    }
                }
            } catch (Exception e) {
                logMessage("CartFragment 200 >> " + e.getMessage(), mContext);
            }
        }, 500);
    }

    @Override
    public void onResume() {
        super.onResume();


        PageShow = BaseCodeClass.myAppPage.cartpage;
        getLocationData();
        Log.d(TAG, "onResume: ");

        CartFragment cartFragment = this;
        if (cartFragment.isVisible())
            callForLatestAddress();
    }

    private void callForLatestAddress() {
        new MangeAddressClass(this).getMyAddress(baseCodeClass.getUserID());

    }

    private void showDialogChooseAddress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle("تعیین آدرس")
                .setMessage("در حال حاضر شما هیچ آدرسی ثبت نکردید، لطفا برای ادامه حداقل یک آدرس ثبت کنید")
                .setCancelable(false)
                .setPositiveButton("باشه", (dialog, which) -> {
                    dialog.dismiss();
                    Intent intent = new Intent(requireContext(), AddAddressActivity.class);
                    startActivity(intent);
                });
        alertDialogAddAddress = builder.create();
        alertDialogAddAddress.show();
    }

    public void initRecyclerView() {
        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            cartListRecyclerView.setLayoutManager(layoutManager);
            adapter = new CartProductRecyclerViewAdapter(mContext, sendOrderClass, this, false, badgeSharedViewModel, localCartViewModel, this::calcPrice);
            cartListRecyclerView.setNestedScrollingEnabled(false);
            cartListRecyclerView.setAdapter(adapter);
            cartListRecyclerView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.d("Error",e.getMessage());
        }
    }



    public void initPaymentRecyclerView(List<PayType> payTypes,int deliveryPrice,TextView txtDeliveryPrice) {
        try {
            if(deliveryPrice == 0)
                txtDeliveryPrice.setText("هزینه حمل و نقل : رایگان");
            else
                txtDeliveryPrice.setText("هزینه حمل و نقل : " + deliveryPrice + " تومان");
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            layoutManager.setReverseLayout(true);
            RecyclerView recyclerView = view.findViewById(R.id.paymentTypeRecyclerView);
            recyclerView.setLayoutManager(layoutManager);
            PaymentTypeRecyclerViewAdapter adapter = new PaymentTypeRecyclerViewAdapter(mContext,payTypes,this);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            logMessage("CartFragment 300 >> " + e.getMessage(), mContext);
        }
    }


    public void toastMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
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

        //check for address existance
        if (addressViewModel != null && addressViewModel.size() == 0 && !isCartEmpty()) {
            //            showDialogChooseAddress();

        } else {
            if (alertDialogAddAddress != null)
                alertDialogAddAddress.dismiss();
            if (addressViewModel != null && addressViewModel.size() > 0)
                selectedAddress = addressViewModel.get(0);
            if (selectedAddress != null)
                addressInCart.setText(selectedAddress.getAddress1());
        }
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

    }

    @Override
    public void AddressOnCliclckListener(AddressViewModel address) {

    }

    @Override
    public void CartRecyclerViewClickListener(View v, boolean b) {
//        toastMessage("click");
        try {

            sendOrderClass.setShipAddress(selectedAddress.getId());
            sendOrderClass.setShipCountryRegion(selectedAddress.getCountry());
            sendOrderClass.setShipStateProvince(selectedAddress.getState());
            sendOrderClass.setShipCity(selectedAddress.getCity());
            sendOrderClass.setShipZIPPostalCode(selectedAddress.getPostalCode());
            sendOrderClass.setSpare1(selectedAddress.getAddress1());

            if (!b) {
                calcPrice();
               /* String s = baseCodeClass.sendOrderClass.getSumPrice();

                if (!s.isEmpty() && s.compareTo("") != 0) {
                    String[] ss = s.split("&");
                    cartSumPrice.setText("قیمت کالاها : " + ss[0] + " تومان");
                    cartSumDiscount.setText("جمع تخفیف ها : " + ss[1] + " تومان");
                    cartSumP.setText("مبلغ قابل پرداخت : " + ss[2] + " تومان");
                }*/
            }

            if (b) {
                String ss = selectedAddress.getAddress1();
//                addressInCart.setText(ss.substring(0, Math.min(ss.length(), 35)) + "...");
                addressInCart.setText(ss);
            }
        } catch (Exception e) {
            Log.d("Error",e.getMessage());
        }
    }

    private boolean isCartEmpty() {
        return sendOrderClass.getOrder_Details().size() == 0;
    }

    private void getPayInformation(String companyId) {
        retrofit = RetrofitInstance.getRetrofit();
        jsonApi = retrofit.create(JsonApi.class);
        Call<List<HowToPay>> call = jsonApi.getPays(companyId);
        call.enqueue(new Callback<List<HowToPay>>() {
            @Override
            public void onResponse(Call<List<HowToPay>> call, Response<List<HowToPay>> response) {
                List<String> imageUrls = new ArrayList<>();
                List<String> payTypeNames = new ArrayList<>();
                List<String> radioButtonNames = new ArrayList<>();
                for(HowToPay hp:response.body()){
                    radioButtonNames.add(hp.getTitle());
                }
                setTextRadio(radioGroup,radioButtonNames);


                radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    RadioButton rb = view.findViewById(checkedId);
                    for(HowToPay hp:response.body()){
                        if(hp.getTitle().equals(rb.getText())){
                            int deliveryCount = hp.getCarierPay();
                            initPaymentRecyclerView(hp.getPayType(),deliveryCount,deliveryPrice);
                            break;
                        }
                    }
                    /*switch (checkedId) {
                        case R.id.personalReceive:
                            payTypeNames.clear();
                            for (PayType type : response.body().get(0).getPayType()) {
                                imageUrls.add(type.getImageAddress());
                                payTypeNames.add(type.getTitle());
                                //payTypes.add(type);

                            }
                            initPaymentRecyclerView(response.body().get(0).getPayType());
                            receiveChecked = 1;
                            break;
                        case R.id.sheepReceive:
                            payTypeNames.clear();
                            for (PayType type : response.body().get(1).getPayType()) {
                                imageUrls.add(type.getImageAddress());
                                payTypeNames.add(type.getTitle());
                                //payTypes.add(type);

                            }
                            getImages(1, imageUrls,payTypeNames);
                            receiveChecked = 2;
                            break;
                        case R.id.fromAloPeykReceive:
                            Toast.makeText(getContext(), "به زودی", Toast.LENGTH_SHORT).show();
                    }*/
                });
            }

            @Override
            public void onFailure(Call<List<HowToPay>> call, Throwable t) {
                Log.d("TAG", t.toString());
            }
        });
        //return payInformation;
    }

    public  void getRadio(RadioGroup Rg,String text) {
        for (int i = 0; i < 10; i++) {
            View o = Rg.getChildAt (i);
            if (o instanceof RadioButton) {
                if (((RadioButton) o).getText ( ).equals (text)) {
                    ((RadioButton) o).setChecked (true);
                    break;
                }
            }
        }
    }

    public  void setTextRadio(RadioGroup Rg,List<String> texts) {
        int j = 0;
        for (int i = 0; i < Rg.getChildCount(); i++) {
            View o = Rg.getChildAt (i);
            if (o instanceof RadioButton) {
                ((RadioButton) o).setText(texts.get(j++));
                o.setVisibility(View.VISIBLE);
                if(j >= texts.size())
                    break;
            }
        }
    }

    @Override
    public void CartPaymentClickListener(int id) {
        paymentType = id;
        if (id == 2) {
            cashLayout.setVisibility(View.VISIBLE);
        } else {
            cashLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onProductRemove() {
        calcPrice();
    }

    @Override
    public void onStop() {
        super.onStop();
        String cartNote = note.getText().toString();
        if (!cartNote.equals(""))
            sharedPreferenceUtils.editCartNote(cartNote);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    private void generatePaymentButtonSabegh() {
        String codePaygir = "";
        try {
            if (allAddress.size() != 0 || selectedAddress != null) {
                if (paymentType != 0) {
                    if (paymentType == 3) {
                        //Toast.makeText(mContext, "dddd", Toast.LENGTH_SHORT).show();
                    } else {

                    }
                    if (paymentType == 2) {
                        if (paygir.getText().length() == 0) {
                            toastMessage("لطفا کد پیگیری وجه واریزی را وارد کنید");
                            return;
                        }
                        codePaygir = "کد پیگیری پرداخت آنلاین :" + paygir.getText().toString();
                    } else {
                        codePaygir = "";
                    }

                    sendOrderClass.setShipAddress(selectedAddress.getId());
                    sendOrderClass.setShipCountryRegion(selectedAddress.getCountry());
                    sendOrderClass.setShipStateProvince(selectedAddress.getState());
                    sendOrderClass.setShipCity(selectedAddress.getCity());
                    sendOrderClass.setShipZIPPostalCode(selectedAddress.getPostalCode());
                    sendOrderClass.setSpare1(selectedAddress.getAddress1());


                    sendOrderClass.setStatusID("1");
                    sendOrderClass.setPaymentType(String.valueOf(paymentType));
                    sendOrderClass.setToken(baseCodeClass.getToken());
                    sendOrderClass.setCompanyID(baseCodeClass.getCompanyID());
                    sendOrderClass.setEmployeeID(baseCodeClass.companyEmployees.get(0).getEmployeeID());//"EmnJnjtsqhU"
                    sendOrderClass.setUserID(baseCodeClass.getUserID());
                    sendOrderClass.setCustomerID(baseCodeClass.getCompanyID() + baseCodeClass.getUserID());

                    sendOrderClass.setNotes(note.getText().toString() + "\n" + codePaygir);
                    sendOrderClass.setShipperID(baseCodeClass.companyProfile.getCompanyID());
                    sendOrderClass.setShipName(baseCodeClass.companyProfile.getCompanyName());
                    sendOrderClass.setShippedDate("2020-08-18T21:54:43.5172323+04:30");
                    sendOrderClass.setShippingFee("0");
                    sendOrderClass.setTaxes("0");
                    sendOrderClass.setTaxRate("0");
                    sendOrderClass.setTaxStatus(String.valueOf(receiveChecked));
                    sendOrderClass.setDeleted("false");
                    sendOrderClass.setPaidDate("2020-08-18T21:54:43.5172323+04:30");
                    sendOrderClass.setUpdateDate("2020-08-18T21:54:43.5172323+04:30");

                    // calcPrice();
                    // if(ss[2]!=null) {
                    //   sendOrderClass.setSumPrice(ss[2]);
                    // }
                    sendOrderClass.calculateSumPrice();
                    sendOrderClass.setSumPrice(String.valueOf((int) StringUtils.getNumberFromStringV2(sendOrderClass.getSumTotal())));


                    Log.d(TAG, ">>>> " + sendOrderClass.toString());
                    Call<GetResualt> call = cartApi.sendOrder(sendOrderClass);
                    call.enqueue(new Callback<GetResualt>() {
                        @Override
                        public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {

                            if (response.body().getResualt().equals("100")) {

                                //we should remove saved notes in shared preference
                                note.setText("");
                                sharedPreferenceUtils.resetCartNote();





                                if (paymentType ==4) {
                                    String res = response.body().getMsg();
                                    long cell = Long.parseLong(BaseCodeClass.userProfile.getMobilePhone());
                                    long amount =(long) StringUtils.getNumberFromStringV2(sendOrderClass.getSumTotal())*10;

                                    String payUrl = "http://hamyarnoandish.ir/Payment/Pay?amount=" + amount + "&cellNumber=" + cell + "&resNum=" + res;

                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(payUrl));
                                    startActivity(intent);

                                }else{
                                    toastMessage("سفارش شما با موفقیت ثبت شد");
                                }

                                sendOrderClass = new SendOrderClass();

                                vf.setDisplayedChild(0);
                                for (AllProductData a : productDataList) {
                                    a.setSelectedToCart(false);
                                }
                                localCartViewModel.deleteAllData();
                            } else {
                                toastMessage(response.body().getMsg());
                                Log.d(TAG, "onResponse: " + response.body().getMsg());
                            }
                        }

                        @Override
                        public void onFailure(Call<GetResualt> call, Throwable t) {

                        }
                    });
                } else {
                    toastMessage("لطفا یکی از روش های پرداخت را انتخاب نمایید.");
                }
            } else {
                showDialogChooseAddress();
            }
        } catch (Exception e) {
            Log.d("Error",e.getMessage());
        }
    }






}
