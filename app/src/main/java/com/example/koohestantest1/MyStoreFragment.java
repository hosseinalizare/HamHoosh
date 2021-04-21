package com.example.koohestantest1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.koohestantest1.activity.CompanySettingActivity;
import com.example.koohestantest1.activity.CostumersListActivity;
import com.example.koohestantest1.activity.InvisibleProductActivity;
import com.example.koohestantest1.activity.NotInStockActivity;
import com.example.koohestantest1.model.CountsDetail;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.example.koohestantest1.viewModel.CountsViewModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import com.example.koohestantest1.ApiDirectory.LoadProductApi;
import com.example.koohestantest1.ApiDirectory.MessageApi;
import com.example.koohestantest1.DB.DataBase;
import com.example.koohestantest1.InfoDirectory.GetOnlineInformationClass;
import com.example.koohestantest1.InfoDirectory.MessageManagerClass;
import com.example.koohestantest1.ViewModels.ContactListViewModel;
import com.example.koohestantest1.ViewModels.SendMessageViewModel;
import com.example.koohestantest1.ViewModels.SendReportViewModel;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.MyStoreProductRecyclerViewAdapter;
import retrofit2.Call;
import retrofit2.Retrofit;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.companyProfile;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.productDataList;

public class MyStoreFragment extends Fragment implements MessageApi, ViewTreeObserver.OnScrollChangedListener {

    private Context mContext;


    BaseCodeClass baseCodeClass;
    DataBase dataBase;

    LoadProductApi loadProductApi;
    RecyclerView gridRecyclerView;
    Button btnAddProduct, btnSendRequest, btnSeeOrder, btnReports;
    EditText message;
    ViewFlipper vf;
    View view;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton drawerMenuBtn;
    ImageView ivMessageIcon;
    FrameLayout frameLayout;

    ScrollView scrollView;

    MyStoreProductRecyclerViewAdapter adapter;
    MessageManagerClass messageManagerClass;

    TextView storeName, storeBio, mobileNO, NoOfCustomer, NoOfSell, NoOfProduct;

    private CountsViewModel countsViewModel;
    BadgeDrawable badgeDrawable;
    private ConstraintLayout clCostumers;
    private TextView tvMenuInReview, tvMenuInProgress, tvMenuInSend, tvMenuReady, tvMenuDelivered,
            tvMenuCancelled, tvMenuNotInStock, tvMenuInvisible,tvMenuInVisibleComments;

    private ImageView ivCompanyProfile;

    private String TAG = MyStoreFragment.class.getSimpleName() + "Debug";

