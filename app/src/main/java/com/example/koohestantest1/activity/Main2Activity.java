package com.example.koohestantest1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.koohestantest1.BaseActivity;
import com.example.koohestantest1.CartFragment;
import com.example.koohestantest1.ExplorerFragment;
import com.example.koohestantest1.Main2Fragment;
import com.example.koohestantest1.MyStoreFragment;
import com.example.koohestantest1.ProfileFragment;
import com.example.koohestantest1.R;
import com.example.koohestantest1.Utils.SharedPreferenceUtils;
import com.example.koohestantest1.ViewModels.Order_DetailsViewModels;
import com.example.koohestantest1.classDirectory.AppService;
import com.example.koohestantest1.constants.CurrentCartId;
import com.example.koohestantest1.constants.IntentKeys;
import com.example.koohestantest1.fragments.transinterface.CartTransitionInterface;
import com.example.koohestantest1.local_db.DBViewModel;
import com.example.koohestantest1.local_db.entity.Product;
import com.example.koohestantest1.model.entity.CartInformation;
import com.example.koohestantest1.viewModel.BadgeSharedViewModel;
import com.example.koohestantest1.viewModel.LocalCartViewModel;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import com.example.koohestantest1.classDirectory.BaseCodeClass;

import java.util.ArrayList;
import java.util.List;

import static com.example.koohestantest1.Utils.Cache.TAG;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.PageShow;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.badge;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.context;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.myAppPage.ExitThread;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.myAppPage.Search;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.myAppPage.ShoppCenter;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.myAppPage.cartpage;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.myAppPage.myProfile;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.myAppPage.myStor;

public class Main2Activity extends AppCompatActivity implements CartTransitionInterface {

    private Context mContext = Main2Activity.this;
    private boolean updateFromDb = true;

    View view;
    BottomNavigationView bottomNavigationView;
    private BaseCodeClass baseCodeClass = new BaseCodeClass();
    private final String TAG = Main2Activity.class.getSimpleName();
    private final Main2Fragment main2Fragment = new Main2Fragment(this);
    private ExplorerFragment explorerFragment = null;// = new ExplorerFragment();
    private MyStoreFragment myStoreFragment = null;// new MyStroreFragment();
    private CartFragment cartFragment = null;// = new CartFragment();
    private ProfileFragment profileFragment = null;// = new ProfileFragment();
    private final FragmentManager fm = getSupportFragmentManager();
    private DBViewModel dbViewModel;
    Fragment selectedFragment = main2Fragment;


    int anInt = 0;

    boolean myStoreClicked = false;

    private BadgeSharedViewModel badgeSharedViewModel;
    private LocalCartViewModel localCartViewModel;
    public Intent myServiceIntent = null;


    /**
     * onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ////// save userId into sharedPrefrence
      String uId =  SharedPreferenceUtils.getUserId(this);
      if (uId ==null){
          SharedPreferenceUtils.saveUserId(this,BaseCodeClass.userID);

      }

        dbViewModel = new ViewModelProvider(this).get(DBViewModel.class);
        ///// start service ////
        myServiceIntent = new Intent(this, AppService.class);
        startService(myServiceIntent);



//
        Log.d(TAG, "onCreate: ");
        view = findViewById(android.R.id.content);

        badgeSharedViewModel = new ViewModelProvider(this).get(BadgeSharedViewModel.class);
        localCartViewModel = new ViewModelProvider(this).get(LocalCartViewModel.class);

        //fm.beginTransaction().add(R.id.main2Container, myStoreFragment).commit();

        fm.beginTransaction().add(R.id.main2Container, main2Fragment).commit();

        //fm.beginTransaction().hide(myStoreFragment).commit();

        bottomNavigationView = findViewById(R.id.bottomNavigationViewBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


        badge = bottomNavigationView.getOrCreateBadge(R.id.ic_cart);


//        attachKeyboardListeners();

//        getFireBaseToken();


        /*localCartViewModel.getProductCount().observe(this, count -> {
            if (count == 0)
                badge.setVisible(false);
            else {
                badge.setVisible(true);
                badge.setNumber(count);
            }

        });*/

