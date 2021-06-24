package com.example.koohestantest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.koohestantest1.Utils.BadgeCounter;
import com.example.koohestantest1.Utils.StringUtils;
import com.example.koohestantest1.activity.Main2Activity;
import com.example.koohestantest1.classDirectory.ManageOrderClass;
import com.example.koohestantest1.classDirectory.ProductRecyclerViewAdapter;
import com.example.koohestantest1.classDirectory.StandardPrice;
import com.example.koohestantest1.constants.IntentKeys;
import com.example.koohestantest1.fragments.bottomsheet.CommentsBottomSheet;
import com.example.koohestantest1.fragments.bottomsheet.EditBottomSheet;
import com.example.koohestantest1.local_db.DBViewModel;
import com.example.koohestantest1.local_db.entity.Product;
import com.example.koohestantest1.local_db.entity.Properties;
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

import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.selectedProduct;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.userID;

public class ViewProductActivity extends AppCompatActivity {

    ArrayList<String> mCategory = new ArrayList<>();

    ArrayList<String> mPropertyName = new ArrayList<>();
    ArrayList<String> mPropertyValue = new ArrayList<>();

    TextView txtPName, txtPPrice, txtPDescription, txtSeeMore;
    ImageView PImage, like, bookmark, imgLoadMore;
    Button btnAddToCart;
    LinearLayout linearLayoutSeeMore;

    private String selectedPid;
    BaseCodeClass baseCodeClass;
    LoadProductApi loadProductApi;

    private String TAG = ViewProductActivity.class.getSimpleName() + "Debug";
    private CardView cardViewController;
    private TextView tvCounter, tvLikeCount, txtViewCount;
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
    private String pId;
    private DBViewModel dbViewModel;


    private ManageOrderClass manageOrderClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        dbViewModel = new ViewModelProvider(this).get(DBViewModel.class);
        contentContiner = findViewById(R.id.activity_view_product_continer);
        manageOrderClass = new ManageOrderClass(this);
        Intent intent = getIntent();
        if (intent.hasExtra("PID")) {
            pId = intent.getStringExtra("PID");
            dbViewModel.getSpecificProduct(pId).observe(this, new Observer<Product>() {
                @Override
                public void onChanged(Product product) {
                    selectedProduct = product;
                    selectedPid = getIntent().getStringExtra("PID");


        /*Log.d(TAG, "onCreate: " + selectedPid);
        Log.d(TAG, "onCreate: " + selectedProduct.ProductName);*/
                    int stock = selectedProduct.Discontinued;
                    if (stock == 0) {
                        btnAddToCart.setVisibility(View.GONE);
                        cardNoItem.setVisibility(View.VISIBLE);
                    }
                    initCategoryRecyclerView();


                    tvLikeCount.setText(String.valueOf(selectedProduct.LikeCount));
                    txtViewCount.setText(StringUtils.showProductViewCount(selectedProduct.ViewedCount));


                    loadingProduct();
                    loadProductProperties();

                    //init like
                    handleLikeView();

                    //init bookmark
                    initBookmarkView();

                    Log.d(TAG, "onCreate: isliked " + selectedProduct.Likeit);
                    Log.d(TAG, "onCreate: " + selectedProduct.Likeit);

                    if (selectedProduct.AddToCard) {
                        showController();
                    }
                }
            });
        }
        else
            finish();

        badgeSharedViewModel = new ViewModelProvider(this).get(BadgeSharedViewModel.class);
        localCartViewModel = new ViewModelProvider(this).get(LocalCartViewModel.class);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        ivCartIcon = findViewById(R.id.iv_cart_icon_product);
        frameLayout = findViewById(R.id.frame_cart_icon_products);
        ivComment = findViewById(R.id.iv_comment_product);
        tvLikeCount = findViewById(R.id.tv_like_count);
        txtViewCount = findViewById(R.id.txt_activityViewProduct_txtViewCount);
        cardNoItem = findViewById(R.id.card_no_item);
        imgLoadMore = findViewById(R.id.img_activityViewProduct_loadMore);
        txtSeeMore = findViewById(R.id.txt_activityViewProduct_seeFewer);
        linearLayoutSeeMore = findViewById(R.id.linear_activityViewProduct_seeMore);

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