    public MyStoreFragment() {
        baseCodeClass = new BaseCodeClass();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        countsViewModel = new ViewModelProvider(this).get(CountsViewModel.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_store, container, false);


        mContext = getActivity();
        messageManagerClass = new MessageManagerClass(mContext, this);

        gridRecyclerView = (RecyclerView) view.findViewById(R.id.productGridRecyclerView);

        vf = (ViewFlipper) view.findViewById(R.id.MyStoreVf);
        btnAddProduct = (Button) view.findViewById(R.id.btnAddProducts);
        btnSendRequest = view.findViewById(R.id.sendRequestBtn);
        btnSeeOrder = view.findViewById(R.id.btnOrder);
        btnReports = view.findViewById(R.id.btnReports);
        ivMessageIcon = view.findViewById(R.id.iv_message_icon);
        frameLayout = view.findViewById(R.id.frame_chat);
        clCostumers = view.findViewById(R.id.cl_customers);

        storeName = view.findViewById(R.id.StoreName);
        storeBio = view.findViewById(R.id.StoreBio);
        NoOfCustomer = view.findViewById(R.id.NoOfCustomer);
        NoOfSell = view.findViewById(R.id.NoOfSell);
        NoOfProduct = view.findViewById(R.id.NoOfProduct);
        message = view.findViewById(R.id.edMessage);
        mobileNO = view.findViewById(R.id.txtMobile);

        ivCompanyProfile = view.findViewById(R.id.imageViewCompany);

        drawerLayout = view.findViewById(R.id.myStore_drawer_layout);
        navigationView = view.findViewById(R.id.myStoreNavView);
        drawerMenuBtn = view.findViewById(R.id.drawer_menu_btn);

        if (companyProfile != null) {
            String companyName = companyProfile.getCompanyName();
            if (companyName != null)
                storeName.setText(companyName);
        }

        dataBase = new DataBase(mContext);
        baseCodeClass.LoadBaseData(mContext);

        Log.d(TAG, "onCreateView: UserId " + baseCodeClass.getUserID());
        if (baseCodeClass.getEmployeeID(baseCodeClass.getUserID()).equals("false")) {
            //regular user
            vf.setDisplayedChild(0);

            btnSendRequest.setOnClickListener(v -> {
                try {
                    messageManagerClass.sendReport(new SendReportViewModel(baseCodeClass.getUserID(), baseCodeClass.getToken(), "8",
                            message.getText().toString(), "1", baseCodeClass.getUserID(), "درخواست ثبت فروشگاه", "",
                            "", mobileNO.getText().toString(), "", "1"));
                    vf.setDisplayedChild(2);
                } catch (Exception e) {
                    logMessage("MyStoreFragment 300 >> " + e.getMessage(), mContext);
                }
            });

        } else {
            // employee
            vf.setDisplayedChild(1);

            try {
                if (baseCodeClass.getPermissions() != null) {
                    if (baseCodeClass.getPermissions().size() > 15) {
                        if (!baseCodeClass.getPermissions().get(5).isState())
                            btnAddProduct.setVisibility(View.INVISIBLE);
                        if (!baseCodeClass.getPermissions().get(11).isState())
                            ivMessageIcon.setVisibility(View.INVISIBLE);
                        if (!baseCodeClass.getPermissions().get(2).isState())
                            btnReports.setVisibility(View.INVISIBLE);
                        if (!baseCodeClass.getPermissions().get(3).isState())
                            btnSeeOrder.setVisibility(View.INVISIBLE);
                    }
                }
            } catch (Exception e) {
                logMessage("MyStoreFragment permission >> " + e.getMessage(), mContext);
            }


            badgeDrawable = BadgeDrawable.create(requireContext());
            badgeDrawable.setBadgeGravity(BadgeDrawable.TOP_END);
            badgeDrawable.setVerticalOffset(28);
            badgeDrawable.setHorizontalOffset(28);

            frameLayout.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {

                //either of the following two lines of code  work
                //badgeDrawable.updateBadgeCoordinates(imageView, frameLayout);
                BadgeUtils.attachBadgeDrawable(badgeDrawable, ivMessageIcon, frameLayout);
            });

            ivMessageIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new  Intent(mContext,ListMessageActivity.class);
                    mIntent.putExtra("id",baseCodeClass.getCompanyID());
                    startActivity(mIntent);

                }
            });



            btnReports.setOnClickListener(v -> startActivity(new Intent(mContext, MyStoreReportActivity.class)));


            String bio = companyProfile.getBio() == null ? "" : companyProfile.getBio();
            String neighborHood = companyProfile.getNeighborhood() == null ? "" : companyProfile.getNeighborhood();
            String address = companyProfile.getAddress() == null ? "" : "،" + companyProfile.getAddress();
            storeBio.setText(bio + "\n" + neighborHood + address);
            NoOfCustomer.setText(companyProfile.getFollowingCount());
            NoOfSell.setText(companyProfile.getOrderCount());
            NoOfProduct.setText(companyProfile.getProductCount());


            Menu menu = navigationView.getMenu();
            setUpMenuCounters(menu);

            drawerMenuBtn.setOnClickListener(v -> {
                try {
                    YoYo.with(Techniques.RubberBand).playOn(drawerMenuBtn);
                    drawerLayout.openDrawer(GravityCompat.END);
                } catch (Exception e) {
                    logMessage("MyStoreFragment 100 >> " + e.getMessage(), mContext);
                }
            });

            clCostumers.setOnClickListener(v -> {
                if (baseCodeClass.getPermissions().get(10).isState()) {
                    Intent intent = new Intent(requireContext(), CostumersListActivity.class);
                    startActivity(intent);
                }
            });
            btnSeeOrder.setOnClickListener(v -> startActivity(new Intent(mContext, MyStoreOrderListActivity.class)));
            navigationView.setNavigationItemSelectedListener(item -> {
                try {
                    int report = 0;

                    switch (item.getItemId()) {
                        case R.id.editAddresStore:
                            try {
                                if (baseCodeClass.getPermissions().get(13).isState()) {
                                    Intent intent = new Intent(mContext, EditStoreProfileActivity.class);
                                    intent.putExtra("mode", BaseCodeClass.CompanyEnum.AddresMenu.toString());
                                    startActivity(intent);
                                }
                            } catch (Exception e) {
                                Log.d("Error",e.getMessage());
                            }
                            break;

                        case R.id.editCallStore:
                            try {
                                if (baseCodeClass.getPermissions().get(13).isState()) {
                                    Intent intent = new Intent(mContext, EditStoreProfileActivity.class);
                                    intent.putExtra("mode", BaseCodeClass.CompanyEnum.CallInfo.toString());
                                    startActivity(intent);
                                }
                            } catch (Exception e) {
                                Log.d("Error",e.getMessage());
                            }
                            break;
                        case R.id.editmonyStore:
                            try {
                                if (baseCodeClass.getPermissions().get(13).isState()) {
                                    Intent intent = new Intent(mContext, EditStoreProfileActivity.class);
                                    intent.putExtra("mode", BaseCodeClass.CompanyEnum.money.toString());
                                    startActivity(intent);
                                }
                            } catch (Exception e) {
                                toastMessage("iufhiuahswfiasuhgw");
                            }
                            break;
                        case R.id.editStore:
                            try {
                                if (baseCodeClass.getPermissions().get(13).isState()) {
                                    Intent intent = new Intent(mContext, EditStoreProfileActivity.class);
                                    intent.putExtra("mode", BaseCodeClass.CompanyEnum.SettingMenu.toString());
                                    startActivity(intent);
                                }
                            } catch (Exception e) {
                                Log.d("Error",e.getMessage());
                            }
                            break;
                        case R.id.employeeStore:
                            if (baseCodeClass.getPermissions().get(1).isState()) {
                                new GetOnlineInformationClass(this.getContext()).companyEmployee(baseCodeClass.getCompanyID());
                                //getOnlineInformationClass.loadCompanyFollower(baseCodeClass.getCompanyID());
                                startActivity(new Intent(mContext, ManageEmployeeActivity.class));
                            }
                            break;
                        case R.id.checking:
                            if (baseCodeClass.getPermissions().get(3).isState())
                                report = 1;
                            break;
                        case R.id.preparing:
                            if (baseCodeClass.getPermissions().get(2).isState())
                                report = 2;
                            break;
                        case R.id.sending:
                            if (baseCodeClass.getPermissions().get(2).isState())
                                report = 4;
                            break;
                        case R.id.ready:
                            if (baseCodeClass.getPermissions().get(2).isState())
                                report = 3;
                            break;
                        case R.id.delivered:
                            if (baseCodeClass.getPermissions().get(2).isState())
                                report = 5;
                            break;
                        case R.id.canceled:
                            if (baseCodeClass.getPermissions().get(2).isState())
                                report = 6;
                            break;
                        case R.id.newOrder:
                            if (baseCodeClass.getPermissions().get(3).isState())
                                startActivity(new Intent(mContext, MyStoreOrderListActivity.class));
                            drawerLayout.closeDrawer(GravityCompat.END);
                            break;
                        case R.id.menu_nav_customers_list:
                            if (baseCodeClass.getPermissions().get(10).isState()) {
                                Intent intent = new Intent(requireContext(), CostumersListActivity.class);
                                startActivity(intent);
                            }
                            break;
                        case R.id.menu_nav_Archived_Product:
                            Intent intentInvisibleProduct = new Intent(requireContext(), InvisibleProductActivity.class);
                            startActivity(intentInvisibleProduct);
                            break;
                       /* case R.id.menu_nav_Archived_comments:
                            Intent intentInvisibleComments = new Intent(requireContext(), InvisibleCommentsActivity.class);
                            startActivity(intentInvisibleComments);
                            break;*/

                        case R.id.menu_nav_nonExist_Product:
                            Intent notInStock = new Intent(requireContext(), NotInStockActivity.class);
                            startActivity(notInStock);
                            break;
                        case R.id.SettingStore:
                            Intent companySettingIntent = new Intent(requireContext(), CompanySettingActivity.class);
                            startActivity(companySettingIntent);
                            break;
                    }
                    if (report != 0) {
                        Intent intent = new Intent(mContext, MyStoreReportActivity.class);
                        drawerLayout.closeDrawer(GravityCompat.END);
                        intent.putExtra("status", String.valueOf(report));
                        startActivity(intent);
                    }
                    return true;
                } catch (Exception e) {
                    logMessage("MyStoreFragment 200 >> " + e.getMessage(), mContext);
                    return false;
                }
            });


            btnAddProduct.setOnClickListener(v -> {
                Intent i = new Intent(mContext, AddProductActivity.class);
                i.putExtra("PID", "");
                startActivity(i);
            });


            final Retrofit retrofit = RetrofitInstance.getRetrofit();

            loadProductApi = retrofit.create(LoadProductApi.class);


            adapter = new MyStoreProductRecyclerViewAdapter(mContext, productDataList, true);
            initRecyclerView();
            countsViewModel.getCount().observe(getViewLifecycleOwner(), count -> {

                CountsDetail countsDetailCompany = count.getCompanyDetails();
                int newMsgCount = countsDetailCompany.getNewMsg();
                if (newMsgCount > 0) {
                    badgeDrawable.setVisible(true);
                    badgeDrawable.setNumber(newMsgCount);
                }

                Log.d(TAG, "onViewCreated: " + countsDetailCompany.toString());

                int cancelledItem = countsDetailCompany.getCanceled();
                if (cancelledItem > 0) {
                    tvMenuCancelled.setText(String.valueOf(cancelledItem));
                } else {
                    tvMenuCancelled.setVisibility(View.GONE);
                }

                int deliveredItem = countsDetailCompany.getDelivered();
                if (deliveredItem > 0) {
                    tvMenuDelivered.setText(String.valueOf(deliveredItem));
                } else {
                    tvMenuDelivered.setVisibility(View.GONE);
                }


                int readyItem = countsDetailCompany.getReadyDeliver();
                if (readyItem > 0) {
                    tvMenuReady.setText(String.valueOf(readyItem));
                } else {
                    tvMenuReady.setVisibility(View.GONE);
                }

                int inSendItem = countsDetailCompany.getSending();
                if (inSendItem > 0) {
                    tvMenuInSend.setText(String.valueOf(inSendItem));
                } else {
                    tvMenuInSend.setVisibility(View.GONE);
                }

                int inProgressItem = countsDetailCompany.getProcessOrder();
                if (inProgressItem > 0) {
                    tvMenuInProgress.setText(String.valueOf(inProgressItem));
                } else {
                    tvMenuInProgress.setVisibility(View.GONE);
                }

                int inReviewItem = countsDetailCompany.getNewOrder();
                if (inReviewItem > 0) {
                    tvMenuInReview.setText(String.valueOf(inReviewItem));

                } else {
                    tvMenuInReview.setVisibility(View.GONE);
                }

                int notInStockItem = countsDetailCompany.getNotInStockProducts();
                if (notInStockItem > 0) {
                    tvMenuNotInStock.setText(String.valueOf(notInStockItem));
                } else {
                    tvMenuNotInStock.setVisibility(View.GONE);
                }

                int inVisibleItem = countsDetailCompany.getInvisibleProducts();
                if (inVisibleItem > 0) {
                    tvMenuInvisible.setText(String.valueOf(inVisibleItem));
                } else {
                    tvMenuInvisible.setVisibility(View.GONE);
                }

               /* int inVisibleComment =10;
                tvMenuInVisibleComments.setText(String.valueOf(inVisibleComment));*/


            });
        }


        return view;

    }

    private void setUpMenuCounters(Menu menu) {
        MenuItem itemCheck = menu.findItem(R.id.checking);
        tvMenuInReview = (TextView) itemCheck.getActionView().findViewById(R.id.tv_menu_badge);

        MenuItem itemPreparing = menu.findItem(R.id.preparing);
        tvMenuInProgress = (TextView) itemPreparing.getActionView().findViewById(R.id.tv_menu_badge);

        MenuItem itemSending = menu.findItem(R.id.sending);
        tvMenuInSend = (TextView) itemSending.getActionView().findViewById(R.id.tv_menu_badge);


        MenuItem itemReady = menu.findItem(R.id.ready);
        tvMenuReady = (TextView) itemReady.getActionView().findViewById(R.id.tv_menu_badge);

        MenuItem itemDelivered = menu.findItem(R.id.delivered);
        tvMenuDelivered = (TextView) itemDelivered.getActionView().findViewById(R.id.tv_menu_badge);

        MenuItem itemCanceled = menu.findItem(R.id.canceled);
        tvMenuCancelled = (TextView) itemCanceled.getActionView().findViewById(R.id.tv_menu_badge);

        MenuItem itemInvisibleProducts = menu.findItem(R.id.menu_nav_Archived_Product);
        tvMenuInvisible = (TextView) itemInvisibleProducts.getActionView().findViewById(R.id.tv_menu_badge);

        /*MenuItem itemInvisibleComments = menu.findItem(R.id.menu_nav_Archived_comments);
        tvMenuInVisibleComments = (TextView) itemInvisibleComments.getActionView().findViewById(R.id.tv_menu_badge);*/

        MenuItem itemNotInStock = menu.findItem(R.id.menu_nav_nonExist_Product);
        tvMenuNotInStock = (TextView) itemNotInStock.getActionView().findViewById(R.id.tv_menu_badge);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void onResume() {
        super.onResume();
        //call for messages count
        countsViewModel.callForCounts(baseCodeClass.getUserID(), baseCodeClass.getToken(), baseCodeClass.getCompanyID());

        loadCompanyImage();
    }

    private void loadCompanyImage() {
        String url = baseCodeClass.BASE_URL + "Company/DownloadFile?CompanyID=" + baseCodeClass.getCompanyID() + "&ImageAddress=" + 1;
        Glide.with(this).load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(ivCompanyProfile);
    }


    public void initRecyclerView() {
        try {
            GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false);
            gridRecyclerView.setLayoutManager(layoutManager);
//            gridRecyclerView.setNestedScrollingEnabled(false);
            gridRecyclerView.setFocusable(false);
            gridRecyclerView.setAdapter(adapter);
        } catch (Exception e) {
            logMessage("MyStoreFragment 400 >> " + e.getMessage(), mContext);
        }
    }

    public void toastMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public Call<GetResualt> sendMessage(SendMessageViewModel sendMessage) {
        return null;
    }

    @Override
    public void onResponseSendMessage(GetResualt getResualt) {

    }

    @Override
    public Call<GetResualt> sendReport(SendReportViewModel sendReportViewModel) {
        return null;
    }

    @Override
    public void onResponseSendReport(GetResualt getResualt) {

    }

    @Override
    public Call<List<SendMessageViewModel>> getMessage(SendMessageViewModel sendMessageViewModel) {
        return null;
    }

    @Override
    public void onResponseGetMessage(List<SendMessageViewModel> sendMessageViewModels) {

    }

    @Override
    public Call<List<ContactListViewModel>> getContact(String token, String userID, String objectID) {
        return null;
    }

    @Override
    public void onResponseGetContact(List<ContactListViewModel> contactListViewModels) {

    }

    @Override
    public Call<List<ContactListViewModel>> getContactV2(String token, String userID, String objectID) {
        return null;
    }

    @Override
    public void onResponseGetContactV2(List<ContactListViewModel> contactListViewModels) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        drawerLayout.closeDrawer(GravityCompat.END);
    }

    @Override
    public void onScrollChanged() {
//        try {
//            View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
//            int top_detector = scrollView.getScrollY();
//            int bottom_detector = view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY());
//
////        if (bottomNavClick == 0){
////            return;
////        }
//
//            if (top_detector <= 0) {
//                //baseCodeClass.logMessage("top", mContext);
//            }
//
//            if (bottom_detector <= 0) {
//                adapter.loadMore();
//                Toast.makeText(mContext, "bottom", Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception e) {
//            baseCodeClass.logMessage("onScrollchanged : " + e.getMessage(), mContext);
//        }

    }
}
