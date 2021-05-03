package com.example.koohestantest1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;

//import static com.example.koohestantest1.classDirectory.BaseCodeClass.productClasses;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.CompanyID;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.bulletinProduct;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.categoryRecyclerViewAdapter;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.filterValue;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.particularProduct;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.productDataList;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.productRecyclerViewAdapter;
import static com.example.koohestantest1.Utils.TimeUtils.convertStrToDate;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.koohestantest1.classDirectory.SendProduct;
import com.example.koohestantest1.classDirectory.StandardPrice;
import com.example.koohestantest1.constants.CurrentCartId;
import com.example.koohestantest1.constants.FilterOption;
import com.example.koohestantest1.fragments.transinterface.CartTransitionInterface;
import com.example.koohestantest1.local_db.DBViewModel;
import com.example.koohestantest1.local_db.entity.Product;
import com.example.koohestantest1.local_db.entity.Properties;
import com.example.koohestantest1.model.DeleteProduct;
import com.example.koohestantest1.model.UpdatedProductBody;
import com.example.koohestantest1.model.entity.CartInformation;
import com.example.koohestantest1.model.entity.CartProduct;
import com.example.koohestantest1.model.entity.CartWithProduct;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.example.koohestantest1.viewModel.BadgeSharedViewModel;
import com.example.koohestantest1.viewModel.CountsViewModel;
import com.example.koohestantest1.viewModel.LocalCartViewModel;
import com.example.koohestantest1.viewModel.ProductViewModel;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.koohestantest1.ApiDirectory.LoadProductApi;
import com.example.koohestantest1.DB.DataBase;
import com.example.koohestantest1.DB.MyDataBase;
import com.example.koohestantest1.ViewModels.BookMarkViewModel;
import com.example.koohestantest1.ViewModels.Order_DetailsViewModels;
import com.example.koohestantest1.ViewModels.PostLikeViewModel;
import com.example.koohestantest1.ViewModels.PostViewViewModel;
import com.example.koohestantest1.classDirectory.AllProductData;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.BulletinRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.CategoryRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.CompanyRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.FilterRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.GetPropertisOfCompanyProducts;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.ParticularProductRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.ProductPropertisClass;
import com.example.koohestantest1.classDirectory.ProductRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.SendDeleteProduct;
import com.example.koohestantest1.classDirectory.SendOrderClass;
import com.example.koohestantest1.classDirectory.ReceiveProductClass;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Main2Fragment extends Fragment implements LoadProductApi, ViewTreeObserver.OnScrollChangedListener {

    private BadgeSharedViewModel badgeSharedViewModel;
    private static final int ACTIVITY_NU = 0;
    private final int recyclerSize = 20;
    int i = 0;
    private FilterOption filterOption = FilterOption.RELATED;
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean checkUpdateProduct = false;
    private DataBase dataBase;
    //    Cursor data;
    private BaseCodeClass baseCodeClass = new BaseCodeClass();
    private LoadProductApi loadProductApi;
    private BottomNavigationViewEx bottomNavigationViewEx;
    private RecyclerView bulletinRecyclerView, filterList;
    private Context mContext;
    private Main2Fragment main2Fragment;
    private MyDataBase mydb;
    private CardView cardViewFilter;
    private ImageView companyLogo;
    private ScrollView scrollView;
    private DBViewModel dbViewModel;
    private RelativeLayout mainLayout;
    List<String> allProductsPId= new ArrayList<>();
    Date lastUpdateTime = new Date();
    String updateTime;
    private FrameLayout frameLayout;
    private ImageView ivCartIcon;
    private final String TAG = Main2Fragment.class.getSimpleName();
    private CountsViewModel countsViewModel;
    private LocalCartViewModel localCartViewModel;
    private CartWithProduct cartWithProduct;
    private boolean updateFromDb = true;
    private ProductViewModel productViewModel;


    enum viewMode {allProduct, bulletin}


    viewMode showView = viewMode.allProduct;

    public static final ArrayList<String> mCategory = new ArrayList<String>();


    //filter Value
    public static List<String> filterName = new ArrayList<>();
    public static List<String> filterBrandName = new ArrayList<>();
    public static ArrayList<Integer> filterImage = new ArrayList<>();
    public static ArrayList<Integer> filterBrandImage = new ArrayList<>();

    private Button btnFilter;

    private final ArrayList<String> mCompanyNames = new ArrayList<>();
    private final ArrayList<Integer> mCompanyLogo = new ArrayList<>();

    private RecyclerView productRecyclerView;

    private RecyclerView recyclerView_;

    FilterDialogFragment dialogFragment;
    BrandFilterDialogFragment brandFilterDialogFragment;

    private Button btnSort;
    private FrameLayout frameUserChat;
    private boolean isDataLoaded = false;

    private String keyFilter = "همه";

    public Main2Fragment() {
    }



    @Override
    public Call<GetResualt> sendProductDetail(SendProduct sendProductClass) {
        return null;
    }

    @Override
    public void onResponseSendProduct(GetResualt getResualt) {

    }

    @Override
    public Call<GetResualt> uploadProductImage(String prId, String coId, String uID, String token, MultipartBody.Part file) {
        return null;
    }

    @Override
    public Call<List<ReceiveProductClass>> loadProduct(String companyId) {
        return null;
    }

    @Override
    public void onResponseLoadProduct(List<ReceiveProductClass> receiveProductClasses) {
        productRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public Call<List<ReceiveProductClass>> loadProduct(String companyId, String userID) {
        return null;
    }

    @Override
    public Call<GetResualt> deleteProduct(SendDeleteProduct sendDeleteProduct) {
        return null;
    }

    @Override
    public void onResponseDeleteProduct(GetResualt getResualt) {

    }

    @Override
    public Call<ResponseBody> downloadImage(String PId, String fileNumber) {
        return null;
    }

    @Override
    public void onResponseDownloadImage(ResponseBody responseBody, String pid) {
    }

    @Override
    public Call<List<String>> getCategory(int companyType) {
        return null;
    }

    @Override
    public void onResponseGetCategory(List<String> cat) {

    }

    @Override
    public Call<List<String>> getSubCatOne(int companytype, String mainCat) {
        return null;
    }

    @Override
    public void onResponseGetSubCatOne(List<String> subCat1) {

    }

    @Override
    public Call<List<GetPropertisOfCompanyProducts>> getProperty(int companytype) {
        return null;
    }

    @Override
    public void onResponseGetProperty(List<GetPropertisOfCompanyProducts> propertisOfCompanyProducts) {

    }

    @Override
    public Call<GetResualt> viewProduct(PostViewViewModel postViewViewModel) {
        return null;
    }

    @Override
    public Call<GetResualt> likeProduct(PostLikeViewModel postViewViewModel) {
        return null;
    }

    @Override
    public Call<GetResualt> saveProduct(BookMarkViewModel postViewViewModel) {
        return null;
    }

    @Override
    public Call<GetResualt> editProductDetail(SendProduct receiveProductClass) {
        return null;
    }



    @Override
    public Call<List<ReceiveProductClass>> getUpdatedData(UpdatedProductBody updatedProductBody) {
        return null;
    }

    @Override
    public void onScrollChanged() {
        try {
            View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
            int bottom_detector = view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY());


            if (bottom_detector <= 0) {

                if (layoutManagerProduct != null) {
                    productRecyclerViewAdapter.totalItemCount = layoutManagerProduct.getItemCount();
                    productRecyclerViewAdapter.lastVisibleItem = layoutManagerProduct.findLastVisibleItemPosition();

                    if (!productRecyclerViewAdapter.isLoading &&
                            productRecyclerViewAdapter.totalItemCount >= (productRecyclerViewAdapter.lastVisibleItem)) {
                        if (productRecyclerViewAdapter.loadMoreInterFace != null) {
                            productRecyclerViewAdapter.loadMoreInterFace.onLoadMore();
                        }
                        productRecyclerViewAdapter.isLoading = true;
                    }
                }
            }
        } catch (Exception e) {
            logMessage("onScrollchanged : " + e.getMessage(), mContext);
        }
    }


    @Override
    public void recyclerViewListClicked(View v, String value, boolean notify) {
        try {
            keyFilter = value;
            productRecyclerViewAdapter.getFilter().filter("F"+value);
            filterValue = value;
            if (notify) {
                initFilterRecyclerView();
            }

        } catch (Exception e) {
            logMessage("Main2Fragment 100 >> " + e.getMessage(), mContext);
        }
    }

    @Override
    public void brandRecyclerViewListClicked(View v, String value, boolean notify) {

        keyFilter = value;
        productRecyclerViewAdapter.getFilter().filter("B"+value);
        filterValue = value;
        if (notify) {
            initFilterRecyclerView();
        }
    }

    @Override
    public void recyclerViewCanUpdating() {
        Log.d(TAG, "recyclerViewCanUpdating: ");
        mCategory.clear();
        filterBrandName.clear();
        filterBrandName.add("همه");


        for (AllProductData allProductData :
                productDataList) {
            if (isProductAdded(allProductData.getProductClass().getProductID())) {
                allProductData.setSelectedToCart(true);
            }
            try {
                String category = allProductData.getProductClass().getCategory();
                if (category != null && !category.isEmpty()) {
                    String[] aCategory = category.split("\\.");
                    for (int j = 0; j < aCategory.length; j++) {
                        if (aCategory[j] != null && !aCategory[j].isEmpty() && !aCategory[j].equals("null")) {
                            if (j == 1) {
                                if (!mCategory.contains(aCategory[j])) {
                                    mCategory.add(aCategory[j]);
                                    Log.d(TAG, aCategory[j] + "\n");
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                Log.d("Error", ex.toString());
            }
            try {
                String brand = null;
                for (ProductPropertisClass productPropertisClass :allProductData.getProductClass().getProductPropertis()) {
                    if (productPropertisClass.getPropertisName().equals("برند")){
                        brand = productPropertisClass.getPropertisValue();
                        break;
                    }
                }
                if (brand != null ){
                    if (!filterBrandName.contains(brand)){
                       filterBrandName.add(brand);
                       filterBrandImage.add(GetImag(brand));
                    }
                }
            }catch (Exception e){

            }

        }


        initRecyclerView();
        initBulletinRecyclerView();
        initParticularRecyclerView();
        swipeRefreshLayout.setRefreshing(false);
        manageLists(showView);
        initFilterRecyclerView();
        filterList.setVisibility(View.VISIBLE);


/*
        if (baseCodeClass.getCompanyID().equals("Chrbnihukva")) {

            initFilterRecyclerView();
            filterList.setVisibility(View.VISIBLE);
        }
*/

        swipeRefreshLayout.setRefreshing(false);
    }

    private boolean isProductAdded(String pid) {
        for (Order_DetailsViewModels item :
                BaseCodeClass.sendOrderClass.getOrder_Details()) {
            if (item.getProductID().equals(pid))
                return true;
        }
        return false;
    }

    @Override
    public void imageAdapterCanUpdating(String imagePID) {

    }

    @Override
    public Call<GetResualt> deleteProduct(DeleteProduct deleteProduct) {
        return null;
    }


    private CartTransitionInterface cartTransitionInterface;

    public Main2Fragment(CartTransitionInterface cartTransitionInterface) {
        if (cartTransitionInterface != null)
            this.cartTransitionInterface = cartTransitionInterface;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        badgeSharedViewModel = new ViewModelProvider(requireActivity()).get(BadgeSharedViewModel.class);
        countsViewModel = new ViewModelProvider(this).get(CountsViewModel.class);
        localCartViewModel = new ViewModelProvider(requireActivity()).get(LocalCartViewModel.class);
        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);

    }

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_main2, container, false);
        dbViewModel = new ViewModelProvider(this).get(DBViewModel.class);
        frameLayout = view.findViewById(R.id.frame_cart_icon);
        ivCartIcon = view.findViewById(R.id.iv_cart);
        recyclerView_ = view.findViewById(R.id.productrc);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        bulletinRecyclerView = view.findViewById(R.id.bulletinRecyclerView);
        cardViewFilter = view.findViewById(R.id.cardViewFilter);
        btnFilter = view.findViewById(R.id.btnFilter);
        companyLogo = view.findViewById(R.id.companyLogo);
        filterList = view.findViewById(R.id.filterRecyclerView);
        frameUserChat = view.findViewById(R.id.frame_user_chat);
        btnSort = view.findViewById(R.id.btnSort);
        scrollView = view.findViewById(R.id.scrollViewMain2);
        scrollView.setSmoothScrollingEnabled(false);

        //companyLogo.setImageResource(CompanyId.COMPANY_IMAGE);
        String url = baseCodeClass.pBASE_URL + "Company/DownloadFile?CompanyID=" + baseCodeClass.getCompanyID() + "&ImageAddress=" + 1;
        Glide.with(this).load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(companyLogo);
        mContext = getActivity();
        mydb = new MyDataBase(mContext);
        dataBase = new DataBase(mContext);
        baseCodeClass.LoadBaseData(mContext);
        // baseCodeClass = new BaseCodeClass();

        BadgeDrawable badgeDrawable = BadgeDrawable.create(requireContext());
        badgeDrawable.setBadgeGravity(BadgeDrawable.TOP_END);
        badgeDrawable.setVerticalOffset(25);
        badgeDrawable.setHorizontalOffset(25);

        frameLayout.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {

            //either of the following two lines of code  work
            //badgeDrawable.updateBadgeCoordinates(imageView, frameLayout);
            BadgeUtils.attachBadgeDrawable(badgeDrawable, ivCartIcon, frameLayout);
        });

        BadgeDrawable badgeDrawableUserChat = BadgeDrawable.create(requireContext());
        badgeDrawableUserChat.setBadgeGravity(BadgeDrawable.TOP_END);
        badgeDrawableUserChat.setVerticalOffset(15);
        badgeDrawableUserChat.setHorizontalOffset(20);

        frameUserChat.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {

            //either of the following two lines of code  work
            //badgeDrawable.updateBadgeCoordinates(imageView, frameLayout);
            BadgeUtils.attachBadgeDrawable(badgeDrawableUserChat, companyLogo, frameUserChat);
        });

        companyLogo.setOnClickListener(v -> startActivity(new Intent(mContext, ShowStoreActivity.class)));

        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        productRecyclerView.setNestedScrollingEnabled(false);

        if (baseCodeClass.getCompanyID().equals("Chrbnihukva")) {
            filterList.setVisibility(View.GONE);
        } else {
            filterList.setVisibility(View.VISIBLE);
            initFilterRecyclerView();
        }

//        data = dataBase.getAllData(dataBase.BASE_TABLE);
//        data.moveToFirst();

        final Retrofit retrofit = RetrofitInstance.getRetrofit();

        loadProductApi = retrofit.create(LoadProductApi.class);

        main2Fragment = this;

        //  loadCategory();
        loadCompany();

        scrollView.getViewTreeObserver().addOnScrollChangedListener(this);

        //TODO(1): make some decision about swipe:
        swipeRefreshLayout.setOnRefreshListener(() -> {
            //new ManageProductClass(mContext, main2Fragment, baseCodeClass.getCompanyID()).execute(baseCodeClass.getCompanyID(), BaseCodeClass.DownloadParam);
            updateData(CompanyID);
        });

        try {
            bottomNavigationViewEx = view.findViewById(R.id.productNavigationViewBar);
            bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.list:
                            manageLists(viewMode.allProduct);
                            break;
                        case R.id.grid:
                            manageLists(viewMode.bulletin);
                            break;
                    }
                    return true;
                }
            });
        } catch (Exception e) {
            logMessage("Main2Fragment 200 >> " + e.getMessage(), mContext);
        }

        dialogFragment = new FilterDialogFragment(this);
        brandFilterDialogFragment = new BrandFilterDialogFragment(this);

        btnFilter.setOnClickListener(v -> {
            showPopup(v);
        });

        btnSort.setOnClickListener(v -> {
            showFilteringDialog();
        });

        ivCartIcon.setOnClickListener(v -> {
            cartTransitionInterface.onCartClickListener();
        });
        productDataList.add(null);

        countsViewModel.getCount().observe(getViewLifecycleOwner(), count -> {
            if (count.getUserDetails().getNewMsg() == 0)
                badgeDrawableUserChat.setVisible(false);
            else {
                badgeDrawableUserChat.setVisible(true);
                badgeDrawableUserChat.setNumber(count.getUserDetails().getNewMsg());
            }
        });

        localCartViewModel.getProductCount().observe(getViewLifecycleOwner(), count -> {
            if (count == 0)
                badgeDrawable.setVisible(false);
            else {
                badgeDrawable.setVisible(true);
                badgeDrawable.setNumber(count);
            }

        });





        return view;
    }//end onCreateView


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //appHelp();
        localCartViewModel.getCartData().observe(getViewLifecycleOwner(), cartWithProducts -> {
            if (cartWithProducts.size() > 0) {
                if (!isDataLoaded) {
                    cartWithProduct = cartWithProducts.get(0);
                    initRecyclerView();
                    //filter by latest filter
                    isDataLoaded = true;
                }
            }
        });

        localCartViewModel.getCartData().observe(getViewLifecycleOwner(), cartWithProducts -> {
            if (cartWithProducts.size() == 1 && updateFromDb) {
                BaseCodeClass.sendOrderClass = new SendOrderClass();
                CartWithProduct currentParent = cartWithProducts.get(0);
                CurrentCartId.setId(currentParent.cartInformation.getCartId());
                CartInformation cartInformation = currentParent.cartInformation;

                BaseCodeClass.sendOrderClass.setSendOrderClass(null, null, cartInformation.getOrderID(), cartInformation.getEmployeeID(), cartInformation.getCustomerID(), cartInformation.getCompanyID(), cartInformation.getOrderDate(), cartInformation.getShippedDate(), cartInformation.getShipperID(), cartInformation.getShipName(), cartInformation.getShipAddress(),
                        cartInformation.getShipCity(), cartInformation.getShipStateProvince(), cartInformation.getShipZIPPostalCode(), cartInformation.getShipCountryRegion(), cartInformation.getShippingFee(), cartInformation.getTaxes(), cartInformation.getPaymentType(), cartInformation.getPaidDate(), cartInformation.getNotes(), cartInformation.getTaxRate(), cartInformation.getTaxStatus(), cartInformation.getStatusID(), cartInformation.getSumPrice());

                for (CartProduct cartProduct :
                        currentParent.cartProducts) {
                    BaseCodeClass.sendOrderClass.getOrder_Details().add(new Order_DetailsViewModels(cartProduct.getId(), cartProduct.getOrderID(), cartProduct.getProductIDString(), cartProduct.getQuantity(), cartProduct.getUnitPrice(),
                            cartProduct.getDiscount(), cartProduct.getStatusID(), cartProduct.getDateAllocated(), cartProduct.getPurchaseOrderID(), cartProduct.getInventoryID(), cartProduct.getProductName(), null));
                }
            }
            if (updateFromDb) {
                String ddd = baseCodeClass.getUserID();
                Log.d("LOG",ddd);
                //new ManageProductClass(mContext, main2Fragment, baseCodeClass.getCompanyID()).execute(baseCodeClass.getCompanyID(), BaseCodeClass.DownloadParam);
                dbViewModel.getAllProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
                    @Override
                    public void onChanged(List<Product> products) {
                        if (products == null || products.size() == 0) {
                            loadProductFromServer(CompanyID);
                        } else {

                                dbViewModel.getAllProperties().observe(getViewLifecycleOwner(), new Observer<List<Properties>>() {
                                    @Override
                                    public void onChanged(List<Properties> propertiesList) {
                                        analyzeReceiveProduct(products, propertiesList);
                                        if(!checkUpdateProduct) {
                                            updateData(CompanyID);
                                            checkUpdateProduct = true;
                                        }
                                    }
                                });



                        }
                    }
                });
                updateFromDb = false;
            }
        });

        //update Rv item price if changed
        productViewModel.getLiveEditedValue().observe(getViewLifecycleOwner(), s -> productRecyclerViewAdapter.notifyDataSetChanged());

    }

    private void appHelp(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("appHelp",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        boolean hasSeen = sharedPreferences.getBoolean("help",false);
        if(!hasSeen){
            TapTargetSequence sequence = new TapTargetSequence(getActivity());
            TapTarget btnFilterTarget = TapTarget.forView(view.findViewById(R.id.btnFilter),"دکمه فیلتر محصول","به کمک این دکمه می توانید محصول خود را فیلتر کنید")
                    .cancelable(false)
                    .drawShadow(true)
                    .dimColor(android.R.color.tab_indicator_text)
                    .outerCircleColor(android.R.color.holo_blue_dark)
                    .targetCircleColor(android.R.color.holo_green_dark)
                    .transparentTarget(true)
                    .targetRadius(32)
                    .outerCircleAlpha(0.96f)
                    .titleTextSize(15)
                    .descriptionTextSize(12)
                    .descriptionTextColor(android.R.color.white)
                    .textColor(android.R.color.holo_blue_bright)
                    .titleTextColor(android.R.color.white)
                    .tintTarget(false);
            TapTarget btnSortTarget = TapTarget.forView(view.findViewById(R.id.btnSort),"دکمه مرتب سازی محصول","به کمک این دکمه می توانید محصول خود را مرتب کنید کنید")
                    .cancelable(false)
                    .drawShadow(true)
                    .dimColor(android.R.color.tab_indicator_text)
                    .outerCircleColor(android.R.color.holo_blue_dark)
                    .targetCircleColor(android.R.color.holo_green_dark)
                    .transparentTarget(true)
                    .targetRadius(32)
                    .outerCircleAlpha(0.96f)
                    .titleTextSize(15)
                    .descriptionTextSize(12)
                    .descriptionTextColor(android.R.color.white)
                    .textColor(android.R.color.holo_blue_bright)
                    .titleTextColor(android.R.color.white)
                    .tintTarget(false);

            TapTarget btnGoProfileTarget = TapTarget.forView(view.findViewById(R.id.companyLogo),"دکمه پروفایل","این دکمه شما را به صفحه پروفایل می برد")
                    .cancelable(false)
                    .drawShadow(true)
                    .dimColor(android.R.color.tab_indicator_text)
                    .outerCircleColor(android.R.color.holo_blue_dark)
                    .targetCircleColor(android.R.color.holo_green_dark)
                    .transparentTarget(true)
                    .targetRadius(32)
                    .outerCircleAlpha(0.96f)
                    .titleTextSize(15)
                    .descriptionTextSize(12)
                    .descriptionTextColor(android.R.color.white)
                    .textColor(android.R.color.holo_blue_bright)
                    .titleTextColor(android.R.color.white)
                    .tintTarget(false);


            /*TapTarget t = TapTarget.forBounds(bottomNavigationViewEx.getBottomNavigationItemView(R.id.ic_shoppingCenter).getClipBounds(),"Title","Description")
                    .cancelable(false)
                    .drawShadow(true)
                    .dimColor(android.R.color.tab_indicator_text)
                    .outerCircleColor(android.R.color.holo_blue_dark)
                    .targetCircleColor(android.R.color.holo_green_dark)
                    .transparentTarget(true)
                    .targetRadius(32)
                    .outerCircleAlpha(0.96f)
                    .titleTextSize(15)
                    .descriptionTextSize(12)
                    .descriptionTextColor(android.R.color.white)
                    .textColor(android.R.color.holo_blue_bright)
                    .titleTextColor(android.R.color.white)
                    .tintTarget(false);*/

            sequence.targets(btnFilterTarget,btnSortTarget,btnGoProfileTarget);
            sequence.listener(new TapTargetSequence.Listener() {
                @Override
                public void onSequenceFinish() {
                    editor.putBoolean("help",true);
                    editor.apply();
                }

                @Override
                public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                }

                @Override
                public void onSequenceCanceled(TapTarget lastTarget) {

                }
            }).start();
        }
    }

    private void analyzeReceiveProduct(List<Product> products, List<Properties> propertiesList) {
        updateTime = "2020-01-01T00:00:00.000";
        lastUpdateTime = convertStrToDate(updateTime);
        productDataList.clear();
        particularProduct.clear();
        bulletinProduct.clear();
        allProductsPId.clear();

        List<Properties> properties = new ArrayList<>();
        for (Product product : products) {
            StandardPrice standardPrice = new StandardPrice();
            standardPrice.setOffPrice(product.offPrice);
            standardPrice.setPrice(product.Price);
            standardPrice.setShowoffPrice(product.ShowoffPrice);
            standardPrice.setShowPrice(product.ShowPrice);
            standardPrice.setShowStandardCost(product.ShowStandardCost);
            standardPrice.setStandardCost(product.StandardCost);
            allProductsPId.add(product.ProductID);

            if (product.Deleted ||
                    (!product.Show) ||
                    (!product.CompanyID.equals(CompanyID))
            ) {
                continue;
            }
            properties.clear();
            for (Properties proper : propertiesList) {

                if (proper.ProductID.equals(product.ProductID)) {
                    properties.add(proper);
                }
            }
            if(properties.size() == 0)
                continue;
            ReceiveProductClass spc = new ReceiveProductClass(product, properties,standardPrice);



            List<String> category = new ArrayList<>();
            String cat = product.Category;
            if (cat != null && !cat.isEmpty()) {
                String[] aCategory = cat.split("\\.");
                for (String s : aCategory) {
                    if (s != null && !s.isEmpty() && !s.equals("null")) {
                        category.add(s);
                    }
                }
            }
            NumberFormat formatter = new DecimalFormat("#,###");
            String nPrice = product.ShowStandardCost;
            if (nPrice != null && !nPrice.isEmpty()) {
                String[] aPrice = nPrice.split(",");
                StringBuilder priceBuilder = new StringBuilder();
                for (String s : aPrice) {
                    priceBuilder.append(s);
                }
                nPrice = priceBuilder.toString();
                nPrice = formatter.format(Integer.parseInt(nPrice));
            }

            String LPrice = product.ShowoffPrice;
            if (LPrice != null && !LPrice.isEmpty()) {
                String[] aPrice = LPrice.split("\\.");
                LPrice = (aPrice[0]);

            }
            AllProductData allProductData = new AllProductData(mContext, BaseCodeClass.getCompanyName(), false, product.Likeit,
                    null, product.Saveit, /*Integer.getInteger(product.LikeCount)*/product.LikeCount, /*Integer.getInteger(product.ViewedCount)*/product.ViewedCount, category, spc);
            if (ISParticular(String.valueOf(product.ReorderLevel))) {
                particularProduct.add(allProductData);
            }
            if (ISBulletin(String.valueOf(product.ReorderLevel))) {
                bulletinProduct.add((allProductData));
            }
            productDataList.add(allProductData);
            try {
                Date temp = convertStrToDate(String.valueOf(product.UpdateDate));
                if (temp != null) {
                    if (temp.after(lastUpdateTime)) {
                        lastUpdateTime = temp;
                        updateTime = String.valueOf(product.UpdateDate);
                    }
                }

            } catch (Exception e) {
                Log.d("Error",e.toString());
            }
            //  String[] like = cursor.getString(cursor.getColumnIndex(MyDataBase.Spare2)).split("&");
            // String[] bookmark = cursor.getString(cursor.getColumnIndex(MyDataBase.Spare3)).split("&");

        }
        recyclerViewCanUpdating();
    }

    private boolean ISParticular(String reOrder) {
        if (reOrder.equals("100") || reOrder.equals("200")) {
            return true;
        }
        return false;
    }

    private boolean ISBulletin(String reOrder) {
        if (reOrder.equals("150") || reOrder.equals("200")) {
            return true;
        }
        return false;
    }

    private void loadProductFromServer(String CompanyId) {
        String test = baseCodeClass.getUserID();
        Call<List<ReceiveProductClass>> call = loadProductApi.loadProduct(CompanyId, baseCodeClass.getUserID());
        call.enqueue(new Callback<List<ReceiveProductClass>>() {
            @Override
            public void onResponse(Call<List<ReceiveProductClass>> call, Response<List<ReceiveProductClass>> response) {
                synchronizeProductTableVer2(response.body(), false, dbViewModel);
            }

            @Override
            public void onFailure(Call<List<ReceiveProductClass>> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

    ReceiveProductClass inQueueProduct = null;

    private Boolean synchronizeProductTableVer2(List<ReceiveProductClass> NetProduct, boolean updateMode, DBViewModel dbViewModel) {
        boolean result = true;
        final boolean[] mustUpdate = {false};

        //DatabaseService databaseService = new DatabaseService(mContext);
        long itemId;

        if (!updateMode) {
            dbViewModel.deleteAllProducts();
            dbViewModel.deleteAllProperties();
        }
        for (i = 0; i < NetProduct.size(); i++) {

            if (updateMode) {

                inQueueProduct = NetProduct.get(i);
                if(ProductExistinLocal(inQueueProduct.getProductID()))
                {
                    mustUpdate[0] = true;
                    updateProduct(inQueueProduct, dbViewModel);
                }
                else
                {
                    insertIntoDB(inQueueProduct, dbViewModel);
                    allProductsPId.add(inQueueProduct.getProductID());
                }


                //Product product = databaseService.getOneProduct(NetProduct.get(i).getProductID());
               /* dbViewModel.getOneProduct(inQueueProduct.getProductID()).observe(getViewLifecycleOwner(), new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer productCount) {
                        if (productCount > 0) {
                            mustUpdate[0] = true;
                            if (inQueueProduct != null)
                                updateProduct(inQueueProduct, dbViewModel);
                        } else if (inQueueProduct != null)
                            insertIntoDB(inQueueProduct, dbViewModel);
                        inQueueProduct = null;
                    }

                });*/


            } else
                insertIntoDB(NetProduct.get(i), dbViewModel);


        }

        if (NetProduct.size() > 0/*mustUpdate[0]*/) {
            dbViewModel.getAllProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
                @Override
                public void onChanged(List<Product> products) {
                    dbViewModel.getAllProperties().observe(getViewLifecycleOwner(), new Observer<List<Properties>>() {
                        @Override
                        public void onChanged(List<Properties> propertiesList) {
                            analyzeReceiveProduct(products, propertiesList);
                        }
                    });

                }
            });

        }

        swipeRefreshLayout.setRefreshing(false);
        return result;

    }

    private  boolean ProductExistinLocal(String PID)
    {
        for (String  p:
                allProductsPId) {
            if(p.equals(PID))return true;
        }
        return false;
    }
    private void insertIntoDB(ReceiveProductClass NetProduct, DBViewModel dbViewModel) {

        Product product = new Product();
        product.ViewedCount = NetProduct.getViewedCount();

        product.UpdateDate = NetProduct.getUpdateDate();
        product.Unit = NetProduct.getUnit();

        product.TargetLevel = NetProduct.getTargetLevel();
        product.SupplierID = NetProduct.getSupplierID();
        product.StandardCost = NetProduct.getStandardCost().getStandardCost();
        product.offPrice = NetProduct.getStandardCost().getOffPrice();
        product.Price = NetProduct.getStandardCost().getPrice();
        product.ShowStandardCost = NetProduct.getStandardCost().getShowStandardCost();
        product.ShowoffPrice = NetProduct.getStandardCost().getShowoffPrice();
        product.ShowPrice = NetProduct.getStandardCost().getShowPrice();
        product.Show = NetProduct.getShow();
        product.SellCount = NetProduct.getSellCount();
        product.Saveit = NetProduct.getSaveit();
        product.SaveCount = NetProduct.getSaveCount();
        product.ReorderLevel = NetProduct.getReorderLevel();
        product.QuantityPerUnit = NetProduct.getQuantityPerUnit();
        product.ProductName = NetProduct.getProductName();
        product.ProductID = NetProduct.getProductID();
        product.MinimumReorderQuantity = NetProduct.getMinimumReorderQuantity();
        product.Likeit = NetProduct.isLikeit();
        product.LikeCount = NetProduct.getLikeCount();
        product.Discontinued = NetProduct.getDiscontinued();
        product.Description = NetProduct.getDescription();
        product.Deleted = NetProduct.getDeleted();
        product.CompanyID = NetProduct.getCompanyID();
        product.Category = NetProduct.getCategory();
        if(NetProduct.getSpare1() == null || NetProduct.getSpare1().equals(""))
            product.Spare1 = "#ffffff";
        else
            product.Spare1 = NetProduct.getSpare1();
        if(NetProduct.getSpare2() == null || NetProduct.getSpare1().equals(""))
            product.Spare2 = "#ffffff";
        else
            product.Spare2 = NetProduct.getSpare2();
        if(NetProduct.getSpare3() == null || NetProduct.getSpare1().equals(""))
            product.Spare3 = "#ffffff";
        else
            product.Spare3 = NetProduct.getSpare3();
        dbViewModel.insertProduct(product);

        for (ProductPropertisClass productPropertis : NetProduct.getProductPropertis()) {
            Properties properties = new Properties();
            properties.ProductID = productPropertis.getProductID();
            properties.PropertiesGroup = productPropertis.getPropertisGroup();
            properties.PropertiesName = productPropertis.getPropertisName();
            properties.PropertiesValue = productPropertis.getPropertisValue();
            properties.UpdateTime = productPropertis.getUpdatTime();
            dbViewModel.insertProperties(properties);
        }
    }

    private void updateProduct(ReceiveProductClass productClass, DBViewModel dbViewModel) {
        Product product = new Product();
        product.Deleted = productClass.getDeleted();
        product.Category = productClass.getCategory();
        product.CompanyID = productClass.getCompanyID();
        product.Description = productClass.getDescription();
        product.Discontinued = productClass.getDiscontinued();
        product.LikeCount = productClass.getLikeCount();
        product.Likeit = productClass.isLikeit();
        product.MinimumReorderQuantity = productClass.getMinimumReorderQuantity();
        product.ProductID = productClass.getProductID();
        product.ProductName = productClass.getProductName();
        product.QuantityPerUnit = productClass.getQuantityPerUnit();
        product.ReorderLevel = productClass.getReorderLevel();
        product.SaveCount = productClass.getSaveCount();
        product.Saveit = productClass.getSaveit();
        product.SellCount = productClass.getSellCount();
        product.Show = productClass.getShow();
        product.StandardCost = productClass.getStandardCost().getStandardCost();
        product.offPrice = productClass.getStandardCost().getOffPrice();
        product.Price = productClass.getStandardCost().getPrice();
        product.ShowStandardCost = productClass.getStandardCost().getShowStandardCost();
        product.ShowoffPrice = productClass.getStandardCost().getShowoffPrice();
        product.ShowPrice = productClass.getStandardCost().getShowPrice();
        product.SupplierID = productClass.getSupplierID();
        product.TargetLevel = productClass.getTargetLevel();
        product.Unit = productClass.getUnit();
        product.UpdateDate = productClass.getUpdateDate();
        product.ViewedCount = productClass.getViewedCount();
        if(productClass.getSpare1() == null || productClass.getSpare1().equals(""))
            product.Spare1 = "#ffffff";
        else
            product.Spare1 = productClass.getSpare1();
        if(productClass.getSpare2() == null || productClass.getSpare1().equals(""))
            product.Spare2 = "#ffffff";
        else
            product.Spare2 = productClass.getSpare2();
        if(productClass.getSpare3() == null || productClass.getSpare1().equals(""))
            product.Spare3 = "#ffffff";
        else
            product.Spare3 = productClass.getSpare3();
        dbViewModel.updateProduct(product);
    }

    private void updateData(String companyID) {
        lastUpdateTime = convertStrToDate(updateTime);
        try {
            UpdatedProductBody updatedProductBody = new UpdatedProductBody(companyID, baseCodeClass.getUserID(), updateTime);
            Call<List<ReceiveProductClass>> call = loadProductApi.getUpdatedData(updatedProductBody);
            call.enqueue(new Callback<List<ReceiveProductClass>>() {
                @Override
                public void onResponse(Call<List<ReceiveProductClass>> call, Response<List<ReceiveProductClass>> response) {
                    synchronizeProductTableVer2(response.body(), true, dbViewModel);
                    Log.d(TAG, "onResponse: ******update*******" + response.body().size());
                }

                @Override
                public void onFailure(Call<List<ReceiveProductClass>> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        } catch (Exception ex) {
            baseCodeClass.logMessage(" loadProduct Error " + ex.getMessage(), mContext);
        }
    }

    private void manageLists(viewMode allProduct) {
        if (allProduct == viewMode.allProduct) {
            showView = viewMode.allProduct;
            productRecyclerView.setVisibility(View.VISIBLE);
            bulletinRecyclerView.setVisibility(View.GONE);
            cardViewFilter.setVisibility(View.VISIBLE);
        } else {
            showView = viewMode.bulletin;
            productRecyclerView.setVisibility(View.GONE);
            bulletinRecyclerView.setVisibility(View.VISIBLE);
            cardViewFilter.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (productRecyclerViewAdapter != null) {
            productRecyclerViewAdapter.notifyDataSetChanged();
            productRecyclerViewAdapter.getFilter().filter(filterValue);
        }
        countsViewModel.callForCounts(baseCodeClass.getUserID(), baseCodeClass.getToken(), baseCodeClass.getCompanyID());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d(TAG, "onHiddenChanged: " + hidden);
        if (!hidden) {
            Log.d(TAG, "onHiddenChanged: notifying");
            productRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    public void initBulletinRecyclerView() {

        try {
            BaseCodeClass.bulletinRecyclerViewAdapter = new BulletinRecyclerViewAdapter(mContext, bulletinProduct);
            GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false);
            bulletinRecyclerView.setLayoutManager(layoutManager);
            bulletinRecyclerView.setAdapter(BaseCodeClass.bulletinRecyclerViewAdapter);
        } catch (Exception e) {
            logMessage("Main2Fragment 300 >> " + e.getMessage(), mContext);
        }
    }


    List<ProductPropertisClass> loadProperty(String Pid) {
        try {
            List<ProductPropertisClass> lppc = new ArrayList<>();

            Cursor cursor = mydb.GetProductProperties(mContext, Pid);
            List<String> s = new ArrayList<>();
            s.add(Pid);
            if (cursor.moveToFirst()) {
                do {
                    ProductPropertisClass ppc = new ProductPropertisClass(cursor.getString(cursor.getColumnIndex(MyDataBase.ProductID)), cursor.getString(cursor.getColumnIndex(MyDataBase.PropertisGroup)),
                            cursor.getString(cursor.getColumnIndex(MyDataBase.PropertisName)), cursor.getString(cursor.getColumnIndex(MyDataBase.PropertisValue)),
                            cursor.getString(cursor.getColumnIndex(MyDataBase.UpdateTime)));
                    s.add(cursor.getString(cursor.getColumnIndex(MyDataBase.PropertisName)));
                    lppc.add(ppc);
                } while (cursor.moveToNext());
                return lppc;
            }
        } catch (Exception e) {
            Log.d("Error",e.getMessage());
            return null;
        }
        return null;
    }

    LinearLayoutManager layoutManagerProduct;

    private void initRecyclerView() {
        try {
            if (productDataList != null) {

                final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                layoutManagerProduct = layoutManager;
                //layoutManager.setReverseLayout(true);
                productRecyclerView.setLayoutManager(layoutManager);
                productRecyclerViewAdapter = new ProductRecyclerViewAdapter(mContext, productDataList, badgeSharedViewModel, localCartViewModel, getChildFragmentManager());
                productRecyclerView.setAdapter(productRecyclerViewAdapter);
                productRecyclerView.setVisibility(View.VISIBLE);

                productRecyclerViewAdapter.setLoadMoreInterFace(() -> {
                    try {
                        if (productRecyclerViewAdapter.showSize() < productRecyclerViewAdapter.filteredProduct.size()) {
                            AllProductData nulProductData = null;
                            productRecyclerViewAdapter.showProductData.add(nulProductData);
                            productRecyclerViewAdapter.notifyItemInserted(productRecyclerViewAdapter.showSize());
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        productRecyclerViewAdapter.showProductData.remove(productRecyclerViewAdapter.showProductData.size() - 1);
                                        productRecyclerViewAdapter.notifyItemRemoved(productRecyclerViewAdapter.showProductData.size());

                                        int i = 0;
                                        for (AllProductData a : productRecyclerViewAdapter.filteredProduct
                                        ) {
                                            if (productRecyclerViewAdapter.showProductData.contains(a)) {
                                                continue;
                                            }
                                            productRecyclerViewAdapter.showProductData.add(a);
                                            i++;
                                            if (i >= 7) {
                                                break;
                                            }
                                        }
//                                        productRecyclerViewAdapter.updateImage();
                                        productRecyclerViewAdapter.notifyDataSetChanged();
                                        productRecyclerViewAdapter.setLoaded();
                                    } catch (Exception e) {
                                        logMessage("Main2Fragment 999 >> " + e.getMessage(), mContext);
                                    }
                                }
                            }, 500);
                        } else {
//                            baseCodeClass.logMessage("تمام شد", mContext);
                        }
                    } catch (Exception e) {
                        logMessage("Main2Fragment 2999 >> " + e.getMessage(), mContext);
                    }
                });
            } else {
                Toast.makeText(requireContext(), "در حال حاضر فروشگاه محصولی برای نمایش ندارد.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Log.d("Error" , e.getMessage());
        }
    }


    //filter
    public void initFilterRecyclerView() {
        try {
            filterName.clear();
            filterImage.clear();
            for (String item :
                    mCategory) {
                filterName.add(item);
                filterImage.add(GetImag(item));
            }
            filterName.add("همه");
            filterImage.add(R.drawable.allfilter);

            filterList.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));

            FilterRecyclerViewAdapter filterAdapter = new FilterRecyclerViewAdapter(mContext, filterName, filterImage, filterValue, this);

            filterList.setAdapter(filterAdapter);
        } catch (Exception e) {
            logMessage("initFilter : " + e.getMessage(), mContext);
        }
    }

    private Integer GetImag(String item) {


        switch (item) {
            case "خامه":
            case "کره":
            case "کره و خامه":
                return R.drawable.butter;
            case "خشکبار و برنج":
                return R.drawable.flour;
            case "شیرینی":
                return R.drawable.cookies;
            case "مربا":
                return R.drawable.jam;
            case "روغن":
                return R.drawable.oil;
            case "بستنی":
                return R.drawable.icecream;
            case "تخم مرغ":
                return R.drawable.egg;
            case "دمنوش":
                return R.drawable.damnoosh;
            case "قند و نبات":
                return R.drawable.candy;
            case "عرقیجات":
            case "شربت و ابمیوه":
                return R.drawable.beer;
            case "کشک":
                return R.drawable.kashk;
            case "ترشیجات":
            case "ترشی":
                return R.drawable.pickle;
            case "عسل":
                return R.drawable.honey;
            case "شیره":
                return R.drawable.syrup;
            case "خرما":
                return R.drawable.khorma;
            case "حلواشکری ارده کنجد":
            case "حلوا ارده":
                return R.drawable.halvaadrde;
            case "پنیر":
                return R.drawable.cheese;
            case "دوغ":
                return R.drawable.dough;
            case "ماست":
                return R.drawable.yogurt;
            case "شیر":
                return R.drawable.milk;
            case "قره قروت":
                return R.drawable.granola;
            case "کرم  و شکلات صبحانه":
                return R.drawable.ic_chocolate;
            case "زیتون":
                return R.drawable.ic_olive;
            case "منجمد و یخچالی":
                return R.drawable.ic_beef;
            case "کره بادام زمینی":
                return R.drawable.ic_peanutbutter;
            default:
                return R.drawable.invisiblepass;
        }
    }

    private void initCategoryRecyclerView() {
        try {

            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            layoutManager.setReverseLayout(true);
            RecyclerView recyclerView = view.findViewById(R.id.CategoryRecyclerView);
            recyclerView.setLayoutManager(layoutManager);
            categoryRecyclerViewAdapter = new CategoryRecyclerViewAdapter(mContext, mCategory, this);
            recyclerView.setAdapter(categoryRecyclerViewAdapter);

        } catch (Exception e) {
            logMessage("initCategory : " + e.getMessage(), mContext);
        }
    }

    public void loadCompany() {
        mCompanyNames.add("فروشگاه دهکده");
        mCompanyLogo.add(R.drawable.logodehkade);

        mCompanyNames.add("اضافه کردن فروشگاه جدید");
        mCompanyLogo.add(R.drawable.ic_add);

        initCompanyRecyclerView();
    }

    private void initCompanyRecyclerView() {
        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            layoutManager.setReverseLayout(true);
            RecyclerView recyclerView = view.findViewById(R.id.CompanyRecyclerView);
            recyclerView.setLayoutManager(layoutManager);
            CompanyRecyclerViewAdapter adapter = new CompanyRecyclerViewAdapter(mContext, mCompanyNames, mCompanyLogo);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            logMessage("initCompany : " + e.getMessage(), mContext);
        }
    }

    private void initParticularRecyclerView() {
        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            RecyclerView recyclerView = view.findViewById(R.id.particularProduct);
            recyclerView.setLayoutManager(layoutManager);
            ParticularProductRecyclerViewAdapter particularProductRecyclerViewAdapter = new ParticularProductRecyclerViewAdapter(mContext, particularProduct);
            recyclerView.setAdapter(particularProductRecyclerViewAdapter);
        } catch (Exception e) {
            logMessage("Main2Fragment 500 >> " + e.getMessage(), mContext);
        }
    }

    public void showFilteringDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_filtering_list, null);
        builder.setView(view);
        builder.setNeutralButton("لغو", (dialog, which) -> {
            dialog.dismiss();
        });
        MaterialButton btnCheap, btnExpensive, btnSell, btnRelated, btnView, btnNew;

        btnCheap = view.findViewById(R.id.btn_filter_cheap);
        btnExpensive = view.findViewById(R.id.btn_filter_expensive);
        btnSell = view.findViewById(R.id.btn_filter_sell);
        btnView = view.findViewById(R.id.btn_filter_view);
        btnRelated = view.findViewById(R.id.btn_filter_related);
        btnNew = view.findViewById(R.id.btn_filter_new);

        switch (filterOption) {
            case SELL:
                btnSell.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_tik, 0, 0, 0);
                break;
            case EXPENSIVE:
                btnExpensive.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_tik, 0, 0, 0);
                break;
            case VIEW:
                btnView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_tik, 0, 0, 0);
                break;
            case RELATED:
                btnRelated.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_tik, 0, 0, 0);
                break;
            case CHEAP:
                btnCheap.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_tik, 0, 0, 0);
                break;
            case New:
                btnNew.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_tik, 0, 0, 0);


        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //related
        btnRelated.setOnClickListener(v -> {
            filterOption = FilterOption.RELATED;
            alertDialog.dismiss();
        });

        //cheapest price
        btnCheap.setOnClickListener(v -> {
            filterOption = FilterOption.CHEAP;
            alertDialog.dismiss();
            productRecyclerViewAdapter.sortCheapData();

        });

        //most expensive
        btnExpensive.setOnClickListener(v -> {
            filterOption = FilterOption.EXPENSIVE;
            alertDialog.dismiss();
            productRecyclerViewAdapter.sortExpensiveData();
        });

        //most sell
        btnSell.setOnClickListener(v -> {
            filterOption = FilterOption.SELL;
            alertDialog.dismiss();
            productRecyclerViewAdapter.sortMostSell();
        });

        //most view
        btnView.setOnClickListener(v -> {
            filterOption = FilterOption.VIEW;
            alertDialog.dismiss();
            productRecyclerViewAdapter.sortMostView();
        });

        // new
        btnNew.setOnClickListener(v -> {
            filterOption = FilterOption.New;
            alertDialog.dismiss();
            productRecyclerViewAdapter.sortByNewProduct();

        });
    }

    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(mContext,v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.filtering_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.categoriFiltering_menu:
                        try {
                            dialogFragment.show(getChildFragmentManager(), "dialogAlert");
                        } catch (Exception e) {
                            toastMessage("btnFilterClick >> " + e.getMessage(), 9);
                        }

                        break;
                    case R.id.brandFiltering_menu:
                        try {
                            brandFilterDialogFragment.show(getChildFragmentManager(), "dialogAlert");
                        } catch (Exception e) {
                            toastMessage("btnFilterClick >> " + e.getMessage(), 9);
                        }
                        break;
                }
                return false;
            }
        });

        popupMenu.show();

    }



    public void toastMessage(String message, int id) {
        Toast.makeText(mContext, id + ">>" + message, Toast.LENGTH_LONG).show();
    }
}
