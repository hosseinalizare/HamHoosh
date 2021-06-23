package com.example.koohestantest1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.koohestantest1.adapter.ProductRecyclerViewAdapterV2;
import com.example.koohestantest1.databinding.ActivityInvisibleProductsBinding;
import com.example.koohestantest1.local_db.DBViewModel;
import com.example.koohestantest1.viewModel.ProductViewModel;

import com.example.koohestantest1.classDirectory.BaseCodeClass;

public class InvisibleProductActivity extends AppCompatActivity {

    private ProductRecyclerViewAdapterV2 adapterV2;
    private ProductViewModel productViewModel;
    private ActivityInvisibleProductsBinding binding;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInvisibleProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        imgBack = binding.imgActivityInvisibleBack;

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        DBViewModel dbViewModel = new ViewModelProvider(this).get(DBViewModel.class);
        adapterV2 = new ProductRecyclerViewAdapterV2(this , true,getSupportFragmentManager(),dbViewModel,this);
        binding.rvInvisibleProducts.setLayoutManager(new LinearLayoutManager(this));
        binding.rvInvisibleProducts.setAdapter(adapterV2);

        //call for getting list
        productViewModel.callForInvisibleProducts(BaseCodeClass.userID, BaseCodeClass.CompanyID, BaseCodeClass.token);

        //listen for error
        productViewModel.getLiveErrorInvisibleProducts().observe(this, s -> {
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
            binding.pbInvisibleProducts.setVisibility(View.GONE);
        });

        //listen for data
        productViewModel.getLiveInvisibleProducts().observe(this, sendProductClasses -> {
            //adapterV2.setData(sendProductClasses);
            binding.pbInvisibleProducts.setVisibility(View.GONE);

        });
    }

    public void btnBack(View view) {
        finish();
    }
}