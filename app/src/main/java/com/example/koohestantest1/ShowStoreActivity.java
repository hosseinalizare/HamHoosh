package com.example.koohestantest1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.CompanyEmployeesClass;
import com.example.koohestantest1.classDirectory.MyStoreProductRecyclerViewAdapter;

import java.util.List;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.companyProfile;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.productDataList;

public class ShowStoreActivity extends AppCompatActivity {

    private Context mContext;
    public final static String STATE_MESSAGE_SENDER = "state_message_sender";
    public final static int REGULAR_USER = 0;
    public final static int COMPANY_USER = 1;
    RecyclerView gridRecyclerView;
    private ImageView ivCompanyImage;
    MyStoreProductRecyclerViewAdapter adapter;
    BaseCodeClass baseCodeClass;
    TextView storeName, storeBio, NoOfCustomer, NoOfSell, NoOfProduct;
    public  void RunChatActivity(){
        btnSendMessageClick(null);

    }

    ActivityResultLauncher<String> callPermissionResult = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    //permission granted
                    makePhoneCall(companyProfile.getBusinessPhone());
                } else {
                    //permission denied
                    Toast.makeText(mContext, "بدون اجازه دسترسی قادر به تماس گرفتن نیستیم!", Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_store);



        ivCompanyImage = findViewById(R.id.iv_company_image);
        gridRecyclerView = findViewById(R.id.productGridRecyclerView);
        storeName = findViewById(R.id.StoreName);
        storeBio = findViewById(R.id.StoreBio);
        NoOfCustomer = findViewById(R.id.NoOfCustomer);
        NoOfSell = findViewById(R.id.NoOfSell);
        NoOfProduct = findViewById(R.id.NoOfProduct);

        mContext = ShowStoreActivity.this;
        baseCodeClass = new BaseCodeClass();




        storeName.setText(companyProfile.getCompanyName());
        String bio = companyProfile.getBio() == null ? "" : companyProfile.getBio();
        String neighborHood = companyProfile.getNeighborhood() == null ? "" : companyProfile.getNeighborhood();
        String address = companyProfile.getAddress() == null ? "" : "،" + companyProfile.getAddress();
        storeBio.setText(bio + "\n" + neighborHood + address);
        NoOfCustomer.setText(companyProfile.getFollowingCount());
        NoOfSell.setText(companyProfile.getOrderCount());
        NoOfProduct.setText(companyProfile.getProductCount());


        initRecyclerView();

        loadCompanyImage();



/*        if (isNotif){
            try {
                Intent intent = new Intent(ShowStoreActivity.this, MessageActivity.class);
                //sender = ourselfes
                intent.putExtra("sender", baseCodeClass.getUserID());
                //getter = company(others)
                intent.putExtra("getter", baseCodeClass.getCompanyID());
                intent.putExtra(STATE_MESSAGE_SENDER, REGULAR_USER);
                startActivity(intent);
            } catch (Exception e) {
                baseCodeClass.logMessage(e.getMessage() + "888", mContext);
            }
        }*/

    }

    private void loadCompanyImage() {
        String url = baseCodeClass.BASE_URL + "Company/DownloadFile?CompanyID=" + baseCodeClass.getCompanyID() + "&ImageAddress=" + 1;
        Glide.with(this).load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(ivCompanyImage);
    }

    public void initRecyclerView() {
        try {
            if (productDataList.size() > 0) {
                GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false);
                gridRecyclerView.setLayoutManager(layoutManager);
                gridRecyclerView.setNestedScrollingEnabled(true);
                gridRecyclerView.setFocusable(false);
                adapter = new MyStoreProductRecyclerViewAdapter(mContext, productDataList, false);
                gridRecyclerView.setAdapter(adapter);
            }

        } catch (Exception e) {
            logMessage("ShowStore 200 >> " + e.getMessage(), mContext);
        }
    }

    public void btnBackClick(View view) {
        finish();
    }

    public void btnSendMessageClick(View view) {

        try {
            Intent intent = new Intent(ShowStoreActivity.this, MessageActivity.class);
            //sender = ourselfes
            intent.putExtra("sender", baseCodeClass.getUserID());
            //getter = company(others)
            intent.putExtra("getter", baseCodeClass.getCompanyID());
            intent.putExtra(STATE_MESSAGE_SENDER, REGULAR_USER);
            startActivity(intent);
        } catch (Exception e) {
            baseCodeClass.logMessage(e.getMessage() + "888", mContext);
        }
    }

    public void btnCallToStore(View view) {
                /*
            checks for user permission
         */
        /**
         * strategy for calling changed
         */
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
//                == PackageManager.PERMISSION_GRANTED) {
//
//            //permission is already granted
//            makePhoneCall(companyProfile.getBusinessPhone());
//        } else {
//            //convince user to grant a permission
//            showDialogPhoneCall();
//        }
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + companyProfile.getBusinessPhone()));
        startActivity(intent);
    }

    private void showDialogPhoneCall() {

        //show a dialog to convince user
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("برای تماس با فروشگاه نیازه که اجازه دسترسی داده بشه.");

        builder.setNegativeButton("نه", (dialog, which) -> Toast.makeText(mContext, "بدون اجازه دسترسی قادر به تماس گرفتن نیستیم!", Toast.LENGTH_LONG).show());

        //user is convinced, launch permission
        builder.setPositiveButton("باشه", (dialog, which) -> callPermissionResult.launch(Manifest.permission.CALL_PHONE));

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void makePhoneCall(String businessPhone) {
        Uri phoneUri = Uri.parse("tel:" + businessPhone);
        Intent intent = new Intent(Intent.ACTION_CALL, phoneUri);
        startActivity(intent);
    }

    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(mContext,v);
        int itemId=1;

        List<CompanyEmployeesClass> employeePosition = BaseCodeClass.companyEmployees;
        for (CompanyEmployeesClass employeesClass:employeePosition){
            if (employeesClass.getPosition().equals("owner")){
                employeesClass.setPosition("مالک");
            }
            popupMenu.getMenu().add(0, itemId, Menu.NONE,employeesClass.getPosition());
            itemId++;

        }

        popupMenu.getMenu().add(0, 100, Menu.NONE,"منوی گفتگو");



        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case 100:
                        Intent intent2 = new Intent(mContext,ListMessageActivity.class);
                        intent2.putExtra("id", baseCodeClass.getUserID());
                        startActivity(intent2);
                        break;

                    default:
                        Intent intent = new Intent(ShowStoreActivity.this, MessageActivity.class);
                        //sender = ourselfes
                        intent.putExtra("sender",BaseCodeClass.userID);
                        //getter = company(others)
                        intent.putExtra("getter",  employeePosition.get(item.getItemId()-1).getUserID());
                        intent.putExtra(STATE_MESSAGE_SENDER, REGULAR_USER);
                        startActivity(intent);
                        break;

                }
              /*  switch (item.getItemId()){
                    case 1:
                        Toast.makeText(mContext, "1", Toast.LENGTH_SHORT).show();
                        break;

                    case 2:
                        Toast.makeText(mContext, "2", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(mContext, "3", Toast.LENGTH_SHORT).show();
                        break;


                }*/
                return false;
            }
        });

        popupMenu.show();


    }


    public void btnMenuOnClick(View view) {
        showPopup(view);
    }
}