       /* selectedPid = getIntent().getStringExtra("PID");


        *//*Log.d(TAG, "onCreate: " + selectedPid);
        Log.d(TAG, "onCreate: " + selectedProduct.ProductName);*//*
        int stock = selectedProduct.Discontinued;
        if (stock == 0) {
            btnAddToCart.setVisibility(View.GONE);
            cardNoItem.setVisibility(View.VISIBLE);
        }
        initCategoryRecyclerView();


        tvLikeCount.setText(String.valueOf(selectedProduct.LikeCount));
        txtViewCount.setText(StringUtils.showProductViewCount(selectedProduct.ViewedCount));


        loadingProduct();
        loadProductProperties();

        //init like
        handleLikeView();

        //init bookmark
        initBookmarkView();

        Log.d(TAG, "onCreate: isliked " + selectedProduct.Likeit);
        Log.d(TAG, "onCreate: " + selectedProduct.Likeit);

        if (selectedProduct.AddToCard) {
            showController();
        }*/

        final Retrofit retrofit = RetrofitInstance.getRetrofit();

        loadProductApi = retrofit.create(LoadProductApi.class);

        counterViewPost(selectedPid, userID, "1");


        like.setOnClickListener(v -> {
            setUpLike();
        });


        bookmark.setOnClickListener(v -> {
            try {
                if (selectedProduct.Saveit) {
                    bookmarked(selectedPid, userID, false);
                    bookmark.setImageResource(R.drawable.ic_bookmark);
                } else {
                    bookmarked(selectedPid, userID, true);
                    bookmark.setImageResource(R.drawable.ic_bookmarked);
                }

                YoYo.with(Techniques.RubberBand)
                        .playOn(bookmark);
            } catch (Exception e) {
                Log.d("Error", e.getMessage());
            }
        });

        ivAdd.setOnClickListener(v -> {
            addItemInCart();
        });

        ivRemove.setOnClickListener(v -> {
            removeItemFromCart();
        });

        ivCartIcon.setOnClickListener(v -> {
            //TODO
           /* Intent intent1 = new Intent(this, Main2Activity.class);
            intent1.putExtra(IntentKeys.INTENT_CART_PRODUCT_TO_MAIN, true);
            startActivity(intent1);*/
            finish();
        });

