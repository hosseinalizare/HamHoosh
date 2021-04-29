package com.example.koohestantest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.koohestantest1.Utils.BadgeCounter;
import com.example.koohestantest1.Utils.StringUtils;
import com.example.koohestantest1.activity.Main2Activity;
import com.example.koohestantest1.classDirectory.StandardPrice;
import com.example.koohestantest1.constants.IntentKeys;
import com.example.koohestantest1.fragments.bottomsheet.CommentsBottomSheet;
import com.example.koohestantest1.fragments.bottomsheet.EditBottomSheet;
import com.example.koohestantest1.model.entity.CartProduct;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.example.koohestantest1.viewModel.BadgeSharedViewModel;
import com.example.koohestantest1.viewModel.LocalCartViewModel;
import com.example.koohestantest1.viewModel.ProductViewModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.koohestantest1.ApiDirectory.LoadProductApi;
import com.example.koohestantest1.DB.MyDataBase;
import com.example.koohestantest1.ViewModels.BookMarkViewModel;
import com.example.koohestantest1.ViewModels.PostLikeViewModel;
import com.example.koohestantest1.ViewModels.PostViewViewModel;
import com.example.koohestantest1.classDirectory.AllProductData;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.CategoryRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.ProductPropertiesRecyclerViewAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.manageOrderClass;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.productDataList;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.selectedProduct;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.sendOrderClass;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.userID;

public class ViewProductActivity extends AppCompatActivity {

    ArrayList<String> mCategory = new ArrayList<>();

    ArrayList<String> mPropertyName = new ArrayList<>();
    ArrayList<String> mPropertyValue = new ArrayList<>();

    TextView txtPName, txtPPrice, txtPDescription;
    ImageView PImage, like, bookmark;
    Button btnAddToCart;

    private String selectedPid;
    private MyDataBase mydb;
    BaseCodeClass baseCodeClass;
    LoadProductApi loadProductApi;

    private String TAG = ViewProductActivity.class.getSimpleName() + "Debug";
    private CardView cardViewController;
    private TextView tvCounter, tvLikeCount,txtViewCount;
    private ImageView ivAdd, ivRemove;
    private BadgeSharedViewModel badgeSharedViewModel;
    private FrameLayout frameLayout;
    private ImageView ivCartIcon;
    private BadgeDrawable badgeDrawable;
    private CardView cardNoItem;
    private ImageView ivComment;
    private LocalCartViewModel localCartViewModel;
    private CartProduct cartProduct;
    private ProductViewModel productViewModel;
    private ConstraintLayout contentContiner;
    private ExplorerFragment explorerFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        contentContiner = findViewById(R.id.activity_view_product_continer);

        badgeSharedViewModel = new ViewModelProvider(this).get(BadgeSharedViewModel.class);
        localCartViewModel = new ViewModelProvider(this).get(LocalCartViewModel.class);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        ivCartIcon = findViewById(R.id.iv_cart_icon_product);
        frameLayout = findViewById(R.id.frame_cart_icon_products);
        ivComment = findViewById(R.id.iv_comment_product);
        tvLikeCount = findViewById(R.id.tv_like_count);
        txtViewCount = findViewById(R.id.txt_activityViewProduct_txtViewCount);
        cardNoItem = findViewById(R.id.card_no_item);

        //setUp badge view
        badgeDrawable = BadgeDrawable.create(this);
        badgeDrawable.setBadgeGravity(BadgeDrawable.TOP_END);
        badgeDrawable.setVerticalOffset(24);
        badgeDrawable.setHorizontalOffset(24);

