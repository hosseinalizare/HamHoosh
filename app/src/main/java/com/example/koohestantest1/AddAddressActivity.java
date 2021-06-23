package com.example.koohestantest1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

import com.example.koohestantest1.ApiDirectory.AddressApi;
import com.example.koohestantest1.InfoDirectory.MangeAddressClass;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.AddressViewModel;
import com.example.koohestantest1.classDirectory.LoadAddressRecyclerViewAdapter;
import retrofit2.Call;

public class AddAddressActivity extends AppCompatActivity implements AddressApi {

    BaseCodeClass baseCodeClass;

    ImageButton ibBack;
    LoadAddressRecyclerViewAdapter adapter;
    public static int REQ_CODE_EDIT_ADDRESS = 2;
    public static String KEY_INTENT_EDIT = "key_intent_edit_address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        ibBack = findViewById(R.id.ib_add_address_back);

        baseCodeClass = new BaseCodeClass();


        baseCodeClass.LoadBaseData(this);


        ibBack.setOnClickListener(view -> {
            finish();
        });

        initRecyclerView();

    }

    private void initRecyclerView() {
        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(AddAddressActivity.this);
            RecyclerView recyclerView = findViewById(R.id.addressRecyclerView);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new LoadAddressRecyclerViewAdapter(AddAddressActivity.this, BaseCodeClass.allAddress, this);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            BaseCodeClass.logMessage("addAddress 199 >> " + e.getMessage(), this);
        }
    }


    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void addNewAddressClick(View view) {
        Intent i = new Intent(AddAddressActivity.this, AddNewAddressActivity.class);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == 1) {
                if (resultCode == Activity.RESULT_OK) {
                    initRecyclerView();
                    toastMessage("آدرس با موفقیت ذخیره شد");
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    toastMessage("آدرسی ذخیره نشد!");
                }
            }

            if (requestCode == REQ_CODE_EDIT_ADDRESS) {
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
            }
        } catch (Exception e) {
            Log.d("AddAddress 599 >> " ,e.toString());
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
//        initRecyclerView();
        try {
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.d("AddAddress 499 >> " , e.getMessage());
        }
    }

    @Override
    public Call<GetResualt> delAddress(AddressViewModel addressViewModel) {
        return null;
    }

    @Override
    public void onResponseDelAddress(GetResualt getResualt) {
        try {
            new MangeAddressClass(this).getMyAddress(baseCodeClass.getUserID());
        } catch (Exception e) {
            Log.d("AddAddress 799 >> " , e.getMessage());
        }
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
        try {
            new MangeAddressClass(this).DelMyAddress(address);
        } catch (Exception e) {
            Log.d("AddAddress 699 >>" , e.getMessage());
        }
    }

    @Override
    public void CartRecyclerViewClickListener(View v, boolean b) {

    }

    @Override
    public void CartPaymentClickListener(int id) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerView();
    }
}