        dbViewModel.getCardItemCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                if (count == 0)
                    badge.setVisible(false);
                else {
                    badge.setVisible(true);
                    badge.setNumber(count);
                    badgeSharedViewModel.setCount(count);
                }
            }
        });


        /*
             setting up local cart
         */


        //get cart count in local db
        localCartViewModel.getCartCount().observe(this, integer -> {

            //there is no cart, crate one
            if (integer == 0) {
                CartInformation cartInformation = new CartInformation(0, false, null, null, null, null, null, baseCodeClass.getCompanyID(), null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                        null, null, null, null, null);

                localCartViewModel.insertCart(cartInformation);
            }
        });

        //listen for new cart id
        localCartViewModel.getInsertedCartId().observe(this, CurrentCartId::setId);
        appHelp();
    }

    /*private void getFireBaseToken() {
        FirebaseInstallations.getInstance().getToken(true).addOnSuccessListener(this, instanceIdResult -> {
            Log.d(TAG, "getFireBaseToken: " + instanceIdResult.getToken());
        });
    }*/


   /* private BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        try {
            Fragment fragment = new Fragment();
            switch (item.getItemId()) {
                case R.id.ic_shoppingCenter:
                    fragment = new Main2Fragment(this::onCartClickListener);

                    break;
                case R.id.ic_search:
                    fragment = new ExplorerFragment();
                    break;
                case R.id.ic_myStore:
                    fragment = new MyStoreFragment();
                    break;
                case R.id.ic_cart:
                    fragment = new CartFragment();
                    break;
                case R.id.ic_profile:
                    fragment = new ProfileFragment();
                    break;
            }

            fm.beginTransaction().replace(R.id.main2Container, fragment).commit();
            //hideCurrentFragment();
            *//*switch (item.getItemId()) {
                case R.id.ic_shoppingCenter:
                    fm.beginTransaction().show(main2Fragment).commit();
                    selectedFragment = main2Fragment;
                    PageShow = ShoppCenter;
                    BaseCodeClass.hashtagsValue = null;
                    break;
                case R.id.ic_search:
                    if (explorerFragment == null) {
                        explorerFragment = new ExplorerFragment();
                        fm.beginTransaction().add(R.id.main2Container, explorerFragment).commit();
                    }
                    fm.beginTransaction().show(explorerFragment).commit();
                    selectedFragment = explorerFragment;
                    PageShow = Search;
                    break;
                case R.id.ic_myStore:
                    PageShow = myStor;
                    if (myStoreFragment == null) {
                        myStoreFragment = new MyStoreFragment();
                        fm.beginTransaction().add(R.id.main2Container, myStoreFragment).commit();
                    }
                    fm.beginTransaction().show(myStoreFragment).commit();
                    selectedFragment = myStoreFragment;
//                    bottomNavClick = R.id.ic_myStore;
                    //         PageShow = myStor;
                    break;
                case R.id.ic_cart:

                    showCartFragment();
                    break;
                case R.id.ic_profile:
                    if (profileFragment == null) {
                        profileFragment = new ProfileFragment();
                        fm.beginTransaction().add(R.id.main2Container, profileFragment).commit();
                    }
                    fm.beginTransaction().show(profileFragment).commit();
                    selectedFragment = profileFragment;
                    PageShow = myProfile;
                    break;
            }*//*
            return true;
        } catch (Exception e) {
            toastMessage("onNavigationItemSelected :" + e.getMessage());
            return false;
        }
    };*/

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        try {
            hideCurrentFragment();
            switch (item.getItemId()) {
                case R.id.ic_shoppingCenter:
                    fm.beginTransaction().show(main2Fragment).commit();
                    selectedFragment = main2Fragment;
                    PageShow = ShoppCenter;
                    break;
                case R.id.ic_search:
                    if (explorerFragment == null) {
                        explorerFragment = new ExplorerFragment();
                        fm.beginTransaction().add(R.id.main2Container, explorerFragment).commit();
                    }
                    fm.beginTransaction().show(explorerFragment).commit();
                    selectedFragment = explorerFragment;
                    PageShow = Search;
                    break;
                case R.id.ic_myStore:
                    PageShow = myStor;
                    if (myStoreFragment == null) {
                        myStoreFragment = new MyStoreFragment();
                        fm.beginTransaction().add(R.id.main2Container, myStoreFragment).commit();
                    }
                    fm.beginTransaction().show(myStoreFragment).commit();
                    selectedFragment = myStoreFragment;
//                    bottomNavClick = R.id.ic_myStore;
                    //         PageShow = myStor;
                    break;
                case R.id.ic_cart:
                    showCartFragment();
                    break;
                case R.id.ic_profile:
                    if (profileFragment == null) {
                        profileFragment = new ProfileFragment();
                        fm.beginTransaction().add(R.id.main2Container, profileFragment).commit();
                    }
                    fm.beginTransaction().show(profileFragment).commit();
                    selectedFragment = profileFragment;
                    PageShow = myProfile;
                    break;
            }
            return true;
        } catch (Exception e) {
            toastMessage("onNavigationItemSelected :" + e.getMessage());
            return false;
        }
    };


    //
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            PageShow = ExitThread;
            finish();
