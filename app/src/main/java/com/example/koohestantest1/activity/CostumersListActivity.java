package com.example.koohestantest1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.koohestantest1.databinding.ActivityCostumersListBinding;

public class CostumersListActivity extends AppCompatActivity {

    private ActivityCostumersListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCostumersListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}