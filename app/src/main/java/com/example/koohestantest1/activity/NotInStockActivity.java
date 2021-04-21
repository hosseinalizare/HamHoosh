package com.example.koohestantest1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.koohestantest1.adapter.ProductRecyclerViewAdapterV2;
import com.example.koohestantest1.databinding.ActivityNotInStockBinding;
import com.example.koohestantest1.viewModel.ProductViewModel;

import com.example.koohestantest1.classDirectory.BaseCodeClass;

public class NotInStockActivity extends AppCompatActivity {

    private ProductRecyclerViewAdapterV2 adapterV2;
    private ProductViewModel productViewModel;
    private ActivityNotInStockBinding binding;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotInStockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        imgBack = binding.imgActivityNotInStockBack;
        imgBack.setOnClickListener(v -> finish());

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        adapterV2 = new ProductRecyclerViewAdapterV2(this , true,getSupportFragmentManager());
        binding.rvNotInStockProducts.setAdapter(adapterV2);
        binding.rvNotInStockProducts.setLayoutManager(new LinearLayoutManager(this));

        //call for not in stock
        productViewModel.callForNotInStockProducts(BaseCodeClass.CompanyID, BaseCodeClass.userID, BaseCodeClass.token);

        productViewModel.getLiveErrorStockProducts().observe(this, s -> {
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
            binding.pbNotInStockProducts.setVisibility(View.GONE);
        });

        productViewModel.getLiveStockProducts().observe(this, sendProductClasses -> {
            adapterV2.setData(sendProductClasses);
            binding.pbNotInStockProducts.setVisibility(View.GONE);
        });
    }
}