        frameLayout.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {

            //either of the following two lines of code  work
            //badgeDrawable.updateBadgeCoordinates(imageView, frameLayout);
            BadgeUtils.attachBadgeDrawable(badgeDrawable, ivCartIcon, frameLayout);
        });


        txtPName = findViewById(R.id.ProductName);
        txtPPrice = findViewById(R.id.ProductPrice);
        txtPDescription = findViewById(R.id.productDescription);
        PImage = findViewById(R.id.productImage);
        btnAddToCart = findViewById(R.id.btnAddProduct);

        cardViewController = findViewById(R.id.card_controlling_items);
        tvCounter = findViewById(R.id.tv_products_count);
        ivAdd = findViewById(R.id.iv_add_item_product);
        ivRemove = findViewById(R.id.iv_remove_item_product);

        like = findViewById(R.id.like);
        bookmark = findViewById(R.id.bookmark);


        baseCodeClass = new BaseCodeClass();
        mydb = new MyDataBase(this);

        selectedPid = getIntent().getStringExtra("PID");
        Log.d(TAG, "onCreate: " + selectedPid);
        Log.d(TAG, "onCreate: " + selectedProduct.getProductClass().getProductName());
        int stock = selectedProduct.getProductClass().getDiscontinued();
        if (stock == 0) {
            btnAddToCart.setVisibility(View.GONE);
            cardNoItem.setVisibility(View.VISIBLE);
        }
        initCategoryRecyclerView();


        tvLikeCount.setText(String.valueOf(selectedProduct.getProductClass().getLikeCount()));
        txtViewCount.setText(StringUtils.showProductViewCount(selectedProduct.getProductClass().getViewedCount()));


        loadingProduct();
        loadProductProperties();

        //init like
        handleLikeView();

        //init bookmark
        initBookmarkView();

        Log.d(TAG, "onCreate: isliked " + selectedProduct.isLike());
        Log.d(TAG, "onCreate: " + selectedProduct.getProductClass().isLikeit());

        if (selectedProduct.isSelectedToCart()) {
            showController();
        }

        final Retrofit retrofit = RetrofitInstance.getRetrofit();

        loadProductApi = retrofit.create(LoadProductApi.class);

        counterViewPost(selectedPid, userID, "1");


        like.setOnClickListener(v -> {
            setUpLike();
        });


        bookmark.setOnClickListener(v -> {
            try {
                if (selectedProduct.isBookMark()) {
                    bookmarked(selectedPid, userID, false);
                    bookmark.setImageResource(R.drawable.ic_bookmark);
                } else {
                    bookmarked(selectedPid, userID, true);
                    bookmark.setImageResource(R.drawable.ic_bookmarked);
                }

                YoYo.with(Techniques.RubberBand)
                        .playOn(bookmark);
            } catch (Exception e) {
                Log.d("Error" , e.getMessage());
            }
        });

        ivAdd.setOnClickListener(v -> {
            addItemInCart();
        });

        ivRemove.setOnClickListener(v -> {
            removeItemFromCart();
        });

        ivCartIcon.setOnClickListener(v -> {
            Intent intent = new Intent(this, Main2Activity.class);
            intent.putExtra(IntentKeys.INTENT_CART_PRODUCT_TO_MAIN, true);
            startActivity(intent);
            finish();
        });

        ivComment.setOnClickListener(v -> {
            CommentsBottomSheet commentsBottomSheet = new CommentsBottomSheet(selectedPid, userID,BaseCodeClass.token,BaseCodeClass.CompanyID);
            commentsBottomSheet.show(getSupportFragmentManager(), "TAG_COMMENT_SHEET");
        });

        localCartViewModel.getProductCount().observe(this, integer -> {
            if (integer != 0) {
                badgeDrawable.setVisible(true);
                badgeDrawable.setNumber(integer);
            } else
                badgeDrawable.setVisible(false);
        });

        if (!baseCodeClass.getEmployeeID(baseCodeClass.getUserID()).equals("false")) {
            txtPPrice.setOnLongClickListener(v -> {
                EditBottomSheet editBottomSheet = EditBottomSheet.onNewInstance(0, txtPPrice.getText().toString(), selectedPid,BaseCodeClass.productFieldEnum.StandardCost,null);
                editBottomSheet.show(getSupportFragmentManager(), "EDIT_PRICE_TAG");
                return true;
            });
        }

        //get edited value from bottom sheet to refresh edit text
        productViewModel.getLiveEditedValue().observe(this, s -> {
            StandardPrice standardPrice = new StandardPrice();
            standardPrice.setShowPrice(s);
            txtPPrice.setText(s);
            selectedProduct.getProductClass().setStandardCost(standardPrice);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        badgeSharedViewModel.updateCount();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: ");
    }

    public void setUpLike() {

        try {
            int likeCount = selectedProduct.getProductClass().getLikeCount();
            Log.d(TAG, "setUpLike: bool " + selectedProduct.isLike());

            if (selectedProduct.isLike()) {
                likePost(selectedPid, userID, false);
                like.setImageResource(R.drawable.ic_like);
                if (likeCount > 0)
                    selectedProduct.getProductClass().setLikeCount(--likeCount);
            } else {
                likePost(selectedPid, userID, true);
                like.setImageResource(R.drawable.ic_liked);
                selectedProduct.getProductClass().setLikeCount(++likeCount);
            }

            tvLikeCount.setText(String.valueOf(selectedProduct.getProductClass().getLikeCount()));

            YoYo.with(Techniques.Bounce)
                    .duration(500)
                    .repeat(0)
                    .playOn(like);
        } catch (Exception e) {
            toastMessage("ViewProduct >> " + e.getMessage(), 2000);
        }
    }

    public void handleLikeView() {
        if (selectedProduct.isLike()) {
            like.setImageResource(R.drawable.ic_liked);
        } else {
            like.setImageResource(R.drawable.ic_like);
        }

        YoYo.with(Techniques.Bounce)
                .duration(500)
                .repeat(0)
                .playOn(like);
    }

    public void initBookmarkView() {

        Log.d(TAG, "initBookmarkView: " + selectedProduct.isBookMark());
        if (selectedProduct.isBookMark()) {
            bookmark.setImageResource(R.drawable.ic_bookmarked);
        } else {
            bookmark.setImageResource(R.drawable.ic_bookmark);
        }

        YoYo.with(Techniques.Bounce)
                .duration(500)
                .repeat(0)
                .playOn(like);
    }

    private void initCategoryRecyclerView() {
        try {
            mCategory.add("دسته بندی   >");
            LinearLayoutManager layoutManager = new LinearLayoutManager(ViewProductActivity.this, LinearLayoutManager.HORIZONTAL, false);
            layoutManager.setReverseLayout(true);
            RecyclerView recyclerView = findViewById(R.id.CategoryRecyclerView);
            recyclerView.setLayoutManager(layoutManager);
            CategoryRecyclerViewAdapter adapter = new CategoryRecyclerViewAdapter(ViewProductActivity.this, mCategory);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            Log.d("Error" , e.getMessage());
        }
    }

    private void initPropertyRecyclerView() {
        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            layoutManager.setReverseLayout(true);
            RecyclerView recyclerView = findViewById(R.id.RecycleProductProperties);
            recyclerView.setLayoutManager(layoutManager);
            ProductPropertiesRecyclerViewAdapter adapter = new ProductPropertiesRecyclerViewAdapter(this, mPropertyName, mPropertyValue, 1);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            Log.d("Error" , e.getMessage());
        }
    }

    private void loadingProduct() {
        try {
            txtPName.setText(selectedProduct.getProductClass().getProductName());

            /// generate click for hashtags word

            String description = selectedProduct.getProductClass().getDescription();
            SpannableString spannableString = new SpannableString(description);
            String[] words2 = spannableString.toString().split("\\s+|\n,\\s*|\\.\\s*");
            for (final String word : words2) {
                if (word.startsWith("#")) {
                    ClickableSpan clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(@NonNull View widget) {
                            BaseCodeClass.hashtagsValue=word;
                            finish();
                        }
                    };
                    spannableString.setSpan(clickableSpan, spannableString.toString().indexOf(word), spannableString.toString().indexOf(word) + word.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                }
            }
            txtPDescription.setText(spannableString);
            txtPDescription.setMovementMethod(LinkMovementMethod.getInstance());
            txtPDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int maxLine = txtPDescription.getMaxLines();
                    if (maxLine >= 5) {
                        txtPDescription.setMaxLines(10);
                    }
                }
            });


            txtPPrice.setText(selectedProduct.getProductClass().getStandardCost().getShowPrice());
            String category = selectedProduct.getProductClass().getCategory();
            Log.d(TAG, "loadingProduct: " + category);
            if (category != null && !category.isEmpty()) {
                String[] aCategory = category.split("\\.");
                for (int j = 0; j < aCategory.length; j++) {
                    if (aCategory[j] != null && !aCategory[j].isEmpty() && !aCategory[j].equals("null")) {
                        mCategory.add(aCategory[j]);
                    }
                }
            }
            newDownloadImage(selectedPid, PImage);
        } catch (Exception e) {
            Log.d("Error" , e.getMessage());
        }

    }


    public void newDownloadImage(String pid, ImageView _imageView) {
        try {
            String url = baseCodeClass.pBASE_URL + "Products/DownloadFile?ProductID=" + pid + "&fileNumber=1";
            Glide.with(this).load(url).into(_imageView);
        } catch (Exception e) {
            baseCodeClass.logMessage("ViewProduct glide :" + e.getMessage(), this);
        }
    }


    private void loadProductProperties() {
        int i = 0;
        File imgFile = null;
        try {
            Cursor cursor = mydb.GetProductProperties(this, selectedPid);
            List<String> s = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    mPropertyName.add(cursor.getString(cursor.getColumnIndex(mydb.PropertisName)));
                    mPropertyValue.add(cursor.getString(cursor.getColumnIndex(mydb.PropertisValue)));
                } while (cursor.moveToNext());
            }

            initPropertyRecyclerView();
        } catch (Exception ex) {
            Log.d("Error",ex.getMessage());
        }
    }


    public void toastMessage(String message, int id) {
        Toast.makeText(ViewProductActivity.this, id + " >> " + message, Toast.LENGTH_LONG).show();
    }

    public void btnCancelClick(View view) {
        finish();
    }

    public void btnAddtoCart(View view) {
        try {

            if (selectedProduct.isSelectedToCart()) {
                addItemInCart();
            } else {
                if (manageOrderClass.addProductToCart(selectedProduct.getProductClass())) {
                    productDataList.get(productDataList.indexOf(selectedProduct)).setSelectedToCart(true);
                    showController();

                    badgeSharedViewModel.setCount(BadgeCounter.getCount() + 1);

                } else {
                    toastMessage("", 404);
                }
            }

            localCartViewModel.updateCartInfo(sendOrderClass);
        } catch (Exception e) {
            Log.d("Error",e.getMessage());
        }
    }

    public void counterViewPost(String pid, String uid, String viewCount) {
        try {
            PostViewViewModel viewModel = new PostViewViewModel(uid, pid, viewCount);
            Call<GetResualt> call = loadProductApi.viewProduct(viewModel);
            call.enqueue(new Callback<GetResualt>() {
                @Override
                public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {

                }

                @Override
                public void onFailure(Call<GetResualt> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            Log.d("Error" , e.getMessage());
        }

    }

    public void likePost(String pid, String uid, boolean viewCount) {
        try {
            Log.d(TAG, "likePost: " + viewCount);

            PostLikeViewModel viewModel = new PostLikeViewModel(uid, pid, String.valueOf(viewCount));
            Call<GetResualt> call = loadProductApi.likeProduct(viewModel);
            call.enqueue(new Callback<GetResualt>() {
                @Override
                public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
//                    toastMessage("like", 5);
                    Log.d(TAG, "onResponse: " + response.body().toString());

                    if (response.body().getResualt().equals("100")) {
                        selectedProduct.setLike(viewCount);
                        for (AllProductData allProductData :
                                productDataList) {
                            if (allProductData.getProductClass().getProductID().equals(selectedProduct.getProductClass().getProductID())) {
                                allProductData.setLike(viewCount);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetResualt> call, Throwable t) {

                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.d("Error" , e.getMessage());
        }
    }

    private void bookmarked(String pid, String uid, boolean viewCount) {
        try {
            BookMarkViewModel viewModel = new BookMarkViewModel(uid, pid, String.valueOf(viewCount));
            Call<GetResualt> call = loadProductApi.saveProduct(viewModel);
            Log.d(TAG, "bookmarked: " + String.valueOf(viewCount));
            call.enqueue(new Callback<GetResualt>() {
                @Override
                public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {

                    if (response.body().getResualt().equals("100")) {

                        Log.d(TAG, "onResponse: ok" + viewCount);

                        selectedProduct.setBookMark(viewCount);
                        for (AllProductData allProductData :
                                productDataList) {
                            if (allProductData.getProductClass().getProductID().equals(selectedProduct.getProductClass().getProductID())) {
                                allProductData.setBookMark(viewCount);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetResualt> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.d("Error" , e.getMessage());
        }
    }

    public void addItemInCart() {

        manageOrderClass.setProductQTY(selectedProduct.getProductClass().getProductID(),
                manageOrderClass.getProductQTY(selectedProduct.getProductClass().getProductID()) + 1);
        int currentCount = manageOrderClass.getProductQTY(selectedProduct.getProductClass().getProductID());
        tvCounter.setText(String.valueOf(currentCount));
    }

    public void removeItemFromCart() {
        String productId = selectedProduct.getProductClass().getProductID();
        int count = manageOrderClass.getProductQTY(selectedProduct.getProductClass().getProductID());

        int currentCount = count - 1;
        manageOrderClass.setProductQTY(productId, currentCount);

        if (count - 1 > 0) {
            tvCounter.setText(String.valueOf(currentCount));
        } else {
            badgeSharedViewModel.setCount(BadgeCounter.getCount() - 1);
            deleteProductFromCart(productId);
            cardViewController.setVisibility(View.GONE);
            btnAddToCart.setVisibility(View.VISIBLE);
        }
    }

    public void deleteProductFromCart(String pId) {
        manageOrderClass.RemoveProductFromCart(pId);
        for (AllProductData allProductData :
                productDataList) {
            if (allProductData.getProductClass().getProductID().equals(pId)) {
                allProductData.setSelectedToCart(false);
            }
        }
        Toast.makeText(this, "از سبد حذف شد", Toast.LENGTH_SHORT).show();
    }

    public void showController() {
        btnAddToCart.setVisibility(View.GONE);
        cardViewController.setVisibility(View.VISIBLE);
        int currentCount = manageOrderClass.getProductQTY(selectedProduct.getProductClass().getProductID());
        tvCounter.setText(String.valueOf(currentCount));
    }
}