//            localCartViewModel.updateCartInfo(baseCodeClass.sendOrderClass);
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(view, "برای خروج از برنامه دوبار کلیک کنید", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    private void hideCurrentFragment() {
        fm.beginTransaction().hide(selectedFragment).commit();
    }

    private void showCartFragment() {

        if (cartFragment == null) {
            cartFragment = new CartFragment();
            fm.beginTransaction().add(R.id.main2Container, cartFragment).commit();
        }
        fm.beginTransaction().

                show(cartFragment).

                detach(cartFragment).

                attach(cartFragment).

                commit();

        selectedFragment = cartFragment;
        PageShow = cartpage;


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");

        String hashtagWord = BaseCodeClass.hashtagsValue;
        if (hashtagWord != null) {
            bottomNavigationView.setSelectedItemId(R.id.ic_search);
        }

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(IntentKeys.INTENT_CART_PRODUCT_TO_MAIN)) {
            boolean shouldShowCart = intent.getBooleanExtra(IntentKeys.INTENT_CART_PRODUCT_TO_MAIN, false);
            if (shouldShowCart) {
                hideCurrentFragment();
                showCartFragment();
                bottomNavigationView.setSelectedItemId(R.id.ic_cart);
            }
        }
    }

    private void appHelp() {

        SharedPreferences sharedPreferences = this.getSharedPreferences("appHelp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        boolean hasSeen = sharedPreferences.getBoolean("help1", false);
        if (!hasSeen) {
            TapTargetSequence sequence = new TapTargetSequence(this);

            TapTarget shoppingCenterTarget = TapTarget.forView(bottomNavigationView.findViewById(R.id.ic_shoppingCenter), "مرکز خرید", "از طریق این صفحه می تونی محصولاتت رو انتخاب کنی، فیلتر کنی و به سبد خریدت اضافه کنی")
                    .cancelable(false)
                    .drawShadow(true)
                    .dimColor(android.R.color.tab_indicator_text)
                    .outerCircleColor(android.R.color.holo_blue_dark)
                    .targetCircleColor(android.R.color.holo_green_dark)
                    .transparentTarget(true)
                    .targetRadius(32)
                    .outerCircleAlpha(0.96f)
                    .titleTextSize(18)
                    .descriptionTextSize(15)
                    .descriptionTextColor(android.R.color.white)
                    .textColor(android.R.color.white)
                    .titleTextColor(android.R.color.white)
                    .tintTarget(false);
            TapTarget searchTarget = TapTarget.forView(bottomNavigationView.findViewById(R.id.ic_search), "جستجو", "از طریق این صفحه می تونی محصول موردنظر خودت رو جستجو کنی")
                    .cancelable(false)
                    .drawShadow(true)
                    .dimColor(android.R.color.tab_indicator_text)
                    .outerCircleColor(android.R.color.holo_blue_dark)
                    .targetCircleColor(android.R.color.holo_green_dark)
                    .transparentTarget(true)
                    .targetRadius(32)
                    .outerCircleAlpha(0.96f)
                    .titleTextSize(18)
                    .descriptionTextSize(15)
                    .descriptionTextColor(android.R.color.white)
                    .textColor(android.R.color.white)
                    .titleTextColor(android.R.color.white)
                    .tintTarget(false);

            TapTarget myStoreTarget = TapTarget.forView(bottomNavigationView.findViewById(R.id.ic_myStore), "فروشگاه من", "از اینجا می تونی پروفایل فرشگاه رو مشاهده کنی")
                    .cancelable(false)
                    .drawShadow(true)
                    .dimColor(android.R.color.tab_indicator_text)
                    .outerCircleColor(android.R.color.holo_blue_dark)
                    .targetCircleColor(android.R.color.holo_green_dark)
                    .transparentTarget(true)
                    .targetRadius(32)
                    .outerCircleAlpha(0.96f)
                    .titleTextSize(18)
                    .descriptionTextSize(15)
                    .descriptionTextColor(android.R.color.white)
                    .textColor(android.R.color.white)
                    .titleTextColor(android.R.color.white)
                    .tintTarget(false);

            TapTarget basketTarget = TapTarget.forView(bottomNavigationView.findViewById(R.id.ic_cart), "سبد خرید", "هروقت محصولت رو به سبد خرید اضافه کردی، از اینجا می تونی سبدت رو ویرایش کنی")
                    .cancelable(false)
                    .drawShadow(true)
                    .dimColor(android.R.color.tab_indicator_text)
                    .outerCircleColor(android.R.color.holo_blue_dark)
                    .targetCircleColor(android.R.color.holo_green_dark)
                    .transparentTarget(true)
                    .targetRadius(32)
                    .outerCircleAlpha(0.96f)
                    .titleTextSize(18)
                    .descriptionTextSize(15)
                    .descriptionTextColor(android.R.color.white)
                    .textColor(android.R.color.white)
                    .titleTextColor(android.R.color.white)
                    .tintTarget(false);

            TapTarget profileTarget = TapTarget.forView(bottomNavigationView.findViewById(R.id.ic_profile), "پروفایل من", "از طریق این صفحه می تونی پروفایل شخصی خودت رو ایجاد یا ویرایش کنی")
                    .cancelable(false)
                    .drawShadow(true)
                    .dimColor(android.R.color.tab_indicator_text)
                    .outerCircleColor(android.R.color.holo_blue_dark)
                    .targetCircleColor(android.R.color.holo_green_dark)
                    .transparentTarget(true)
                    .targetRadius(32)
                    .outerCircleAlpha(0.96f)
                    .titleTextSize(18)
                    .descriptionTextSize(15)
                    .descriptionTextColor(android.R.color.white)
                    .textColor(android.R.color.white)
                    .titleTextColor(android.R.color.white)
                    .tintTarget(false);


            sequence.targets(shoppingCenterTarget, searchTarget, myStoreTarget, basketTarget, profileTarget);
            sequence.listener(new TapTargetSequence.Listener() {
                @Override
                public void onSequenceFinish() {
                    editor.putBoolean("help1", true);
                    editor.apply();

                    main2Fragment.appHelp();

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

    @Override
    public void onCartClickListener() {
        //hideCurrentFragment();
        showCartFragment();
        bottomNavigationView.setSelectedItemId(R.id.ic_cart);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        BaseCodeClass.hashtagsValue = null;

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myServiceIntent = new Intent(this, AppService.class);
        startService(myServiceIntent);



    }




}
