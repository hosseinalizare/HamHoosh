package com.example.koohestantest1.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.koohestantest1.MessageActivity;
import com.example.koohestantest1.MyStoreOrderListActivity;
import com.example.koohestantest1.R;
import com.example.koohestantest1.ShowStoreActivity;
import com.example.koohestantest1.adapter.ProfileViewPagerAdapter;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.fragments.FragmentTabsProfile;
import com.example.koohestantest1.model.Item;
import com.example.koohestantest1.model.ProfileModel;
import com.example.koohestantest1.viewModel.ProfileVM;
import com.example.koohestantest1.viewModel.UserProfileViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.companyProfile;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.context;

public class ActivityProfile extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ImageView imgProfile;
    private FloatingActionButton fbCall,fbMessage;
    private TextView txtBio,txtAddress,txtNuOfCustomer,txtNuOfSell,txtNuOfProduct;
    private ProgressBar prg;
    private UserProfileViewModel userProfileViewModel;
    private ProfileVM profileVM;
    private BaseCodeClass baseCodeClass;
   private List<String> titles ;
   private ProfileViewPagerAdapter pagerAdapter ;
   private ConstraintLayout cl_customer,cl_sellProduct,cl_product;
    public final static String STATE_MESSAGE_SENDER = "state_message_sender";
    public final static int REGULAR_USER = 0;




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupViews();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
       /* toolbar.setLogo(R.drawable.ic_call_fb);
        View logoView = getToolbarLogoIcon(toolbar);
        logoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityProfile.this, "yesssss777", Toast.LENGTH_SHORT).show();
            }
        });
*/
        if (actionBar!= null) {
           actionBar.setDisplayHomeAsUpEnabled(true);
            /*actionBar.setTitle("Abolfazl");
            actionBar.setSubtitle("on");*/

        }


        fbMessage.setOnClickListener(v -> {
            try {
                setAnimation(Techniques.Tada,100L,fbMessage);
                Intent intent = new Intent(ActivityProfile.this, MessageActivity.class);
                //sender = ourselfes
                intent.putExtra("sender", baseCodeClass.getUserID());
                //getter = company(others)
                intent.putExtra("getter", baseCodeClass.getCompanyID());
                intent.putExtra(STATE_MESSAGE_SENDER, REGULAR_USER);
                startActivity(intent);
            } catch (Exception e) {
                baseCodeClass.logMessage(e.getMessage() + "888", ActivityProfile.this);
            }
        });


        cl_customer.setOnClickListener(v -> {
            try {

                if (baseCodeClass.getPermissions().get(10).isState()) {
                    setAnimation(Techniques.Tada,100L,cl_customer);
                    Intent intent = new Intent(this, CostumersListActivity.class);
                    startActivity(intent);
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        });
        cl_sellProduct.setOnClickListener(v -> {

            try {

                if (baseCodeClass.getPermissions().get(10).isState()) {
                    setAnimation(Techniques.Tada,100L,cl_sellProduct);
                    Intent intent = new Intent(this, MyStoreOrderListActivity.class);
                    startActivity(intent);
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        });


        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setSelectedTabIndicatorColor(getColor(R.color.DarkYellow));

        tabLayout.setTabTextColors(Color.parseColor("#ffffff"), getColor(R.color.DarkYellow));
        profileVM = new ViewModelProvider(this).get(ProfileVM.class);
        profileVM.getProfileData(BaseCodeClass.CompanyID,BaseCodeClass.userID).observe(this, new Observer<ProfileModel>() {

            @Override
            public void onChanged(ProfileModel profileModel) {
                prg.setVisibility(View.GONE);
                txtBio.setText(profileModel.getBio());
                txtAddress.setText(profileModel.getAddres());
                txtNuOfCustomer.setText(profileModel.getFollowingCount());
                txtNuOfSell.setText(profileModel.getSaleCount());
                txtNuOfProduct.setText(profileModel.getProductCount());
                actionBar.setTitle(profileModel.getCompanyName());
                actionBar.setSubtitle(profileModel.getOnlineTime());

                Glide.with(ActivityProfile.this).load(generateUrlCompanyPicture(profileModel.getCompanyId())).placeholder(R.drawable.image_placeholder).into(imgProfile);

                for (Item item:profileModel.getItem()){
                    titles.add(item.getName());
                    pagerAdapter.addTab(new FragmentTabsProfile(item));
                }
                viewPager.setAdapter(pagerAdapter);
                viewPager.setCurrentItem(titles.size()-1);
                cl_product.setOnClickListener(v -> {
                        int position =viewPager.getCurrentItem();
                        int index =0;
                        for (Item item:profileModel.getItem()){
                          if (item.getGroupType() ==1){
                              position = index;
                              break;
                          }
                          index++;
                        }
                        setAnimation(Techniques.Tada,100L,cl_product);
                        viewPager.setCurrentItem(position);

                });
                fbCall.setOnClickListener(v -> {
                    setAnimation(Techniques.Tada,100L,fbCall);
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + profileModel.getBusnisePhone()));
                    startActivity(intent);

                });

                new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(titles.get(position));
                    }
                }).attach();

            }
        });
        changeTabsFont(tabLayout);
    }

    private void setupViews(){
        toolbar = findViewById(R.id.activityProfile_toolbar);
        tabLayout = findViewById(R.id.activityProfile_tabLayout);
        viewPager = findViewById(R.id.activityProfile_viewpager);
        imgProfile = findViewById(R.id.img_activityProfile_imgProfile);
        fbMessage = findViewById(R.id.fb_activityProfile_fbMessage);
        fbCall = findViewById(R.id.fbCallProfile);
        txtBio = findViewById(R.id.txt_activityProfile_txtBioValue);
        txtAddress = findViewById(R.id.txt_activityProfile_txtAddressValue);
        txtNuOfCustomer = findViewById(R.id.NoOfCustomer);
        txtNuOfSell = findViewById(R.id.NoOfSell);
        txtNuOfProduct = findViewById(R.id.NoOfProduct);
        prg = findViewById(R.id.prg_activityProfile);
        cl_customer = findViewById(R.id.cl_customers);
        cl_sellProduct =findViewById(R.id.root_noOfSell);
        cl_product=findViewById(R.id.cl_product);
        titles =new ArrayList<>();
        pagerAdapter =  new ProfileViewPagerAdapter(this);
        baseCodeClass = new BaseCodeClass();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static View getToolbarLogoIcon(Toolbar toolbar){
        //check if contentDescription previously was set
        boolean hadContentDescription = android.text.TextUtils.isEmpty(toolbar.getLogoDescription());
        String contentDescription = String.valueOf(!hadContentDescription ? toolbar.getLogoDescription() : "logoContentDescription");
        toolbar.setLogoDescription(contentDescription);
        ArrayList<View> potentialViews = new ArrayList<View>();
        //find the view based on it's content description, set programatically or with android:contentDescription
        toolbar.findViewsWithText(potentialViews,contentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        //Nav icon is always instantiated at this point because calling setLogoDescription ensures its existence
        View logoIcon = null;
        if(potentialViews.size() > 0){
            logoIcon = potentialViews.get(0);
        }
        //Clear content description if not previously present
        if(hadContentDescription)
            toolbar.setLogoDescription(null);
        return logoIcon;
    }



    private void changeTabsFont(TabLayout tabLayout) {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    AssetManager mgr = getAssets();
                    Typeface tf = Typeface.createFromAsset(mgr, "font/koodak.ttf");//Font file in /assets
                    ((TextView) tabViewChild).setTypeface(tf);
                }
            }
        }
    }

    private String generateUrlCompanyPicture(String id) {
        String url = baseCodeClass.BASE_URL + "Company/DownloadFile?CompanyID=" +id+ "&ImageAddress=" + 1;
        return url;
    }
    private void setAnimation(Techniques animation, Long duration, View view) {
        YoYo.with(animation)
                .duration(duration)
                .playOn(view);
    }


    @Override
    protected void onResume() {
        super.onResume();
        String hashtagWord = BaseCodeClass.hashtagsValue;
        if (hashtagWord != null) {
            finish();
        }
    }
}