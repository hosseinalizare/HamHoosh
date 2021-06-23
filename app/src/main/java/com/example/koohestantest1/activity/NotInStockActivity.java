package com.example.koohestantest1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.koohestantest1.adapter.ProductRecyclerViewAdapterV2;
import com.example.koohestantest1.classDirectory.ProductRecyclerViewAdapter;
import com.example.koohestantest1.databinding.ActivityNotInStockBinding;
import com.example.koohestantest1.local_db.DBViewModel;
import com.example.koohestantest1.local_db.entity.Product;
import com.example.koohestantest1.viewModel.BadgeSharedViewModel;
import com.example.koohestantest1.viewModel.LocalCartViewModel;
import com.example.koohestantest1.viewModel.ProductViewModel;

import com.example.koohestantest1.classDirectory.BaseCodeClass;

import java.util.ArrayList;
import java.util.List;

public class NotInStockActivity extends AppCompatActivity {

    private ProductRecyclerViewAdapter adapterV2;
    private ProductViewModel productViewModel;
    private ActivityNotInStockBinding binding;
    private ImageView imgBack;
    private DBViewModel dbViewModel;
    private BadgeSharedViewModel badgeSharedViewModel;
    private LocalCartViewModel localCartViewModel;
    private LifecycleOwner lifecycleOwner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotInStockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        imgBack = binding.imgActivityNotInStockBack;
        imgBack.setOnClickListener(v -> finish());
        badgeSharedViewModel = new ViewModelProvider(this).get(BadgeSharedViewModel.class);
        localCartViewModel = new ViewModelProvider(this).get(LocalCartViewModel.class);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        lifecycleOwner = this;
        dbViewModel = new ViewModelProvider(this).get(DBViewModel.class);



        //adapterV2 = new ProductRecyclerViewAdapterV2(this , true,getSupportFragmentManager(),dbViewModel,this);



        dbViewModel.getDiscontinuedProduct().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                adapterV2 = new ProductRecyclerViewAdapter(getApplicationContext(),products,badgeSharedViewModel,localCartViewModel,getSupportFragmentManager(),dbViewModel,lifecycleOwner,NotInStockActivity.this);
                binding.rvNotInStockProducts.setAdapter(adapterV2);
                binding.rvNotInStockProducts.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                binding.pbNotInStockProducts.setVisibility(View.GONE);
            }
        });



        //call for not in stock
        /*productViewModel.callForNotInStockProducts(BaseCodeClass.CompanyID, BaseCodeClass.userID, BaseCodeClass.token);

        productViewModel.getLiveErrorStockProducts().observe(this, s -> {
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
            binding.pbNotInStockProducts.setVisibility(View.GONE);
        });*/

        /*productViewModel.getLiveStockProducts().observe(this, sendProductClasses -> {
            adapterV2.setData(sendProductClasses);
            binding.pbNotInStockProducts.setVisibility(View.GONE);
        });*/
    }
}