        ivComment.setOnClickListener(v -> {
            CommentsBottomSheet commentsBottomSheet = new CommentsBottomSheet(selectedPid, userID, BaseCodeClass.token, BaseCodeClass.CompanyID);
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
                EditBottomSheet editBottomSheet = EditBottomSheet.onNewInstance(0, txtPPrice.getText().toString(), selectedPid, BaseCodeClass.productFieldEnum.StandardCost, null);
                editBottomSheet.show(getSupportFragmentManager(), "EDIT_PRICE_TAG");
                return true;
            });
        }

        //get edited value from bottom sheet to refresh edit text
        productViewModel.getLiveEditedValue().observe(this, s -> {
            StandardPrice standardPrice = new StandardPrice();
            standardPrice.setShowPrice(s);
            txtPPrice.setText(s);
            /**
             * Check here
             */
            selectedProduct.StandardCost = standardPrice.getStandardCost();
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
            int likeCount = selectedProduct.LikeCount;
            Log.d(TAG, "setUpLike: bool " + selectedProduct.Likeit);

            if (selectedProduct.Likeit) {
                likePost(selectedPid, userID, false);
                like.setImageResource(R.drawable.ic_like);
                if (likeCount > 0)
                    selectedProduct.LikeCount = --likeCount;
            } else {
                likePost(selectedPid, userID, true);
                like.setImageResource(R.drawable.ic_liked);
                selectedProduct.LikeCount = ++likeCount;
            }

            tvLikeCount.setText(String.valueOf(selectedProduct.LikeCount));

            YoYo.with(Techniques.Bounce)
                    .duration(500)
                    .repeat(0)
                    .playOn(like);
        } catch (Exception e) {
            toastMessage("ViewProduct >> " + e.getMessage(), 2000);
        }
    }

    public void handleLikeView() {
        if (selectedProduct.Likeit) {
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

        Log.d(TAG, "initBookmarkView: " + selectedProduct.Saveit);
        if (selectedProduct.Saveit) {
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
            Log.d("Error", e.getMessage());
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
            Log.d("Error", e.getMessage());
        }
    }

    private void loadingProduct() {
        try {
            txtPName.setText(selectedProduct.ProductName);

            /// generate click for hashtags word

            String description = selectedProduct.Description;
            SpannableString spannableString = new SpannableString(description);
            String[] words2 = spannableString.toString().split("\\s+|\n,\\s*|\\.\\s*");
            for (final String word : words2) {
                if (word.startsWith("#")) {
                    ClickableSpan clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(@NonNull View widget) {
                            BaseCodeClass.hashtagsValue = word;
                            finish();
                        }
                    };
                    spannableString.setSpan(clickableSpan, spannableString.toString().indexOf(word), spannableString.toString().indexOf(word) + word.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                }
            }
            txtPDescription.setText(spannableString);
            txtPDescription.setMovementMethod(LinkMovementMethod.getInstance());

            txtPDescription.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    txtPDescription.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int lineCount = txtPDescription.getLineCount();

                    if (lineCount > 3) {
                        txtPDescription.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        linearLayoutSeeMore.setVisibility(View.VISIBLE);

                    } else {
                        linearLayoutSeeMore.setVisibility(View.GONE);

                    }

                    linearLayoutSeeMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (imgLoadMore.getContentDescription().equals("befor")) {
                                txtPDescription.setMaxLines(lineCount);
                                imgLoadMore.setImageResource(R.drawable.ic_un_more_load);
                                imgLoadMore.setContentDescription("after");
                                txtSeeMore.setText("جزئیات کمتر");
                                YoYo.with(Techniques.Bounce)
                                        .duration(700)
                                        .playOn(linearLayoutSeeMore);
                            } else {
                                txtPDescription.setMaxLines(3);
                                imgLoadMore.setImageResource(R.drawable.ic_more_details);
                                imgLoadMore.setContentDescription("befor");
                                txtSeeMore.setText("جزئیات بیشتر");
                                YoYo.with(Techniques.Bounce)
                                        .duration(700)
                                        .playOn(linearLayoutSeeMore);

                            }

                        }
                    });

                }
            });


            txtPPrice.setText(selectedProduct.ShowPrice);
            String category = selectedProduct.Category;
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
            PImage.setBackgroundColor(Color.parseColor(selectedProduct.Spare1));
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }

    }


    public void newDownloadImage(String pid, ImageView _imageView) {
        try {
            String url = baseCodeClass.BASE_URL + "Products/DownloadFile?ProductID=" + pid + "&fileNumber=1";
            Glide.with(this).load(url).into(_imageView);
        } catch (Exception e) {
            baseCodeClass.logMessage("ViewProduct glide :" + e.getMessage(), this);
        }
    }


    private void loadProductProperties() {
        int i = 0;
        File imgFile = null;
        try {
           /* Cursor cursor = mydb.GetProductProperties(this, selectedPid);
            List<String> s = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    mPropertyName.add(cursor.getString(cursor.getColumnIndex(mydb.PropertisName)));
                    mPropertyValue.add(cursor.getString(cursor.getColumnIndex(mydb.PropertisValue)));
                } while (cursor.moveToNext());
            }*/


            DBViewModel dbViewModel = new ViewModelProvider(this).get(DBViewModel.class);
            dbViewModel.getSpecificProperties(selectedPid).observe(this, new Observer<List<Properties>>() {
                @Override
                public void onChanged(List<Properties> propertiesList) {
                    for (Properties properties : propertiesList) {
                        mPropertyName.add(properties.PropertiesName);
                        mPropertyValue.add(properties.PropertiesValue);
                    }
                    initPropertyRecyclerView();
                }
            });

        } catch (Exception ex) {
            Log.d("Error", ex.getMessage());
        }
    }


    public void toastMessage(String message, int id) {
        Toast.makeText(ViewProductActivity.this, id + " >> " + message, Toast.LENGTH_LONG).show();
    }

    public void btnCancelClick(View view) {
        ProductRecyclerViewAdapter.isFinish = true;
        finish();
    }

    public void btnAddtoCart(View view) {
        selectedProduct.CartItemCount = 1;
        selectedProduct.AddToCard = true;
        dbViewModel.updateProduct(selectedProduct);
        dbViewModel.getSpecificProduct(selectedProduct.ProductID).observe(this, new Observer<Product>() {
            @Override
            public void onChanged(Product product) {
                if(product != null){
                    Toast.makeText(getApplicationContext(), "محصول با موفقیت به سبد خرید اضافه شد", Toast.LENGTH_SHORT).show();
                    btnAddToCart.setEnabled(false);
                }else{
                    Toast.makeText(getApplicationContext(), "لطفا دوباره تلاش کنید", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*try {



            if (selectedProduct.AddToCard) {

               // addItemInCart();
            } else {
                *//**
                 * Check the condition statement
                 *//*

                if (manageOrderClass.addProductToCart(selectedProduct)) {

                    showController();

                    badgeSharedViewModel.setCount(BadgeCounter.getCount() + 1);

                } else {
                    toastMessage("", 404);
                }

                dbViewModel.updateProduct(selectedProduct);
            }

            //localCartViewModel.updateCartInfo(sendOrderClass);
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }*/
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
            Log.d("Error", e.getMessage());
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
                        selectedProduct.Likeit = viewCount;
                        dbViewModel.updateProduct(selectedProduct);
                        /*for (Product allProductData :
                                productDataList) {
                            if (allProductData.ProductID.equals(selectedProduct.ProductID)) {
                                allProductData.Likeit = viewCount;
                            }
                        }*/
                    }
                }

                @Override
                public void onFailure(Call<GetResualt> call, Throwable t) {

                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
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

                        selectedProduct.Saveit = viewCount;
                        /*for (Product allProductData : productDataList) {
                            if (allProductData.ProductID.equals(selectedProduct.ProductID)) {
                                allProductData.Saveit = viewCount;
                            }
                        }*/
                        dbViewModel.updateProduct(selectedProduct);

                    }
                }

                @Override
                public void onFailure(Call<GetResualt> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
    }

    public void addItemInCart() {

        manageOrderClass.setProductQTY(selectedProduct.ProductID,
                manageOrderClass.getProductQTY(selectedProduct.ProductID) + 1);
        int currentCount = manageOrderClass.getProductQTY(selectedProduct.ProductID);
        tvCounter.setText(String.valueOf(currentCount));

    }

    public void removeItemFromCart() {
        String productId = selectedProduct.ProductID;
        int count = manageOrderClass.getProductQTY(selectedProduct.ProductID);

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
        /*for (Product allProductData : selectedProduct) {
            if (allProductData.ProductID.equals(pId)) {
                allProductData.AddToCard = false;
            }
        }*/

        dbViewModel.getSpecificProduct(pId).observe(this, new Observer<Product>() {
            @Override
            public void onChanged(Product product) {
                if (product != null) {
                    product.AddToCard = false;
                    dbViewModel.updateProduct(product);
                }
            }
        });

        Toast.makeText(this, "از سبد حذف شد", Toast.LENGTH_SHORT).show();
    }

    public void showController() {
        btnAddToCart.setVisibility(View.GONE);
        cardViewController.setVisibility(View.VISIBLE);
        int currentCount = manageOrderClass.getProductQTY(selectedProduct.ProductID);
        tvCounter.setText(String.valueOf(currentCount));
    }
}
